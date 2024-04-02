package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.machines.container.ChestContainer;
import es.degrassi.forge.core.common.recipe.ChestRecipe;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Chest;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ChestEntity extends MachineEntity<ChestRecipe> implements LidBlockEntity {
  private Chest tier;
  private final ContainerOpenersCounter openersCounter;
  private final ChestLidController chestLidController;
  public ChestEntity(BlockPos pos, BlockState blockState, Chest tier) {
    super(EntityRegistration.CHEST.get(), pos, blockState);

    getElementManager().addPlayerInventory(
      tier.getInvX(),
      tier.getInvY(),
      Component.literal("player_inventory"),
      new DegrassiLocation("textures/gui/base_inventory.png")
    );

    int j;
    String id;
    for (int i = 0; i < tier.getRows(); i++) {
      for (j = 0; j < tier.getCols(); j++) {
        id = "item_" + i + "_" + j;
        getComponentManager().addItem(id);
        getElementManager().addItem(
          7 + j * 18,
          15 + i * 18,
          Component.literal(id),
          new DegrassiLocation("textures/gui/base_slot.png"),
          id
        );
      }
    }

    this.tier = tier;

    this.openersCounter = new ContainerOpenersCounter() {
      protected void onOpen(Level level, BlockPos pos, BlockState state) {
        playSound(level, pos, state, SoundEvents.CHEST_OPEN);
      }

      protected void onClose(Level level, BlockPos pos, BlockState state) {
        playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
      }

      protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
        ChestEntity.this.signalOpenCount(level, pos, state, count, openCount);
      }

      protected boolean isOwnContainer(@NotNull Player player) {
        if (player.containerMenu instanceof ChestContainer menu) {
          BlockEntity container = menu.getEntity();
          return container == ChestEntity.this;
        } else {
          return false;
        }
      }
    };
    this.chestLidController = new ChestLidController();
  }

  protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
    Block block = state.getBlock();
    level.blockEvent(pos, block, 1, eventParam);
  }

  static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
    double d = (double) pos.getX() + 0.5;
    double e = (double) pos.getY() + 0.5;
    double f = (double) pos.getZ() + 0.5;

    level.playSound(null, d, e, f, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("tier", tier.name().toLowerCase());
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    this.tier = Chest.value(tag.getString("tier"));
  }

  @Override
  public Component getName() {
    return getTier().getTranslation();
  }

  public Chest getTier() {
    return tier;
  }

  public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, ChestEntity blockEntity) {
    blockEntity.chestLidController.tickLid();
  }

  public void recheckOpen() {
    if (!this.remove) {
      this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  public boolean triggerEvent(int id, int type) {
    if (id == 1) {
      this.chestLidController.shouldBeOpen(type > 0);
      return true;
    } else {
      return super.triggerEvent(id, type);
    }
  }

  public void startOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  public void stopOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  public static int getOpenCount(BlockGetter level, BlockPos pos) {
    BlockState blockState = level.getBlockState(pos);
    if (blockState.hasBlockEntity()) {
      BlockEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity instanceof ChestEntity entity) {
        return entity.openersCounter.getOpenerCount();
      }
    }
    return 0;
  }

  @Override
  public float getOpenNess(float partialTicks) {
    return this.chestLidController.getOpenness(partialTicks);
  }
}
