package es.degrassi.forge.integration.jade.server.data.providers;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.entity.MelterEntity;
import es.degrassi.forge.init.entity.UpgradeMakerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.IServerDataProvider;

public class UpgradeMakerDataProvider implements IServerDataProvider<BlockEntity> {
  public static final UpgradeMakerDataProvider INSTANCE = new UpgradeMakerDataProvider();
  public static final ResourceLocation ID = new DegrassiLocation("upgrade_maker_server_data_provider");
  @Override
  public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, @NotNull BlockEntity entity, boolean b) {
    if (entity instanceof UpgradeMakerEntity upgradeMakerEntity) {
      CompoundTag tag = new CompoundTag();
      upgradeMakerEntity.saveAdditional(tag);
      compoundTag.put(Degrassi.MODID, tag);
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }
}
