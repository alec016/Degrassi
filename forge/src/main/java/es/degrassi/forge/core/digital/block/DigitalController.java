package es.degrassi.forge.core.digital.block;

import es.degrassi.common.utils.Utils;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DigitalController extends MachineBlock {
  public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
  public DigitalController() {
    super(Properties.of().destroyTime(500).sound(SoundType.GLASS).lightLevel((state) -> state.getValue(ACTIVE) ? 15 : 0).requiresCorrectToolForDrops());
  }
  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
    @NotNull Level level,
    @NotNull BlockState state,
    @NotNull BlockEntityType<T> type
  ) {
    return Utils.createTickerHelper(
      type, EntityRegistration.DIGITAL_CONTROLLER.get(),
      level.isClientSide()
        ? DigitalControllerEntity::clientTick
        : DigitalControllerEntity::serverTick
    );
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return EnvHandler.INSTANCE.createDigitalController(pos, state);
  }

  @Override
  @SuppressWarnings("deprecation")
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    final String frequency = "testing";
    if (level.getBlockEntity(pos) instanceof DigitalControllerEntity controller) {
      if (!level.isClientSide()) {
        if (!state.getValue(ACTIVE))
          controller.onNetworkSelected(frequency);
        else
          controller.onNetworkRemove(frequency);
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }

  @Override
  public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
    return super.getStateForPlacement(pContext).setValue(ACTIVE, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ACTIVE);
  }
}
