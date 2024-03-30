package es.degrassi.forge;

import es.degrassi.forge.core.common.cables.energy.EnergyCableEntity;
import es.degrassi.forge.core.common.cables.IBlock;
import es.degrassi.forge.core.common.cables.fluid.FluidCableEntity;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.SolarPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class EnvHandler {
  public static final EnvHandler INSTANCE = new EnvHandler();
  private final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

  private EnvHandler() {

  }

  public boolean hasEnergy(Level level, BlockPos pos, Direction side) {
    var be = level.getBlockEntity(pos);
    return be != null && be.getCapability(ForgeCapabilities.ENERGY, side).isPresent();
  }

  public boolean hasFluid(Level level, BlockPos pos, Direction side) {
    var be = level.getBlockEntity(pos);
    return be != null && be.getCapability(ForgeCapabilities.FLUID_HANDLER, side).isPresent();
  }


  public EnergyCableEntity createEnergyCable(BlockPos pos, BlockState state, CableTier cableTier) {
    return new EnergyCableEntity(pos, state, cableTier);
  }

  public FluidCableEntity createFluidCable(BlockPos pos, BlockState state, CableTier cableTier) {
    return new FluidCableEntity(pos, state, cableTier);
  }

  public SolarPanelEntity createSP(BlockPos pos, BlockState state, SolarPanel solarPanel) {
    return new SolarPanelEntity(pos, state, solarPanel);
  }

  public void setupBlockItems() {
    modEventBus.addListener((RegisterEvent event) -> {
      if (event.getRegistryKey() == Registries.ITEM) {
        var registry = event.getForgeRegistry();
        for (var block : ForgeRegistries.BLOCKS.getValues()) {
          if (block instanceof IBlock<?, ?> iBlock) {
            var blockItem = iBlock.getBlockItem(new Item.Properties());
            var name = BuiltInRegistries.BLOCK.getKey(block);
            registry.register(name, blockItem);
          }
        }
      }
    });
  }
}
