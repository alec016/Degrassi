package es.degrassi.forge.init.entity.generators;

import es.degrassi.forge.init.block.generators.*;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.ItemPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unchecked")
public class JewelryGeneratorEntity extends GeneratorEntity<JewelryGeneratorEntity, JewelryGenerator> {
  {
    ENERGY_STORAGE = new AbstractEnergyStorage(DegrassiConfig.get().generatorsConfig.jewelry_capacity, DegrassiConfig.get().generatorsConfig.jewelry_transfer) {
      @Override
      public boolean canReceive() {
        return false;
      }

      @Override
      public void onEnergyChanged() {
        setChanged();
        if (level != null && !level.isClientSide())
          new EnergyPacket(this.energy, this.capacity, this.maxReceive, getBlockPos())
            .sendToChunkListeners(level.getChunkAt(getBlockPos()));
      }
    };

    itemHandler = new ItemStackHandler(3) {
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
          case 0 -> stack.is(ItemRegister.GENERATION_UPGRADE.get());
          case 1 -> stack.is(ItemRegister.TRANSFER_UPGRADE.get());
          case 2 -> {
            AtomicBoolean has = new AtomicBoolean(false);
            RecipeHelpers.GENERATORS.getRecipesForMachine(getDelegate()).forEach(recipe -> {
              GeneratorRecipe r = (GeneratorRecipe) recipe;
              if (r.getIngredients().get(0).getItems()[0].is(stack.getItem())) has.set(true);
            });
            yield has.get();
          }
          default -> false;
        };
      }
    };
  }

  public final ContainerData data;

  public JewelryGeneratorEntity(
    BlockPos blockPos,
    BlockState blockState,
    JewelryGenerator block
  ) {
    super(EntityRegister.JEWELRY_GENERATOR.get(), blockPos, blockState, block);
    this.data = new ContainerData() {
      @Override
      public int get(int index) {
        return switch (index) {
          case 0 -> JewelryGeneratorEntity.this.progressStorage.getProgress();
          case 1 -> JewelryGeneratorEntity.this.progressStorage.getMaxProgress();
          default -> 0;
        };
      }

      @Override
      public void set(int index, int value) {
        switch (index) {
          case 0 -> JewelryGeneratorEntity.this.progressStorage.setProgress(value);
          case 1 -> JewelryGeneratorEntity.this.progressStorage.setMaxProgress(value);
        }
      }

      @Override
      public int getCount() {
        return 2;
      }
    };
  }
  public Component getName() {
    return Component.translatable(
      "block.degrassi.jewelry_generator"
    );
  }

  public static void tick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull JewelryGeneratorEntity entity
  ) {
    if (level.isClientSide()) return;

    if (RecipeHelpers.GENERATORS.hasRecipe(entity)) {
      entity.progressStorage.increment(false);
      RecipeHelpers.GENERATORS.insertEnergy(entity);
      if (entity.progressStorage.getProgress() >= entity.progressStorage.getMaxProgress()) RecipeHelpers.GENERATORS.craftItem(entity);
    } else {
      entity.resetProgress();
    }
    setChanged(level, pos, state);
  }
}
