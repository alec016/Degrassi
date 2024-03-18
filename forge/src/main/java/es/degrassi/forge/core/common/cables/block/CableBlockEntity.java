package es.degrassi.forge.core.common.cables.block;

import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.recipe.CableRecipe;
import es.degrassi.forge.core.tiers.CableTier;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public abstract class CableBlockEntity extends MachineEntity<CableRecipe> {

  private CableTier tier;
  private CableType cableType;
  public CableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, CableTier tier, CableType cableType) {
    super(type, pos, blockState);
    this.tier = tier;
    this.cableType = cableType;
    switch (cableType) {
      case ENERGY ->
        getComponentManager().addEnergy(tier.energy().getCapacity(), tier.energy().getTransfer(), "energy");
      case ITEM ->
        getComponentManager().addItem("item");
//      case FLUID ->
//        getComponentManager().addFluid(tier.fluid().getCapacity(), "fluid");
    }
  }

  public CableTier getTier() {
    return tier;
  }

  public void setTier(CableTier tier) {
    this.tier = tier;
    getBlockState().setValue(CableBlock.TIER, tier);
    setChanged();
  }

  private Set<BlockPos> outputs = null;

  private void checkOutputs() {
    if (outputs == null) {
      outputs = new HashSet<>();
      traverse(worldPosition, cable -> {
        for (Direction direction : Direction.values()) {
          BlockPos p = cable.getBlockPos().relative(direction);
          BlockEntity te = level.getBlockEntity(p);
          if (te != null && !(te instanceof CableBlockEntity)) {
            te.getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
              if (handler.canReceive()) outputs.add(p);
            });
          }
        }
      });
    }
  }

  public void markDirty() {
    traverse(worldPosition, cable -> cable.outputs = null);
  }

  private void traverse (BlockPos pos, Consumer<CableBlockEntity> consumer) {
    Set<BlockPos> traversed = new HashSet<>();
    traversed.add(pos);
    consumer.accept(this);
    traverse(pos, traversed, consumer);
  }

  private void traverse (BlockPos pos, Set<BlockPos> traversed, Consumer<CableBlockEntity> consumer) {
    for (Direction direction : Direction.values()) {
      BlockPos p = pos.relative(direction);
      if (!traversed.contains(p)){
        if (getLevel().getBlockEntity(p) instanceof  CableBlockEntity cable && getLevel().getBlockEntity(pos) instanceof  CableBlockEntity entity) {
          if (cable.getCableType() == entity.getCableType() && cable.getTier() == entity.getTier()) {
            traversed.add(p);
            consumer.accept(cable);
            cable.traverse(p, traversed, consumer);
          }
        }
      }
    }
  }

  public void serverTick() {
    getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).ifPresent(energy -> {
      if (energy.getEnergyStored() > 0) {
        checkOutputs();
        if (!outputs.isEmpty()) {
          int amount = energy.getEnergyStored() / outputs.size();
          for (BlockPos p : outputs) {
            BlockEntity te = level.getBlockEntity(p);
            if (te != null) {
              te.getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
                if (handler.canReceive()) {
                  int received = handler.receiveEnergy(amount, false);
                  energy.extractEnergy(received, false);
                }
              });
            }
          }
        }
      }
    });
  }

  public CableType getCableType() {
    return cableType;
  }
  public void setCableType(CableType type) {
    this.cableType = type;
    getBlockState().setValue(CableBlock.TYPE, cableType);
    getComponentManager().clear();
    switch (cableType) {
      case ENERGY ->
        getComponentManager().addEnergy(tier.energy().getCapacity(), tier.energy().getTransfer(), "energy");
      case ITEM ->
        getComponentManager().addItem("item");
//      case FLUID ->
//        getComponentManager().addFluid(tier.fluid().getCapacity(), "fluid");
    }
    setChanged();
  }

  @Override
  public Component getName() {
    return Component.literal("Cable");
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.putString("tier", getTier().getSerializedName());
    tag.putString("type", getCableType().getSerializedName());
    return tag;
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("tier", getTier().getSerializedName());
    tag.putString("type", getCableType().getSerializedName());
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    setTier(CableTier.of(tag.getString("tier")));
    setCableType(CableType.of(tag.getString("type")));
  }
}
