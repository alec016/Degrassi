package es.degrassi.forge.api.core.machine;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface IMachine {
  Component getName();
  ResourceLocation getId();
  List<ResourceLocation> getRecipeIds();
  boolean isDummy();

  // IProcessorTemplate<?> getProcessorTemplate();
}
