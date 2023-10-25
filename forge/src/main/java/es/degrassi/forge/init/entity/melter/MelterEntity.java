package es.degrassi.forge.init.entity.melter;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.IEnergyEntity;
import es.degrassi.forge.init.entity.IProgressEntity;
import es.degrassi.forge.init.entity.IRecipeEntity;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.ItemPacket;
import es.degrassi.forge.network.ProgressPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.ProgressStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class MelterEntity extends BaseEntity implements IEnergyEntity, IRecipeEntity, IProgressEntity {
  private MelterRecipe recipe;
  private final AbstractEnergyStorage ENERGY_STORAGE = new AbstractEnergyStorage(DegrassiConfig.melter_capacity.get(), DegrassiConfig.melter_transfer.get()) {
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
  };;
  public MelterEntity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.MELTER.get(),
      pos,
      state
    );
  }
  private final ProgressStorage progressStorage = new ProgressStorage(0) {
    @Override
    public void onProgressChanged() {
      setChanged();
      if (level != null && !level.isClientSide())
        new ProgressPacket(this.progress, this.maxProgress, getBlockPos())
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
    }
  };

  private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
      if (level != null && !level.isClientSide() && level.getServer() != null) {
        new ItemPacket(this, worldPosition)
          .sendToAll(level.getServer());
        //.sendToChunkListeners(level.getChunkAt(pos));
      }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
      return switch(slot) {
        case 0 -> stack.is(ItemRegister.SPEED_UPGRADE.get());
        case 1 -> stack.is(ItemRegister.ENERGY_UPGRADE.get());
        case 2 -> true;
        case 3 -> false;
        default -> isItemValid(slot, stack);
      };
    }
  };

  public Component getName() {
    return Component.translatable(
      "block.degrassi.melter"
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
    @NotNull MelterEntity furnaceEntity
  ) {
    if (level.isClientSide()) return;
    if (RecipeHelpers.MELTER.hasRecipe(furnaceEntity)) {
//      furnaceEntity.progressStorage.increment(false);
//      RecipeHelpers.MELTER.extractEnergy(furnaceEntity);
//      if (furnaceEntity.progressStorage.getProgress() >= furnaceEntity.progressStorage.getMaxProgress()) RecipeHelpers.MELTER.craftItem(furnaceEntity);
    } else {
//      furnaceEntity.resetProgress();
    }
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
}
