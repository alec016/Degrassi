package es.degrassi.forge.integration.jade;

import es.degrassi.forge.init.block.FurnaceBlock;
import es.degrassi.forge.init.block.Melter;
import es.degrassi.forge.init.block.UpgradeMaker;
import es.degrassi.forge.init.block.generators.*;
import es.degrassi.forge.init.entity.FurnaceEntity;
import es.degrassi.forge.init.entity.MelterEntity;
import es.degrassi.forge.init.entity.UpgradeMakerEntity;
import es.degrassi.forge.init.entity.generators.*;
import es.degrassi.forge.init.registration.BlockRegister;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class DegrassiWailaPlugin implements IWailaPlugin {

  @Override
  public void register(@NotNull IWailaCommonRegistration registration) {
    registration.registerBlockDataProvider(DegrassiServerDataProvider.FURNACE, FurnaceEntity.class);
    registration.registerBlockDataProvider(DegrassiServerDataProvider.MELTER, MelterEntity.class);
    registration.registerBlockDataProvider(DegrassiServerDataProvider.UPGRADE_MAKER, UpgradeMakerEntity.class);
    registration.registerBlockDataProvider(DegrassiServerDataProvider.GENERATOR, JewelryGeneratorEntity.class);
    registration.registerBlockDataProvider(DegrassiServerDataProvider.GENERATOR, CombustionGeneratorEntity.class);
  }

  @Override
  public void registerClient(@NotNull IWailaClientRegistration registration) {
    registration.registerBlockComponent(DegrassiComponentProvider.FURNACE, FurnaceBlock.class);
    registration.usePickedResult(BlockRegister.IRON_FURNACE_BLOCK.get());
    registration.usePickedResult(BlockRegister.GOLD_FURNACE_BLOCK.get());
    registration.usePickedResult(BlockRegister.DIAMOND_FURNACE_BLOCK.get());
    registration.usePickedResult(BlockRegister.EMERALD_FURNACE_BLOCK.get());
    registration.usePickedResult(BlockRegister.NETHERITE_FURNACE_BLOCK.get());
    registration.registerBlockComponent(DegrassiComponentProvider.MELTER, Melter.class);
    registration.usePickedResult(BlockRegister.MELTER_BLOCK.get());
    registration.registerBlockComponent(DegrassiComponentProvider.UPGRADE_MAKER, UpgradeMaker.class);
    registration.usePickedResult(BlockRegister.UPGRADE_MAKER.get());
    registration.registerBlockComponent(DegrassiComponentProvider.GENERATOR, JewelryGenerator.class);
    registration.usePickedResult(BlockRegister.JEWELRY_GENERATOR.get());
    registration.registerBlockComponent(DegrassiComponentProvider.GENERATOR, CombustionGenerator.class);
    registration.usePickedResult(BlockRegister.COMBUSTION_GENERATOR.get());
  }
}
