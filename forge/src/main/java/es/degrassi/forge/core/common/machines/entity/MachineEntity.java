package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.processor.MachineProcessor;
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

public abstract class MachineEntity extends BlockEntity {
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<EnergyComponent> lazyEnergyHandler = LazyOptional.empty();
  private final ComponentManager componentManager;
  private final ElementManager elementManager;
  private final MachineProcessor processor;

  public MachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
    this.componentManager = new ComponentManager(this);
    this.elementManager = new ElementManager(this);
    this.processor = new MachineProcessor(this);
  }

  public ComponentManager getComponentManager() {
    return componentManager;
  }

  public ElementManager getElementManager() {
    return elementManager;
  }

  public MachineProcessor getProcessor() {
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
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("componentManager", componentManager.serializeNBT());
    tag.put("elementManager", elementManager.serializeNBT());
    tag.put("processor", processor.serializeNBT());
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

  public static void clientTick (
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity entity
  ) {
    entity.getComponentManager().clientTick();
    entity.getElementManager().clientTick();
  }

  public static void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull MachineEntity entity
  ) {
    entity.getComponentManager().serverTick();
    entity.getElementManager().serverTick();
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}