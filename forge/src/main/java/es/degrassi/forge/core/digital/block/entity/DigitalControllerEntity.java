package es.degrassi.forge.core.digital.block.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.digital.block.DigitalController;
import es.degrassi.forge.core.digital.network.Network;
import es.degrassi.forge.core.digital.recipes.DigitalControllerRecipe;
import es.degrassi.forge.core.digital.util.Networks;
import es.degrassi.forge.core.digital.util.PassiveDrains;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public class DigitalControllerEntity extends MachineEntity<DigitalControllerRecipe> {
  private Network currentNetwork;

  public DigitalControllerEntity(BlockPos pos, BlockState blockState) {
    super(EntityRegistration.DIGITAL_CONTROLLER.get(), pos, blockState);
    componentManager.addEnergy(0, Integer.MAX_VALUE, 0, "energy", ComponentIOMode.INPUT);
  }

  @Override
  public Component getName() {
    return getBlockState().getBlock().getName();
  }

  @Override
  public DigitalControllerEntity copy(boolean dummy) {
    return dummy ? dummyEntity() : new DigitalControllerEntity(getBlockPos(), getBlockState());
  }

  public static DigitalControllerEntity dummyEntity() {
    return new DigitalControllerEntity(BlockPos.ZERO, BlockRegistration.DIGITAL_CONTROLLER.get().defaultBlockState()) {
      public boolean dummy() {
        return true;
      }
    };
  }

  public static void clientTick (
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull DigitalControllerEntity entity
  ) {
    entity.getComponentManager().clientTick();
    entity.getElementManager().clientTick();
    setChanged(level, pos, state);
  }

  public static void serverTick(
    @NotNull ServerLevel level,
    BlockPos pos,
    BlockState state,
    @NotNull DigitalControllerEntity entity
  ) {

    setChanged(level, pos, state);
  }

  public void onNetworkSelected(String frequency) {
    if (getLevel().isClientSide()) return;
    this.currentNetwork = Networks.get((ServerLevel) getLevel()).getOrCreateNetwork(frequency, getLevel());
    BlockState state = getBlockState().setValue(DigitalController.ACTIVE, true);
    this.currentNetwork.setController(getBlockPos(), (ServerLevel) getLevel());
    int capacity = PassiveDrains.CONTROLLER + PassiveDrains.getTotalFromConnections(currentNetwork.getConnections(), getLevel());
    componentManager.getComponent("energy").map(component -> (EnergyComponent) component).ifPresent(component -> {
      component.setCapacity(capacity + 100);
      component.setMaxInput(Integer.MAX_VALUE);
      component.setMaxOutput(Integer.MAX_VALUE);
    });
    level.setBlockAndUpdate(getBlockPos(), state);
    setChanged();
  }

  public void onNetworkRemove(String frequency) {
    if (getLevel().isClientSide()) return;
    this.currentNetwork = null;
    Networks.get((ServerLevel) getLevel()).getNetwork(frequency, getLevel()).removeController(getBlockPos(), (ServerLevel) getLevel());
    BlockState state = getBlockState().setValue(DigitalController.ACTIVE, false);
    componentManager.getComponent("energy").map(component -> (EnergyComponent) component).ifPresent(component -> {
      component.setCapacity(0);
      component.setMaxInput(0);
      component.setMaxOutput(0);
    });
    level.setBlockAndUpdate(getBlockPos(), state);
    setChanged();
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    if (currentNetwork != null) tag.putString("network_frequency", currentNetwork.getFrequency());
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    if (getLevel().isClientSide()) return;
    if (tag.contains("network_frequency"))
      this.currentNetwork = Networks.get((ServerLevel) getLevel()).getOrCreateNetwork(tag.getString("network_frequency"), getLevel());
  }
}
