package es.degrassi.forge.core.digital.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.digital.block.DigitalController;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.digital.util.Networks;
import es.degrassi.forge.core.digital.util.PassiveDrains;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Getter
public class Network {
  private final String frequency;
  private final List<BlockPos> connections;
  private BlockPos controllerPos;

  public Network(String frequency) {
    this.frequency = frequency;
    this.connections = new LinkedList<>();
  }

  public boolean setController(BlockPos pos, ServerLevel level) {
    if (level.getBlockState(pos).getBlock() instanceof DigitalController controller) {
      this.controllerPos = pos;
      return true;
    }
    return false;
  }

  public boolean removeController(BlockPos pos, ServerLevel level) {
    if (controllerPos.equals(pos)) {
      if (level.getBlockState(pos).getBlock() instanceof DigitalController controller) {
        this.controllerPos = null;
        if (connections.isEmpty())
          return Networks.get(level).removeNetwork(frequency, level);
        return true;
      }
    }
    return false;
  }

  public boolean addConnection(BlockPos pos) {
    if (connections.stream().anyMatch(pos::equals)) return false;
    connections.add(pos);
    return true;
  }

  public boolean removeConnection(BlockPos pos) {
    if (connections.stream().noneMatch(pos::equals)) return false;
    connections.remove(pos);
    return true;
  }

  public void onChanged(Level level) {

  }

  public void passiveDrain(ServerLevel level) {
    DigitalControllerEntity entity;
    if (getControllerPos() == null) return;

    if (level.getBlockEntity(getControllerPos()) instanceof DigitalControllerEntity e) entity = e;
    else entity = null;

    if (entity == null) return;

    if (entity.getBlockState().getValue(DigitalController.ACTIVE)) {
      int toDrain = PassiveDrains.CONTROLLER + PassiveDrains.getTotalFromConnections(getConnections(), level);
      entity.getComponentManager().getComponent("energy").map(component -> (EnergyComponent) component).ifPresent(component -> {
        if (component.getCapacity() < toDrain) {
          component.setCapacity(toDrain + 100);
          component.setMaxInput(Integer.MAX_VALUE);
          component.setMaxOutput(Integer.MAX_VALUE);
        }
        int drained = component.extractRecipeEnergy(toDrain, true);
        if (drained == toDrain)
          component.extractRecipeEnergy(toDrain, false);
        else {
          BlockState inactive = entity.getBlockState().setValue(DigitalController.ACTIVE, false);
          level.setBlockAndUpdate(getControllerPos(), inactive);
        }
      });
    } else {
      int toDrain = PassiveDrains.CONTROLLER + PassiveDrains.getTotalFromConnections(getConnections(), level);
      entity.getComponentManager().getComponent("energy").map(component -> (EnergyComponent) component).ifPresent(component -> {
        if (component.getEnergy() >= toDrain) {
          BlockState active = entity.getBlockState().setValue(DigitalController.ACTIVE, true);
          level.setBlockAndUpdate(getControllerPos(), active);
          passiveDrain(level);
        }
      });
    }
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("frequency", frequency);
    JsonArray connectionsList = new JsonArray();
    for (BlockPos pos : connections) {
      connectionsList.add(pos.toString());
    }
    json.add("connections", connectionsList);
    json.addProperty("controllerPos", controllerPos.toString());
    return json;
  }
}
