package es.degrassi.forge.integration.jade.client.component.providers;

import es.degrassi.common.*;
import es.degrassi.forge.*;
import es.degrassi.forge.init.entity.generators.*;
import net.minecraft.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import snownee.jade.api.*;
import snownee.jade.api.config.*;
import snownee.jade.api.ui.*;

public class GeneratorComponentProvider implements IBlockComponentProvider {

  public static final GeneratorComponentProvider INSTANCE = new GeneratorComponentProvider();
  public static final ResourceLocation ID = new DegrassiLocation("generator_component_provider");
  @Override
  public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
    if (blockAccessor.getBlockEntity() instanceof GeneratorEntity<?,?,?>) {
      CompoundTag nbt = blockAccessor.getServerData().getCompound(Degrassi.MODID);
      if (nbt.isEmpty()) return;
      if (nbt.contains("generator.progress")) {
        CompoundTag tag = nbt.getCompound("generator.progress");
        int currentProgress = tag.getInt("progress");
        int maxProgress = tag.getInt("maxProgress");

        float progress = currentProgress / (maxProgress * 1f);
        Component component = Component.literal(currentProgress + " / " + maxProgress);
        if (currentProgress > 0) {
          iTooltip.add(Component.translatable(
            "degrassi.machine.running"
          ).withStyle(ChatFormatting.GREEN));
        } else {
          iTooltip.add(Component.translatable(
            "degrassi.machine.idle"
          ).withStyle(ChatFormatting.GOLD));
        }
        iTooltip.add(iTooltip.getElementHelper().progress(
          progress,
          component,
          iTooltip.getElementHelper().progressStyle(),
          BoxStyle.DEFAULT,
          true
        ));
      }
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }
}
