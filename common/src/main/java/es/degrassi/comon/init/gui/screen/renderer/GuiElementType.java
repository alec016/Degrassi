package es.degrassi.comon.init.gui.screen.renderer;

import es.degrassi.Degrassi;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GuiElementType<T extends IGuiElement> {
  public static final ResourceKey<Registry<GuiElementType<? extends IGuiElement>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Degrassi.MODID, "gui_element_type"));
  @Contract(value = " -> new", pure = true)
  public static <T extends IGuiElement> @NotNull GuiElementType<T> create() {
    return new GuiElementType<>();
  }
  private GuiElementType() {
  }
  public ResourceLocation getId() {
    return Degrassi.guiElementRegistrar().getId(this);
  }
}
