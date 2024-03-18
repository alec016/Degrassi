package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.common.recipe.SolarPanelRecipe;
import es.degrassi.forge.core.tiers.SolarPanel;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

public class SolarPanelEntity extends MachineEntity<SolarPanelRecipe> {

  public static final ModelProperty<Level> WORLD_PROP = new ModelProperty<>();
  public static final ModelProperty<BlockPos> POS_PROP = new ModelProperty<>();
  private SolarPanel tier;
  int voxelTimer = 0;
  VoxelShape shape;
  public SolarPanelEntity(BlockPos pos, BlockState blockState, SolarPanel tier) {
    super(tier.getType().get(), pos, blockState);

    this.getComponentManager()
      .addEnergy(tier.getEnergyCapacity(), "energy");

    this.getElementManager()
      .addEnergy(
        7,
        72,
        Component.literal("energy"),
        new DegrassiLocation("textures/gui/furnace_energy_empty.png"),
        new DegrassiLocation("textures/gui/panel_energy_storage_filled.png"),
        "energy",
        ElementDirection.VERTICAL
      ).addPlayerInventory(
        7,
        97,
        Component.literal("player_inventory"),
        new DegrassiLocation("textures/gui/base_inventory.png")
      );

    this.tier = tier;
  }

  @Override
  public @NotNull ModelData getModelData() {
    return ModelData.builder()
      .with(WORLD_PROP, level)
      .with(POS_PROP, worldPosition)
      .build();
  }

  public SolarPanel getTier() {
    return tier;
  }

  public void setTier(SolarPanel tier) {
    this.tier = tier;
  }
  public void setTier(String tier) {
    this.tier = SolarPanel.value(tier);
  }

  public static void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull SolarPanelEntity entity
  ) {
    if (entity.voxelTimer > 0)
      --entity.voxelTimer;
    entity.getComponentManager().serverTick();
    entity.getElementManager().serverTick();

    level.updateNeighbourForOutputSignal(pos, state.getBlock());
    setChanged(level, pos, state);
  }

  @Override
  public Component getName() {
    return getTier().getName();
  }

  public VoxelShape getShape(SolarPanelBlock block) {
    if (shape == null || voxelTimer <= 0) {
      shape = block.recalcShape(Objects.requireNonNull(getLevel()), worldPosition);
      voxelTimer = 20;
    }
    return shape;
  }

  public void resetVoxelShape() {
    shape = null;
  }
}
