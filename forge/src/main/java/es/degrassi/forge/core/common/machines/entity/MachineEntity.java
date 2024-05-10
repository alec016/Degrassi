package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.MachineStatus;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.common.processor.MachineProcessor;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import es.degrassi.forge.core.common.wrapper.DegrassiItemStackHandler;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Getter;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineEntity<R extends MachineRecipe<R>> extends BlockEntity {
  protected LazyOptional<DegrassiItemStackHandler> lazyItemHandler = LazyOptional.empty();
  protected LazyOptional<EnergyComponent> lazyEnergyHandler = LazyOptional.empty();
  protected LazyOptional<DegrassiFluidHandler> lazyFluidHandler = LazyOptional.empty();
  protected DegrassiFluidHandler fluidHandler;
  protected DegrassiItemStackHandler itemHandler;
  @Getter
  protected final ComponentManager componentManager;
  @Getter
  protected final ElementManager elementManager;
  @Getter
  @Nullable
  protected MachineProcessor<R, ? extends MachineEntity<R>> processor = null;
  @Getter
  protected MachineStatus status = null;
  @Getter
  protected Component errorMessage = Component.empty();

  public MachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
    this.componentManager = new ComponentManager(this);
    this.elementManager = new ElementManager(this);

    if(blockState.hasProperty(MachineBlock.STATUS)) {
      componentManager.addProgress();
      status = MachineStatus.IDLE;
    }

    this.fluidHandler = componentManager.getFluidHandler();
    this.itemHandler = componentManager.getItemHandler();
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
    super.handleUpdateTag(tag);
    load(tag);
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    componentManager.deserializeNBT(tag.getList("componentManager", CompoundTag.TAG_COMPOUND));
    elementManager.deserializeNBT(tag.getList("elementManager", CompoundTag.TAG_COMPOUND));
    if (tag.contains("processor") && processor != null) processor.deserializeNBT(tag.getCompound("processor"));
    if (tag.contains("status")) status = MachineStatus.value(tag.getString("status"));
    errorMessage = Component.literal(tag.getString("errorMessage"));
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("componentManager", componentManager.serializeNBT());
    tag.put("elementManager", elementManager.serializeNBT());
    if (processor != null) tag.put("processor", processor.serializeNBT());
    if (status != null) tag.putString("status", status.getSerializedName());
    tag.putString("errorMessage", errorMessage.getString());
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      if (!componentManager.getComponentsByType("energy").isEmpty()) {
        return lazyEnergyHandler.cast();
      }
    } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
      if (!componentManager.getComponentsByType("item").isEmpty()) {
        return lazyItemHandler.cast();
      }
    } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
      if (!componentManager.getComponentsByType("fluid").isEmpty()) {
        return lazyFluidHandler.cast();
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

    if (!getComponentManager().getItemHandler().getComponents().isEmpty()) {
      lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    if(!getComponentManager().getFluidHandler().getComponents().isEmpty()) {
      lazyFluidHandler = LazyOptional.of(() -> fluidHandler);
    }
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyEnergyHandler.invalidate();
    lazyItemHandler.invalidate();
    lazyFluidHandler.invalidate();
  }

  public static <R extends MachineRecipe<R>> void clientTick (
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity<R> entity
  ) {
    entity.getComponentManager().clientTick();
    entity.getElementManager().clientTick();
    setChanged(level, pos, state);
  }

  public static <R extends MachineRecipe<R>> void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity<R> entity
  ) {
    entity.getComponentManager().serverTick();
    entity.getElementManager().serverTick();
    if (entity.getProcessor() == null) return;
    if (entity.getStatus() != null) switch (entity.getStatus()) {
      case IDLE -> entity.getProcessor().searchForRecipe(entity.getComponentManager().get());
      case RUNNING -> entity.getProcessor().tick();
      case ERROR -> {
        DegrassiLogger.INSTANCE.info("error occurred: {}", entity.errorMessage.getString());
        entity.getComponentManager().getComponent("progress").map(component -> (ProgressComponent) component).ifPresent(component -> {
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
    this.level.setBlockAndUpdate(worldPosition, getBlockState().setValue(MachineBlock.STATUS, status));
    requestModelDataUpdate();
    setChanged();
  }

  public boolean dummy() {
    return false;
  }
}