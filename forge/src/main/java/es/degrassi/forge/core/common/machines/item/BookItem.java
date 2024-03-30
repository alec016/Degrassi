package es.degrassi.forge.core.common.machines.item;

import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BookItem extends Item {

  public BookItem(Properties properties) {
    super(properties.rarity(Rarity.UNCOMMON));
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    return context.getPlayer() != null ? onItemUse(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getHand(),
      context.getClickedFace(), context.getClickLocation()) : super.useOn(context);
  }

  public InteractionResult onItemUse(Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, Vec3 hit) {
    return InteractionResult.PASS;
  }

  public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
    if (context.getPlayer() != null) {
      return onItemUseFirst(stack, context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getHand(),
        context.getClickedFace(), context.getClickLocation());
    } else {
      return InteractionResult.PASS;
    }
  }

  public InteractionResult onItemUseFirst(ItemStack stack, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, Vec3 hit) {
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack stack = playerIn.getItemInHand(handIn);
    if (worldIn.isClientSide) {
      ContainerRegistration.openManualScreen();
    }
    return InteractionResultHolder.success(stack);
  }
}
