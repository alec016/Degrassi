package es.degrassi.forge.init.entity.generators;

import es.degrassi.forge.init.block.generators.JewelryGenerator;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.generators.JewelryGeneratorRecipe;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.ItemPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class JewelryGeneratorEntity extends GeneratorEntity<JewelryGeneratorEntity, JewelryGeneratorRecipe, JewelryGenerator> {
  {
    ENERGY_STORAGE = new AbstractEnergyStorage(DegrassiConfig.get().generatorsConfig.jewelry_capacity, DegrassiConfig.get().generatorsConfig.jewelry_transfer) {
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

    itemHandler = new ItemStackHandler(5) {
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
  }

  public JewelryGeneratorEntity(BlockPos blockPos, BlockState blockState, JewelryGenerator block) {
    super(EntityRegister.JEWELRY_GENERATOR.get(), blockPos, blockState, block);
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
}
