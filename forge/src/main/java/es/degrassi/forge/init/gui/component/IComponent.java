package es.degrassi.forge.init.gui.component;

import es.degrassi.forge.init.gui.component.ComponentManager;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unused")
public interface IComponent extends INBTSerializable<Tag> {
  void deserializeNBT(Tag tag);
  Tag serializeNBT();

  default void init() {}

  default void onRemoved() {}

  void onChanged();

  ComponentManager getManager();


}
