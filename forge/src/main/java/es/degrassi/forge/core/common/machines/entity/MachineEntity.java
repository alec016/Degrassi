package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.forge.api.utils.DegrassiLogger;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.MachineStatus;
import es.degrassi.forge.core.common.processor.MachineProcessor;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineEntity<R extends MachineRecipe> extends BlockEntity {
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<EnergyComponent> lazyEnergyHandler = LazyOptional.empty();
  private final ComponentManager componentManager;
  private final ElementManager elementManager;
  protected MachineProcessor<R> processor;
  private MachineStatus status = MachineStatus.IDLE;
  private Component errorMessage = Component.empty();

  public MachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
    this.componentManager = new ComponentManager(this);
    this.elementManager = new ElementManager(this);
  }

  public ComponentManager getComponentManager() {
    return componentManager;
  }

  public ElementManager getElementManager() {
    return elementManager;
  }

  public MachineProcessor<R> getProcessor() {
    return processor;
  }

  public abstract Component getName();

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    saveAdditional(nbt);
    return nbt;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    componentManager.deserializeNBT(tag.getCompound("componentManager"));
    elementManager.deserializeNBT(tag.getCompound("elementManager"));
    processor.deserializeNBT(tag.getCompound("processor"));
    status = MachineStatus.value(tag.getString("status"));
    errorMessage = Component.literal(tag.getString("errorMessage"));
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("componentManager", componentManager.serializeNBT());
    tag.put("elementManager", elementManager.serializeNBT());
    tag.put("processor", processor.serializeNBT());
    tag.putString("status", status.toString());
    tag.putString("errorMessage", errorMessage.getString());
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      if (!componentManager.get().stream().filter(component -> component instanceof EnergyComponent).toList().isEmpty()) {
        return lazyEnergyHandler.cast();
      }
    } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
      if (!componentManager.get().stream().filter(component -> component instanceof ItemComponent).toList().isEmpty()) {
        return lazyItemHandler.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    getComponentManager()
      .get()
      .stream()
      .filter(component -> component instanceof EnergyComponent)
      .map(component -> (EnergyComponent) component)
      .findFirst()
      .ifPresent(energy -> lazyEnergyHandler = LazyOptional.of(() -> energy));
    getComponentManager()
      .get()
      .stream()
      .filter(component -> component instanceof ItemComponent)
      .map(component -> (ItemComponent) component)
      .findFirst()
      .ifPresent(item -> lazyItemHandler = LazyOptional.of(() -> item));
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyEnergyHandler.invalidate();
    lazyItemHandler.invalidate();
  }

  public static <R extends MachineRecipe> void clientTick (
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity<R> entity
  ) {
    entity.getComponentManager().clientTick();
    entity.getElementManager().clientTick();
    setChanged(level, pos, state);
  }

  public static <R extends MachineRecipe> void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity<R> entity
  ) {
    entity.getComponentManager().serverTick();
    entity.getElementManager().serverTick();
    switch (entity.getStatus()) {
      case IDLE -> entity.getProcessor().searchForRecipe(entity.getComponentManager().get());
      case RUNNING -> entity.getProcessor().tick();
      case ERROR -> {
        DegrassiLogger.INSTANCE.info("error occurred: {}", entity.errorMessage.getString());
        entity.getComponentManager().getComponent(ProgressComponent.id).map(component -> (ProgressComponent) component).ifPresent(component -> {
          if (entity.getProcessor().shouldReset()) {
            component.resetProgress();
          }
          Timer timer = new Timer();
          timer.schedule(new TimerTask() {
            @Override
            public void run() {
              entity.setRunning();
              this.cancel();
              timer.cancel();
            }
          }, 1000);
        });
      }
    }
    setChanged(level, pos, state);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public MachineStatus getStatus() {
    return status;
  }

  public void resetErrorMessage() {
    this.errorMessage = Component.empty();
  }

  public void setErrored(Component errorMessage) {
    setStatus(MachineStatus.ERROR);
    this.errorMessage = errorMessage;
  }

  public void setRunning() {
    setStatus(MachineStatus.RUNNING);
  }

  public void setIdle() {
    setStatus(MachineStatus.IDLE);
  }

  private void setStatus(MachineStatus status) {
    this.status = status;
    setChanged();
  }
}