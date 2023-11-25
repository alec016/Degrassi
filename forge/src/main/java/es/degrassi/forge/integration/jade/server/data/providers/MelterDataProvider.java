package es.degrassi.forge.integration.jade.server.data.providers;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.entity.MelterEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.IServerDataProvider;

public class MelterDataProvider implements IServerDataProvider<BlockEntity> {

  public static final MelterDataProvider INSTANCE = new MelterDataProvider();
  public static final ResourceLocation ID = new DegrassiLocation("melter_server_data_provider");
  @Override
  public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, @NotNull BlockEntity entity, boolean b) {
    if (entity instanceof MelterEntity melterEntity) {
      CompoundTag tag = new CompoundTag();
      melterEntity.saveAdditional(tag);
      compoundTag.put(Degrassi.MODID, tag);
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }
}
