package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.common.registry.IVariant;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.multiblock.IMultiblockPart;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import es.degrassi.forge.core.common.recipe.BaseMultiblockPartRecipe;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public abstract class BaseMultiblockPartEntity<
  V extends Enum<V> & IVariant<V> & MultiblockPartStorage.MultiblockPartTieredSerialization<V>
> extends MachineEntity<BaseMultiblockPartRecipe> implements IMultiblockPart {

  private BaseMultiblockControllerBlock controllerBlock;
  private BaseMultiblockControllerEntity<?, ?, ?> controllerEntity;
  private V variant;

  public BaseMultiblockPartEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, V variant) {
    super(type, pos, blockState);
    this.variant = variant;
  }

  @Override
  public BlockPos getControllerPos() {
    if (controllerEntity == null) return null;
    return controllerEntity.getBlockPos();
  }

  @Override
  public Level getControllerLevel() {
    if (controllerEntity == null) return null;
    return controllerEntity.getLevel();
  }

  @Override
  public void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("tier", variant.serializeNBT());
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    variant = variant.deserializeNBT(tag.getString("tier"));
  }
}
