package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity;

import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.multiblock.IMultiblockController;
import es.degrassi.forge.core.common.machines.multiblock.IMultiblockPart;
import es.degrassi.forge.core.common.machines.multiblock.uils.Multiblocks;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.recipe.BaseMultiblockControllerRecipe;
import es.degrassi.forge.core.common.machines.multiblock.uils.StateMatcher;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class BaseMultiblockControllerEntity<
  R extends BaseMultiblockControllerRecipe<R>,
  B extends BaseMultiblockControllerBlock,
  E extends BaseMultiblockControllerEntity<R, B, E>
> extends MachineEntity<R> implements IMultiblockController<B, E> {

  private static final int validateTime = 5;
  private final Map<BlockPos, StateMatcher> pattern = new HashMap<>();
  private boolean valid = false;
  private int cacheValidate = 0;

  public BaseMultiblockControllerEntity(BlockEntityType<? extends BaseMultiblockControllerEntity<R, B, E>> type, BlockPos pos, BlockState blockState, BaseMultiblockControllerBlock block) {
    super(type, pos, blockState);
    Multiblocks.addController(this, pos);

    getPattern().forEach((position, stateMatcher) -> stateMatcher.getPossibleStates().forEach(state -> {
      if (state.getBlock() instanceof IMultiblockPart part){
        part.setControllerBlock(block);
        part.setControllerEntity(this);
      }
    }));
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    Multiblocks.removeController(getBlockPos());
  }

  @Override
  public Component getName() {
    return getBlockState().getBlock().getName();
  }

  @Override
  public BaseMultiblockControllerEntity<R, B, E> addToPattern(BlockPos pos, StateMatcher block) {
    if (pattern.containsKey(pos)) throw new IllegalStateException("Current position already exists in pattern");
    pattern.put(pos, block);
    return this;
  }

  @Override
  public boolean validate(BlockPos pos, StateMatcher matcher) {
    return matcher.matches(Objects.requireNonNull(getLevel()).getBlockState(pos));
  }

  @Override
  public void validate() {
    boolean isValid = pattern.entrySet().stream().allMatch(entry -> this.validate(entry.getKey(), entry.getValue()));
    this.level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BaseMultiblockControllerBlock.VALID, isValid));
    requestModelDataUpdate();
    setChanged();
  }

  public static <
    R extends BaseMultiblockControllerRecipe<R>,
    B extends BaseMultiblockControllerBlock,
    E extends BaseMultiblockControllerEntity<R, B, E>
  > void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull BaseMultiblockControllerEntity<R, B, E> entity
  ) {
    if (entity.cacheValidate <= 0) {
      entity.validate();
      entity.cacheValidate = validateTime;
      return;
    }
    entity.cacheValidate--;
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    valid = tag.getBoolean("valid");
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("valid", valid);
  }
}
