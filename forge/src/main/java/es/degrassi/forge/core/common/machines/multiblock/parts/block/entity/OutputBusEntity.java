package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidInputTank;
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
  public static final OutputBusEntity DUMMY = dummyEntity();

  private final Map<Direction, LazyOptional<ItemSidedHandler>> itemWrapperHandlerMap;

  public OutputBusEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Item.Output variant) {
    super(EntityRegistration.OUTPUT_BUS.get(), pos, blockState, variant);

    String id;
    int i, j, k = 1;
    for (i = 0; i < variant.getRows(); i++)
      for (j = 0; j < variant.getCols(); j++) {
        id = "output_bus_" + k;
        componentManager.addItem(id);
        jeiComponentManager.addItem(id);

        elementManager
          .addItem(
            7 + (j + 3) * 18,
            20 + i * 18,
            Component.literal(id),
            new DegrassiLocation("textures/gui/base_slot.png"),
            id
          );
        jeiElementManager
          .addItem(
            7 + (j + 3) * 18,
            20 + i * 18,
            Component.literal(id),
            new DegrassiLocation("textures/gui/base_slot.png"),
            id
          );
        k++;
      }


    elementManager.addPlayerInventory(
      7,
      30 + i * 18,
      Component.literal("player_inventory"),
      new DegrassiLocation("textures/gui/base_inventory.png")
    );

    itemWrapperHandlerMap = ItemSidedHandler.DEFAULT_ALL_EXTRACT(componentManager, componentManager.getItemHandler(), getBlockState().getValue(FluidInputTank.FACING));
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      if (side == null) return lazyItemHandler.cast();
      if (side == this.getBlockState().getValue(InputBus.FACING)) {
        if (itemWrapperHandlerMap.containsKey(side))
          return itemWrapperHandlerMap.get(side).cast();
      }
      return LazyOptional.empty();
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

  public OutputBusEntity copy(boolean dummy) {
    return dummy ? dummyEntity() : new OutputBusEntity(getBlockPos(), getBlockState(), getVariant());
  }

  public static OutputBusEntity dummyEntity() {
    return new OutputBusEntity(BlockPos.ZERO, BlockRegistration.OUTPUT_BUS.get(MultiblockPartStorage.Item.Output.BASIC).defaultBlockState(), MultiblockPartStorage.Item.Output.BASIC) {
      @Override
      public boolean dummy() {
        return true;
      }
    };
  }
}
