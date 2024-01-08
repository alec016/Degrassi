package es.degrassi.forge.init.entity.generators;

import es.degrassi.forge.init.block.FurnaceBlock;
import es.degrassi.forge.init.block.generators.GeneratorBlock;
import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity;
import es.degrassi.forge.init.entity.type.IItemEntity;
import es.degrassi.forge.init.entity.type.IProgressEntity;
import es.degrassi.forge.init.entity.type.IRecipeEntity;
import es.degrassi.forge.init.handlers.ItemWrapperHandler;
import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.network.GenerationPacket;
import es.degrassi.forge.network.ProgressPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.GenerationStorage;
import es.degrassi.forge.util.storage.ProgressStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class GeneratorEntity<T extends GeneratorEntity<T, B>, B extends GeneratorBlock> extends BaseEntity implements IEnergyEntity, IRecipeEntity<GeneratorRecipe>, IProgressEntity, IItemEntity, IEnergyEntity.IGenerationEntity {

  // recipe
  private GeneratorRecipe recipe;

  // handlers
  protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  protected LazyOptional<AbstractEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
  protected LazyOptional<ProgressStorage> lazyProgressHandler = LazyOptional.empty();

  // storages
  protected AbstractEnergyStorage ENERGY_STORAGE;
  protected final ProgressStorage progressStorage = new ProgressStorage(0) {
    @Override
    public void onProgressChanged() {
      setChanged();
      if (level != null && !level.isClientSide())
        new ProgressPacket(this.progress, this.maxProgress, getBlockPos())
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
    }
  };
  protected ItemStackHandler itemHandler;
  protected final GenerationStorage currentGen = new GenerationStorage() {
    @Override
    public void onGenerationChanged() {
      setChanged();
      if (level != null && !level.isClientSide()) {
        new GenerationPacket(this.getGeneration(), worldPosition)
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
      }
    }
  };

  protected final B delegate;

  protected final Map<Direction, LazyOptional<ItemWrapperHandler>> itemWrapperHandlerMap = Map.of(
    Direction.UP, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.DOWN, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.NORTH, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.SOUTH, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.EAST, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.WEST, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    ))
  );

  public GeneratorEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, B delegate) {
    super(blockEntityType, blockPos, blockState);
    this.delegate = delegate;
  }

  public void drops() {
    SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      inventory.setItem(i, itemHandler.getStackInSlot(i));
    }

    assert this.level != null;
    Containers.dropContents(this.level, this.worldPosition, inventory);
  }

  @Override
  public AbstractEnergyStorage getEnergyStorage() {
    return ENERGY_STORAGE;
  }

  @Override
  public void setEnergyLevel(int energy) {
    this.ENERGY_STORAGE.setEnergy(energy);
  }

  @Override
  public void setCapacityLevel(int capacity) {
    this.ENERGY_STORAGE.setCapacity(capacity);
  }

  @Override
  public void setTransferRate(int transfer) {
    this.ENERGY_STORAGE.setTransfer(transfer);
  }


  @Override
  public GeneratorRecipe getRecipe() {
    return recipe;
  }

  @Override
  public void setRecipe(GeneratorRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public ProgressStorage getProgressStorage() {
    return progressStorage;
  }

  @Override
  public void setProgress(int progress) {
    this.progressStorage.setProgress(progress);
  }

  @Override
  public void setMaxProgress(int maxProgress) {
    this.progressStorage.setMaxProgress(maxProgress);
  }

  @Override
  public void resetProgress() {
    this.progressStorage.resetProgress();
    this.recipe = null;
    setChanged();
  }

  public Component getName() {
    return getDelegate().getName();
  }

  @Override
  public void setHandler(@NotNull ItemStackHandler handler) {
    for (int i = 0; i < handler.getSlots(); i++) {
      itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
    }
    setChanged();
  }
  public ItemStackHandler getItemHandler() {
    return itemHandler;
  }

  @Override
  public GenerationStorage getGenerationStorage() {
    return currentGen;
  }


  @Override
  public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      if (side == null) return lazyItemHandler.cast();
      if (itemWrapperHandlerMap.containsKey(side)) {
        Direction localDir = this.getBlockState().getValue(FurnaceBlock.FACING);
        if (side == Direction.UP || side == Direction.DOWN) return itemWrapperHandlerMap.get(side).cast();

        return switch(localDir) {
          default -> itemWrapperHandlerMap.get(side.getOpposite()).cast();
          case EAST -> itemWrapperHandlerMap.get(side.getClockWise()).cast();
          case SOUTH -> itemWrapperHandlerMap.get(side).cast();
          case WEST -> itemWrapperHandlerMap.get(side.getCounterClockWise()).cast();
        };
      }
    }

    return super.getCapability(cap, side);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    lazyItemHandler = LazyOptional.of(() -> itemHandler);
    lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    lazyProgressHandler = LazyOptional.of(() -> progressStorage);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyItemHandler.invalidate();
    lazyEnergyHandler.invalidate();
    lazyProgressHandler.invalidate();
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    nbt.put("generator.inventory", itemHandler.serializeNBT());
    nbt.put("generator.energy", ENERGY_STORAGE.serializeNBT());
    nbt.put("generator.progress", progressStorage.serializeNBT());
    return nbt;
  }

  @Override
  public void saveAdditional(@NotNull CompoundTag nbt) {
    super.saveAdditional(nbt);
    nbt.put("generator.inventory", itemHandler.serializeNBT());
    nbt.put("generator.energy", ENERGY_STORAGE.serializeNBT());
    nbt.put("generator.progress", progressStorage.serializeNBT());
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("generator.inventory"));
    ENERGY_STORAGE.deserializeNBT(nbt.getCompound("generator.energy"));
    progressStorage.deserializeNBT(nbt.getCompound("generator.progress"));
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public B getDelegate() {
    return delegate;
  }
}
