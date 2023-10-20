package es.degrassi.forge.integration.jei;

import es.degrassi.Degrassi;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SolarPanelRecipeCategory extends PanelCategory {
  public static final ResourceLocation UID = new ResourceLocation(Degrassi.MODID, "solar_panel");

  public SolarPanelRecipeCategory(@NotNull IGuiHelper helper) {
    super(helper);
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.literal("Solar Panel");
  }
}
