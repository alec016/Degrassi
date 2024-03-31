package es.degrassi.forge.core.common.cables;

import es.degrassi.forge.core.common.cables.energy.EnergySideConfig;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.recipe.CableRecipe;
import es.degrassi.forge.core.tiers.CableTier;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CableEntity<T extends CableNet<?>, S extends SideConfig<?>> extends MachineEntity<CableRecipe> {
  protected S sideConfig;
  private static final String NBT_SIDES = "cs";
  public final EnumSet<Direction> sides = EnumSet.noneOf(Direction.class);
  @Nullable
  public T net = null;
  public MutableBoolean netInsertionGuard = new MutableBoolean(false);
  protected int startIndex = 0;
  protected Redstone redstone = Redstone.IGNORE;
  protected CableTier tier;
  private int syncTicks;
  public int ticks;
  public CableEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, CableTier tier) {
    super(type, pos, blockState);
    this.tier = tier;
  }

  public boolean isActive() {
    if (getLevel() instanceof ServerLevel serverLevel) {
      return serverLevel.getChunkSource().isPositionTicking(ChunkPos.asLong(getBlockPos()));
    }
    return false;
  }

  public void readSync(CompoundTag nbt) {
    this.sideConfig.read(nbt);
    if (!this.tier.isEmpty() && nbt.contains("variant", 3)) {
      this.tier = this.tier.read(nbt, "variant");
    }
    this.redstone = Redstone.values()[nbt.getInt("redstone_mode")];
  }

  public CompoundTag writeSync(CompoundTag nbt) {
    this.sideConfig.write(nbt);
    if (!this.tier.isEmpty()) {
      this.tier.write(nbt, this.tier, "variant");
    }
    nbt.putInt("redstone_mode", this.redstone.ordinal());
    return nbt;
  }


  protected void readSides(CompoundTag compound) {
    // Read connected sides
    this.sides.clear();
    var sideBits = compound.getByte(NBT_SIDES);
    for (var side : Direction.values()) {
      if ((sideBits & getSideMask(side)) != 0) {
        this.sides.add(side);
      }
    }
  }

  protected void writeSides(CompoundTag compound) {
    // Write connected sides
    byte sideBits = 0;
    for (var side : this.sides) {
      sideBits |= getSideMask(side);
    }
    compound.putByte(NBT_SIDES, sideBits);
  }

  public Redstone getRedstoneMode() {
    return this.redstone;
  }

  public void setRedstoneMode(Redstone mode) {
    this.redstone = mode;
  }

  public boolean checkRedstone() {
    boolean power = this.level != null && this.level.getBestNeighborSignal(this.worldPosition) > 0;
    return Redstone.IGNORE.equals(getRedstoneMode()) || power && Redstone.ON.equals(getRedstoneMode())
      || !power && Redstone.OFF.equals(getRedstoneMode());
  }

  public void sync() {
    if (this.level instanceof ServerLevel) {
      final BlockState state = getBlockState();
      getComponentManager().serverTick();
      serverTick(level);
      this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
      setChanged();
    }
  }

  public boolean isRemote() {
    return this.level != null && this.level.isClientSide;
  }

  public void tick() {
    final Level world = this.level;
    if (world != null) {
      if (this.ticks == 0) {
        onFirstTick(world);
      }
      if (doPostTicks(world)) {
        int i = postTick(world);
        if (i > -1 && !isRemote()) {
          sync(i);
        }
      }
      this.ticks++;
      if (!isRemote()) {
        if (this.syncTicks > -1)
          this.syncTicks--;
        if (this.syncTicks == 0)
          sync();
        serverTick(world);
      } else {
        clientTick(world);
      }
    }
  }

  protected void onFirstTick(Level world) {
  }

  protected boolean doPostTicks(Level world) {
    return true;
  }

  protected int postTick(Level world) {
    return -1;
  }

  protected void clientTick(Level world) {
  }

  protected void serverTick(Level world) {
  }

  public void sync(int delay) {
    if (!isRemote()) {
      if (this.syncTicks <= 0 || delay < this.syncTicks) {
        this.syncTicks = delay;
      }
    }
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    writeSync(tag);
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    readSync(tag);
  }

  public Transfer getTransferType() {
    return Transfer.ALL;
  }

  public void onAdded(Level world, BlockState state, BlockState oldState, boolean isMoving) {

  }

  public void onRemoved(Level world, BlockState state, BlockState newState, boolean isMoving) {

  }

  public void onPlaced(Level world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

  }

  protected static byte getSideMask(Direction side) {
    return (byte) (1 << side.ordinal());
  }


  public abstract SideConfig<?> getSideConfig();
}
