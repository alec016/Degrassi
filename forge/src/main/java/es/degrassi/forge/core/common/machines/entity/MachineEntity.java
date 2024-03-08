package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.processor.MachineProcessor;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.crafting.RecipeType;
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
  private final MachineProcessor<R> processor;
  private MachineStatus status = MachineStatus.IDLE;

  public MachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, RecipeType<R> recipeType) {
    super(type, pos, blockState);
    this.componentManager = new ComponentManager(this);
    this.elementManager = new ElementManager(this);
    this.processor = new MachineProcessor<>(this, recipeType);
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

  public void setStatus(MachineStatus status) {
    this.status = status;
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
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("componentManager", componentManager.serializeNBT());
    tag.put("elementManager", elementManager.serializeNBT());
    tag.put("processor", processor.serializeNBT());
    tag.putString("status", status.toString());
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      if (!componentManager.get().stream().filter(component -> component instanceof EnergyComponent).toList().isEmpty()) {
        return lazyEnergyHandler.cast();
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
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyEnergyHandler.invalidate();
  }

  public static <R extends MachineRecipe> void clientTick (
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity<R> entity
  ) {
    entity.getComponentManager().clientTick();
    entity.getElementManager().clientTick();
  }

  public static <R extends MachineRecipe> void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity<R> entity
  ) {
    entity.getComponentManager().serverTick();
    entity.getElementManager().serverTick();
    switch (entity.status) {
      case IDLE -> entity.getProcessor().searchForRecipe(entity.getComponentManager().get());
      case RUNNING -> entity.getProcessor().tick(entity.getComponentManager().get());
      case ERROR ->
        entity.getComponentManager().getComponent(ProgressComponent.id).map(component -> (ProgressComponent) component).ifPresent(component -> {
          if (entity.getStatus() == MachineStatus.ERROR) {
            if (entity.getProcessor().shouldReset()) {
              component.resetProgress();
            }
//            entity.setStatus(MachineStatus.RUNNING);
          }
        });
    }
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public MachineStatus getStatus() {
    return status;
  }

  public enum MachineStatus {
    IDLE, RUNNING, ERROR;
    public static MachineStatus value(String value) {
      if (value.equalsIgnoreCase("idle")) return IDLE;
      if (value.equalsIgnoreCase("running")) return RUNNING;
      if (value.equalsIgnoreCase("error")) return ERROR;
      return null;
    }
  }
}