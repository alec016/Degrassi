package es.degrassi.forge.core.digital.block.entity;

import com.google.gson.JsonObject;
import es.degrassi.common.utils.DegrassiLogger;
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
import es.degrassi.forge.core.network.NetworkSelectionPacket;
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
    componentManager.addEnergy(100, Integer.MAX_VALUE, Integer.MAX_VALUE, "energy", ComponentIOMode.INPUT);
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
    entity.getComponentManager().serverTick();
    entity.getElementManager().serverTick();
    setChanged(level, pos, state);
  }

  public void onNetworkSelected(Network network, BlockPos pos) {
//    network.setController(pos);
    this.setCurrentNetwork(network);
    BlockState state = getBlockState().setValue(DigitalController.ACTIVE, true);
    componentManager.getComponent("energy").map(component -> (EnergyComponent) component).ifPresent(component -> {
      component.setCapacity(100 + PassiveDrains.CONTROLLER + PassiveDrains.getTotalFromConnections(network.getConnections(), getLevel()));
      component.setMaxInput(Integer.MAX_VALUE);
      component.setMaxOutput(Integer.MAX_VALUE);
    });
    level.setBlockAndUpdate(getBlockPos(), state);
    setChanged();
  }

  public void onNetworkSelected(String frequency) {
    DegrassiLogger.INSTANCE.info("Trying to select network...");
    new NetworkSelectionPacket(frequency, this.getBlockPos()).sendToServer();
  }

  public void onNetworkRemove(String frequency) {
    if (level.isClientSide()) return;
    this.currentNetwork = null;
    Networks.getNetwork(frequency, (ServerLevel) level).removeController();
    BlockState state = getBlockState().setValue(DigitalController.ACTIVE, false);
    componentManager.getComponent("energy").map(component -> (EnergyComponent) component).ifPresent(component -> {
      component.setCapacity(100);
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
      this.currentNetwork = Networks.get((ServerLevel) getLevel()).getOrCreateNetwork(tag.getString("network_frequency"), (ServerLevel) getLevel());
  }

  public JsonObject asJson() {
    JsonObject json = super.asJson();
    if (currentNetwork != null) json.add("network", currentNetwork.asJson());
    return json;
  }
}
