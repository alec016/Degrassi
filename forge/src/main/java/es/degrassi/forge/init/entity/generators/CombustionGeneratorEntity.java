package es.degrassi.forge.init.entity.generators;

import es.degrassi.forge.init.block.generators.*;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.recipe.helpers.*;
import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.init.registration.*;
import es.degrassi.forge.integration.config.*;
import es.degrassi.forge.network.*;
import java.util.concurrent.atomic.*;
import net.minecraft.core.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraftforge.items.*;
import org.jetbrains.annotations.*;

@SuppressWarnings("unchecked")
public class CombustionGeneratorEntity extends GeneratorEntity<CombustionGeneratorEntity, CombustionGenerator> {
  {
    ENERGY_STORAGE = new EnergyComponent(getComponentManager(), DegrassiConfig.get().generatorsConfig.combustion_capacity, DegrassiConfig.get().generatorsConfig.combustion_transfer) {
      @Override
      public boolean canReceive() {
        return false;
      }

      @Override
      public void onChanged() {
        super.onChanged();
        if (level != null && !level.isClientSide())
          new EnergyPacket(this.energy, this.capacity, this.maxReceive, getBlockPos())
            .sendToChunkListeners(level.getChunkAt(getBlockPos()));
      }
    };

    itemHandler = new ItemStackHandler(3) {
      @Override
      protected void onContentsChanged(int slot) {
        getComponentManager().markDirty();
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

  public CombustionGeneratorEntity(
    BlockPos blockPos,
    BlockState blockState,
    CombustionGenerator block
  ) {
    super(EntityRegister.COMBUSTION_GENERATOR.get(), blockPos, blockState, block);
    this.data = new ContainerData() {
      @Override
      public int get(int index) {
        return switch (index) {
          case 0 -> CombustionGeneratorEntity.this.progressComponent.getProgress();
          case 1 -> CombustionGeneratorEntity.this.progressComponent.getMaxProgress();
          default -> 0;
        };
      }

      @Override
      public void set(int index, int value) {
        switch (index) {
          case 0 -> CombustionGeneratorEntity.this.progressComponent.setProgress(value);
          case 1 -> CombustionGeneratorEntity.this.progressComponent.setMaxProgress(value);
        }
      }

      @Override
      public int getCount() {
        return 2;
      }
    };
  }

  public static void tick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull CombustionGeneratorEntity entity
  ) {
    if (level.isClientSide()) return;

    if (RecipeHelpers.GENERATORS.hasRecipe(entity)) {
      if(!entity.getRecipe().isInProgress() && entity.getProgressStorage().getProgress() == 0) {
        entity.getRecipe().startProcess(entity);
      } else {
        entity.getRecipe().tick(entity);
      }
      if (entity.getProgressStorage().getProgress() >= entity.getProgressStorage().getMaxProgress()) {
        entity.getRecipe().endProcess(entity);
      }
    }
    entity.getComponentManager().markDirty();
  }
}
