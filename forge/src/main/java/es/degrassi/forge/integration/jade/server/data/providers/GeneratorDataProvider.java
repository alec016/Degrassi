package es.degrassi.forge.integration.jade.server.data.providers;

import es.degrassi.common.*;
import es.degrassi.forge.*;
import es.degrassi.forge.init.entity.*;
import es.degrassi.forge.init.entity.generators.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import org.jetbrains.annotations.*;
import snownee.jade.api.*;

public class GeneratorDataProvider implements IServerDataProvider<BlockEntity> {

  public static final GeneratorDataProvider INSTANCE = new GeneratorDataProvider();
  public static final ResourceLocation ID = new DegrassiLocation("generator_server_data_provider");
  @Override
  public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, @NotNull BlockEntity entity, boolean b) {
    if (entity instanceof GeneratorEntity<?, ?> generatorEntity) {
      CompoundTag tag = new CompoundTag();
      generatorEntity.saveAdditional(tag);
      compoundTag.put(Degrassi.MODID, tag);
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }
}