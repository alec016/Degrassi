package es.degrassi.forge.integration.jade.client.component.providers;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.entity.FurnaceEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;

public class FurnaceComponentProvider implements IBlockComponentProvider {

  public static final FurnaceComponentProvider INSTANCE = new FurnaceComponentProvider();
  public static final ResourceLocation ID = new DegrassiLocation("furnace_component_provider");
  @Override
  public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
    if (blockAccessor.getBlockEntity() instanceof FurnaceEntity) {
      CompoundTag nbt = blockAccessor.getServerData().getCompound(Degrassi.MODID);
      if (nbt.isEmpty()) return;
      if (nbt.contains("furnace.xp")) {
        float xp = nbt.getFloat("furnace.xp");
        iTooltip.add(
          Component.literal(
            "XP: " + xp
          ).withStyle(ChatFormatting.DARK_GREEN)
        );
      }
      if (nbt.contains("furnace.progress")) {
        CompoundTag tag = nbt.getCompound("furnace.progress");
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
