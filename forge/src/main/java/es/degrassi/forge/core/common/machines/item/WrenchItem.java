package es.degrassi.forge.core.common.machines.item;

import es.degrassi.forge.core.common.conduit.common.blockentity.ConduitBlockEntity;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.common.machines.item.wrench.IWrench;
import es.degrassi.forge.core.common.machines.item.wrench.IWrenchable;
import es.degrassi.forge.core.common.machines.item.wrench.WrenchMode;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused, deprecation")
public class WrenchItem extends Item implements IItem, IWrench, IHudItem {
  private static final Direction[] DIRECTIONS = Direction.values();
  public WrenchItem() {
    super(new Properties().stacksTo(1));
  }

  @Override
  public @NotNull InteractionResult useOn(UseOnContext context) {
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
    BlockEntity te = world.getBlockEntity(pos);
    BlockState state = world.getBlockState(pos);
    if (player.isShiftKeyDown() && !(te instanceof ConduitBlockEntity conduit))
      return InteractionResult.SUCCESS;
    if (state.getBlock() instanceof IWrenchable iWrenchable
      && iWrenchable.onWrench(state, world, pos, player, hand, side, getWrenchMode(stack), hit)) {
      return InteractionResult.SUCCESS;
    } else if (te instanceof IWrenchable iWrenchable
      && iWrenchable.onWrench(state, world, pos, player, hand, side, getWrenchMode(stack), hit)) {
      return InteractionResult.SUCCESS;
    } else {
      if (getWrenchMode(stack).rotate()
        // Only rotate Degrassi machines
        && (state.getBlock() instanceof MachineBlock)) {
        final BlockState rotatedState = rotateState(world, state, pos);
        if (!state.equals(rotatedState)) {
          world.setBlockAndUpdate(pos, rotatedState);
          world.playSound(player, pos, rotatedState.getBlock().getSoundType(rotatedState).getPlaceSound(), SoundSource.BLOCKS, 1F, 1F);
          return InteractionResult.sidedSuccess(world.isClientSide);
        }
      }
    }
    return InteractionResult.SUCCESS;
  }

  private BlockState rotateState(Level world, BlockState state, BlockPos pos) {
    for (Property<?> property : state.getProperties()) {
      if (property.getName().equals("facing") && property instanceof DirectionProperty dirProp) {
        final Direction current = state.getValue(dirProp);
        Direction rotated = nextDirection(current);

        // if the rotation isn't valid, try the next rotation
        while (!property.getPossibleValues().contains(rotated) || !state.setValue(dirProp, rotated).canSurvive(world, pos)) {
          rotated = nextDirection(rotated);
          // give up if we went all the way around
          if (rotated == current) {
            return state;
          }
        }
        return state.setValue(dirProp, rotated);
      }
    }
    return state;
  }

  private static Direction nextDirection(Direction dir) {
    return DIRECTIONS[(dir.ordinal() + 1) % DIRECTIONS.length];
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
    ItemStack stack = playerIn.getItemInHand(handIn);
    if (playerIn.isShiftKeyDown()) {
      nextWrenchMode(stack);
      playerIn.displayClientMessage(
        Component.translatable("info.degrassi.wrench.mode",
          Component.translatable("info.degrassi.wrench.mode." + getWrenchMode(stack).name().toLowerCase())
            .withStyle(ChatFormatting.YELLOW)),
        true);
      return InteractionResultHolder.success(stack);
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
    tooltip.add(Component.translatable("info.degrassi.wrench.mode",
      Component.translatable("info.degrassi.wrench.mode." + getWrenchMode(stack).name().toLowerCase())
        .withStyle(ChatFormatting.YELLOW)));
  }

  @Override
  public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof Player player) {
      oneTimeInfo(player, stack,
        Component.translatable("info.degrassi.wrench.mode",
          Component.translatable("info.degrassi.wrench.mode." + getWrenchMode(stack).name().toLowerCase())
            .withStyle(ChatFormatting.YELLOW)));
    }
  }

  @Override
  public boolean renderHud(Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, Vec3 hit) {
    return false;
  }

  private boolean changeWrenchMode(ItemStack stack, boolean next) {
    if (stack.getItem() instanceof IWrench)
      if (next) {
        nextWrenchMode(stack);
        return true;
      } else {
        prevWrenchMode(stack);
        return true;
      }
    return false;
  }

  private void nextWrenchMode(ItemStack stack) {
    CompoundTag nbt = getWrenchNBT(stack);
    int i = nbt.getInt("WrenchMode") + 1;
    int j = WrenchMode.values().length - 1;
    nbt.putInt("WrenchMode", i > j ? 0 : i);
  }

  private void prevWrenchMode(ItemStack stack) {
    CompoundTag nbt = getWrenchNBT(stack);
    int i = nbt.getInt("WrenchMode") - 1;
    int j = WrenchMode.values().length - 1;
    nbt.putInt("WrenchMode", Math.max(i, j));
  }

  @Override
  public WrenchMode getWrenchMode(ItemStack stack) {
    return WrenchMode.values()[getWrenchNBT(stack).getInt("WrenchMode")];
  }

  public CompoundTag getWrenchNBT(ItemStack stack) {
    return stack.getOrCreateTagElement("DegrassiWrenchNBT");
  }
}
