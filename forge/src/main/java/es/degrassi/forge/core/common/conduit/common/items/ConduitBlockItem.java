package es.degrassi.forge.core.common.conduit.common.items;

import es.degrassi.common.conduit.IConduitType;
import es.degrassi.forge.core.common.conduit.common.blockentity.ConduitBlockEntity;
import es.degrassi.forge.core.common.conduit.common.init.EnderConduitTypes;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ConduitBlockItem extends BlockItem implements InfoBox.IInfoBoxHolder {
  private final Supplier<? extends IConduitType<?>> type;

  public ConduitBlockItem(Supplier<? extends IConduitType<?>> type, Block block, Properties properties) {
    super(block, properties);
    this.type = type;
  }

  public IConduitType<?> getType() {
    return type.get();
  }

  @Override
  public String getDescriptionId() {
    return getOrCreateDescriptionId();
  }

  @Override
  public InteractionResult place(BlockPlaceContext context) {
    Level level = context.getLevel();
    @Nullable
    Player player = context.getPlayer();
    BlockPos blockpos = context.getClickedPos();
    ItemStack itemstack = context.getItemInHand();

    // Handle placing into an existing block
    if (level.getBlockEntity(blockpos) instanceof ConduitBlockEntity conduit) {
      if (conduit.hasType(type.get())) {
        // Pass through to block
        return level.getBlockState(blockpos).use(level, player, context.getHand(), context.getHitResult());
      }

      conduit.addType(type.get(), player);
      if (level.isClientSide()) {
        conduit.updateClient();
      }

      BlockState blockState = level.getBlockState(blockpos);
      SoundType soundtype = blockState.getSoundType(level, blockpos, context.getPlayer());
      level.playSound(player, blockpos, this.getPlaceSound(blockState, level, blockpos, context.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
      level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockState));

      if (!player.getAbilities().instabuild) {
        itemstack.shrink(1);
      }
      return InteractionResult.sidedSuccess(level.isClientSide());
    }

    return super.place(context);
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    if (getType() == EnderConduitTypes.ENERGY.get()) {
      box.set(Component.translatable("info.degrassi.io.mode.all"),
        Component.translatable("info.degrassi.fe.per.tick", Integer.MAX_VALUE));
    } else if (getType() == EnderConduitTypes.ITEM.get()) {
      box.set(Component.translatable("info.degrassi.io.mode.all"),
        Component.translatable("info.degrassi.items.per.tick", 64));
    } else if (getType() == EnderConduitTypes.REDSTONE.get()) {
      box.set(Component.translatable("info.degrassi.redstone"),
        Component.translatable("info.degrassi.redstone"));
    } else if (getType() == EnderConduitTypes.FLUID.get()) {
      box.set(Component.translatable("info.degrassi.io.mode.all"),
        Component.translatable("info.degrassi.mb.per.tick", 100));
    } else if (getType() == EnderConduitTypes.FLUID2.get()) {
      box.set(Component.translatable("info.degrassi.io.mode.all"),
        Component.translatable("info.degrassi.mb.per.tick", 1_000));
    } else if (getType() == EnderConduitTypes.FLUID3.get()) {
      box.set(Component.translatable("info.degrassi.io.mode.all"),
        Component.translatable("info.degrassi.mb.per.tick", 10_000));
    }
    return box;
  }
}
