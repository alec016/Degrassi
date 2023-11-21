package es.degrassi.forge.init.entity.generators;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.type.*;
import es.degrassi.forge.init.entity.type.IEnergyEntity.IGenerationEntity;
import es.degrassi.forge.init.handlers.ItemWrapperHandler;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.generators.JewelryGeneratorRecipe;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.GenerationPacket;
import es.degrassi.forge.network.ItemPacket;
import es.degrassi.forge.network.ProgressPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.GenerationStorage;
import es.degrassi.forge.util.storage.ProgressStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class JewelryGeneratorEntity extends BaseEntity implements IEnergyEntity, IRecipeEntity, IProgressEntity, IItemEntity, IGenerationEntity {
  private JewelryGeneratorRecipe recipe;
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<AbstractEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
  private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
  private final AbstractEnergyStorage ENERGY_STORAGE = new AbstractEnergyStorage(DegrassiConfig.get().generatorsConfig.jewelry_capacity, DegrassiConfig.get().generatorsConfig.jewelry_transfer) {
    @Override
    public boolean canExtract() {
      return false;
    }

    @Override
    public void onEnergyChanged() {
      setChanged();
      if (level != null && level.getServer() != null && !level.isClientSide())
        new EnergyPacket(this.energy, this.capacity, this.maxReceive, getBlockPos())
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
    }
  };
  private final ProgressStorage progressStorage = new ProgressStorage(0) {
    @Override
    public void onProgressChanged() {
      setChanged();
      if (level != null && !level.isClientSide())
        new ProgressPacket(this.progress, this.maxProgress, getBlockPos())
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
    }
  };
  private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
      if (level != null && !level.isClientSide() && level.getServer() != null) {
        new ItemPacket(this, worldPosition)
          .sendToAll(level.getServer());
      }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
      return switch(slot) {
        case 0 -> stack.is(ItemRegister.SPEED_UPGRADE.get());
        case 1 -> stack.is(ItemRegister.ENERGY_UPGRADE.get());
        case 2 -> {
          AtomicBoolean has = new AtomicBoolean(false);
          RecipeHelpers.UPGRADE_MAKER.recipes.forEach(recipe -> {
            if (recipe.getIngredients().get(0).getItems()[0].is(stack.getItem())) has.set(true);
          });
          yield has.get();
        }
        case 3 -> {
          AtomicBoolean has = new AtomicBoolean(false);
          RecipeHelpers.UPGRADE_MAKER.recipes.forEach(recipe -> {
            if (recipe.getIngredients().get(1).getItems()[0].is(stack.getItem())) has.set(true);
          });
          yield has.get();
        }
        default -> false;
      };
    }
  };
  private final GenerationStorage currentGen = new GenerationStorage() {
    @Override
    public void onGenerationChanged() {
      setChanged();
      if (level != null && !level.isClientSide()) {
        new GenerationPacket(this.getGeneration(), worldPosition)
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
      }
    }
  };;

  private final Map<Direction, LazyOptional<ItemWrapperHandler>> itemWrapperHandlerMap = Map.of(
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

  public JewelryGeneratorEntity(BlockPos blockPos, BlockState blockState) {
    super(EntityRegister.JEWELRY_GENERATOR.get(), blockPos, blockState);
  }
  public Component getName() {
    return Component.translatable(
      "block.degrassi.jewelry_generator"
    );
  }

  public void drops() {
    SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      inventory.setItem(i, itemHandler.getStackInSlot(i));
    }

    assert this.level != null;
    Containers.dropContents(this.level, this.worldPosition, inventory);
  }

  public static void tick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull JewelryGeneratorEntity entity
  ) {
    if (level.isClientSide()) {
      return;
    }
//    if (RecipeHelpers.UPGRADE_MAKER.hasRecipe(entity)) {
//      entity.progressStorage.increment(false);
//      RecipeHelpers.UPGRADE_MAKER.extractEnergy(entity);
//      if (entity.progressStorage.getProgress() >= entity.progressStorage.getMaxProgress()) RecipeHelpers.UPGRADE_MAKER.craftItem(entity);
//    } else {
//      entity.resetProgress();
//    }
    setChanged(level, pos, state);
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
  public IDegrassiRecipe getRecipe() {
    return recipe;
  }

  @Override
  public void setRecipe(IDegrassiRecipe recipe) {
    this.recipe = (JewelryGeneratorRecipe) recipe;
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
}
