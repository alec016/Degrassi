package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.InputBus;
import es.degrassi.forge.core.common.machines.multiblock.uils.handler.ItemSidedHandler;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class OutputBusEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Item.Output> {
  public static final OutputBusEntity DUMMY = new OutputBusEntity(BlockPos.ZERO, BlockRegistration.OUTPUT_BUS.get(MultiblockPartStorage.Item.Output.BASIC).defaultBlockState(), MultiblockPartStorage.Item.Output.BASIC) {
    @Override
    public boolean dummy() {
      return true;
    }
  };
  private final Map<Direction, LazyOptional<ItemSidedHandler>> itemWrapperHandlerMap;

  public OutputBusEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Item.Output variant) {
    super(EntityRegistration.OUTPUT_BUS.get(), pos, blockState, variant);

    String id;
    int i, j, k = 1;
    for (i = 0; i < variant.getRows(); i++)
      for (j = 0; j < variant.getCols(); j++) {
        id = "output_bus_" + k;
        getComponentManager().addItem(id, ComponentIOMode.OUTPUT);

        getElementManager()
          .addItem(
            7 + (j + 3) * 18,
            20 + i * 18,
            Component.literal(id),
            new DegrassiLocation("textures/gui/base_slot.png"),
            id
          );
        k++;
      }


    getElementManager().addPlayerInventory(
      7,
      30 + i * 18,
      Component.literal("player_inventory"),
      new DegrassiLocation("textures/gui/base_inventory.png")
    );

    itemHandler = getComponentManager().getItemHandler();
    lazyItemHandler = LazyOptional.of(() -> itemHandler);
    itemWrapperHandlerMap = Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        getBlockState().getValue(InputBus.FACING),
        null,
        Direction.UP
      )),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        getBlockState().getValue(InputBus.FACING),
        null,
        Direction.DOWN
      )),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        getBlockState().getValue(InputBus.FACING),
        null,
        Direction.NORTH
      )),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        getBlockState().getValue(InputBus.FACING),
        null,
        Direction.SOUTH
      )),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        getBlockState().getValue(InputBus.FACING),
        null,
        Direction.EAST
      )),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(
        getComponentManager(),
        itemHandler,
        getBlockState().getValue(InputBus.FACING),
        null,
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

  @Override
  public void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
  }
}
