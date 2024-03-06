package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.core.tiers.Furnace;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceEntity extends MachineEntity {
  private final Furnace tier;
  public FurnaceEntity(BlockPos pos, BlockState blockState, Furnace tier) {
    super(tier.getType().get(), pos, blockState);
    this.getComponentManager()
      .addEnergy(tier.getCapacity(), "energy")
      .addItem("upgrade1")
      .addItem("upgrade2")
      .addItem("input")
      .addItem("output");
    this.getElementManager()
      .addEnergy(
        7,
        72,
        Component.literal("energy"),
        new DegrassiLocation("textures/gui/furnace_energy_empty.png"),
        new DegrassiLocation("textures/gui/furnace_energy_storage_filled.png"),
        "energy",
        ElementDirection.HORIZONTAL
      ).addItem(
        7,
        24,
        Component.literal("item"),
        new DegrassiLocation("textures/gui/base_slot.png"),
        "upgrade1"
      ).addItem(
        7,
        42,
        Component.literal("item"),
        new DegrassiLocation("textures/gui/base_slot.png"),
        "upgrade2"
      ).addItem(
        43,
        33,
        Component.literal("item"),
        new DegrassiLocation("textures/gui/base_slot.png"),
        "input"
      ).addItem(
        133,
        33,
        Component.literal("item"),
        new DegrassiLocation("textures/gui/base_slot.png"),
        "output"
      ).addPlayerInventory(
        7,
        97,
        Component.literal("player_inventory"),
        new DegrassiLocation("textures/gui/base_inventory.png")
      );
    this.tier = tier;
  }

  public Furnace getTier() {
    return tier;
  }

  @Override
  public Component getName() {
    return getTier().getName();
  }
}