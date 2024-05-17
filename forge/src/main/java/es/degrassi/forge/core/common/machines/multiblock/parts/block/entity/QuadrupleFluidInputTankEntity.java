package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidInputTank;
import es.degrassi.forge.core.common.machines.multiblock.uils.handler.FluidSidedHandler;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class QuadrupleFluidInputTankEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Fluid.QuadrupleInput> {
  public static final QuadrupleFluidInputTankEntity DUMMY = dummyEntity();

  private final DegrassiFluidHandler fluid;
  private final Map<Direction, LazyOptional<FluidSidedHandler>> fluidWrapperHandlerMap;

  public QuadrupleFluidInputTankEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Fluid.QuadrupleInput variant) {
    super(EntityRegistration.QUADRUPLE_FLUID_INPUT_TANK.get(), pos, blockState, variant);

    int i;
    String id;
    ResourceLocation fluidLocation = new DegrassiLocation("textures/gui/base_fluid_storage.png");
    for (i = 0; i < variant.getTankNumber(); i++) {
      id = "fluid_input_" + i;
      componentManager.addFluid(variant.getCapacity(), id);
      jeiComponentManager.addFluid(variant.getCapacity(), id);
      elementManager.addFluid(
        7 + (i + 3) * 18,
        20,
        Component.literal(id),
        fluidLocation,
        id
      );
      jeiElementManager.addFluid(
        7 + (i + 3) * 18,
        20,
        Component.literal(id),
        fluidLocation,
        id
      );
    }

    elementManager.addPlayerInventory(
      7,
      20 + TextureSizeHelper.getTextureHeight(fluidLocation) + 5,
      Component.literal("player_inventory"),
      new DegrassiLocation("textures/gui/base_inventory.png")
    );

    fluid = componentManager.getFluidHandler();
    fluidWrapperHandlerMap = FluidSidedHandler.DEFAULT_ALL_INSERT(componentManager, fluid, getBlockState().getValue(FluidInputTank.FACING));
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      if (side == null) return lazyFluidHandler.cast();
      if (fluidWrapperHandlerMap.containsKey(side)) {
        Direction localDir = this.getBlockState().getValue(FluidInputTank.FACING);
        if (side == localDir)
          return fluidWrapperHandlerMap.get(localDir).cast();
        return LazyOptional.empty();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public Component getName() {
    return Component.translatable(getBlockState().getBlock().getDescriptionId());
  }

  public QuadrupleFluidInputTankEntity copy(boolean dummy) {
    return dummy ? dummyEntity() : new QuadrupleFluidInputTankEntity(getBlockPos(), getBlockState(), getVariant());
  }

  public static QuadrupleFluidInputTankEntity dummyEntity() {
    return new QuadrupleFluidInputTankEntity(BlockPos.ZERO, BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.get(MultiblockPartStorage.Fluid.QuadrupleInput.BASIC).defaultBlockState(), MultiblockPartStorage.Fluid.QuadrupleInput.BASIC) {
      @Override
      public boolean dummy() {
        return true;
      }
    };
  }
}
