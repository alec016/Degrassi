package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.processor.FurnaceProcessor;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Furnace;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@Getter
public class FurnaceEntity extends MachineEntity<FurnaceRecipe> {
  private Furnace tier;
  public FurnaceEntity(BlockPos pos, BlockState blockState, Furnace tier) {
    super(EntityRegistration.FURNACE.get(), pos, blockState);

    this.getComponentManager()
      .addEnergy(tier.getEnergyCapacity(), "energy", ComponentIOMode.INPUT)
      .addItem("upgrade1", true, ComponentIOMode.INPUT)
      .addItem("upgrade2", true, ComponentIOMode.INPUT)
      .addItem("input", ComponentIOMode.INPUT)
      .addItem("output", ComponentIOMode.OUTPUT)
      .addExperience(tier.getExperienceCapacity(), "experience");

    this.getElementManager()
      .addEnergy(
        7,
        72,
        Component.literal("energy"),
        new DegrassiLocation("textures/gui/furnace_energy_empty.png"),
        new DegrassiLocation("textures/gui/furnace_energy_storage_filled.png"),
        "energy",
        ElementDirection.RIGHT
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
      ).addExperience(
        66,
        57,
        Component.literal("experience"),
        new DegrassiLocation("textures/gui/base_experience_empty.png"),
        new DegrassiLocation("textures/gui/base_experience_filled.png"),
        "experience"
      ).addProgress(
        66,
        33,
        Component.literal("progress"),
        new DegrassiLocation("textures/gui/furnace_progress_empty.png"),
        new DegrassiLocation("textures/gui/furnace_progress_filled.png")
      );
    this.tier = tier;

    this.processor = new FurnaceProcessor(this);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("tier", tier.nameL());
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    this.tier = Furnace.value(tag.getString("tier"));
  }

  @Override
  public Component getName() {
    return getTier().getTranslation();
  }
}