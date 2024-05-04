package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidInputTank;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.InputBus;
import es.degrassi.forge.core.common.machines.multiblock.uils.handler.FluidSidedHandler;
import es.degrassi.forge.core.common.machines.multiblock.uils.handler.ItemSidedHandler;
import es.degrassi.forge.core.common.wrapper.DegrassiItemStackHandler;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class InputBusEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Item.Input> {
  private final Map<Direction, LazyOptional<ItemSidedHandler>> itemWrapperHandlerMap;

  public InputBusEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Item.Input variant) {
    super(EntityRegistration.INPUT_BUS.get(), pos, blockState, variant);

    for (int i = 0; i < variant.getRows(); i++)
      for (int j = 0; j < variant.getCols(); j++){
        getComponentManager().addItem("input_bus_" + i + "_" + j, ComponentIOMode.INPUT);
      }
    itemHandler = new DegrassiItemStackHandler(this);
    lazyItemHandler = LazyOptional.of(() -> itemHandler);
    itemWrapperHandlerMap = Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        null,
        getBlockState().getValue(InputBus.FACING),
        Direction.UP
      )),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        null,
        getBlockState().getValue(InputBus.FACING),
        Direction.DOWN
      )),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        null,
        getBlockState().getValue(InputBus.FACING),
        Direction.NORTH
      )),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        null,
        getBlockState().getValue(InputBus.FACING),
        Direction.SOUTH
      )),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        null,
        getBlockState().getValue(InputBus.FACING),
        Direction.EAST
      )),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        null,
        getBlockState().getValue(InputBus.FACING),
        Direction.WEST
      ))
    );
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      if (side == null) return lazyItemHandler.cast();
      if (itemWrapperHandlerMap.containsKey(side)) {
        Direction localDir = this.getBlockState().getValue(InputBus.FACING);
        if (side == localDir)
          return itemWrapperHandlerMap.get(localDir).cast();
        return LazyOptional.empty();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public Component getName() {
    return Component.translatable(getBlockState().getBlock().getDescriptionId());
  }
}
