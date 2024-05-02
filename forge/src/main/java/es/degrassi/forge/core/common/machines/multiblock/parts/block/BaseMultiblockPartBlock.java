package es.degrassi.forge.core.common.machines.multiblock.parts.block;

import es.degrassi.common.registry.IBlock;
import es.degrassi.common.registry.IVariant;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class BaseMultiblockPartBlock<
  V extends Enum<V> & IVariant<V>,
  B extends Block & IBlock<V, B>
> extends MachineBlock implements IBlock<V, B> {
  private final V variant;

  public BaseMultiblockPartBlock(Properties properties, V variant) {
    super(properties);
    this.variant = variant;
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return null;
  }
}
