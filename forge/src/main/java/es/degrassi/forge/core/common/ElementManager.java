package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.core.common.IType;
import es.degrassi.forge.core.common.element.EnergyElement;
import es.degrassi.forge.core.common.element.ExperienceElement;
import es.degrassi.forge.core.common.element.ItemElement;
import es.degrassi.forge.core.common.element.PlayerInventoryElement;
import es.degrassi.forge.core.common.element.ProgressElement;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.util.INBTSerializable;

public class ElementManager extends Manager<IElement<?>> implements INBTSerializable<CompoundTag> {
  public ElementManager(MachineEntity entity) {
    super(entity);
  }

  public ElementManager(List<IElement<?>> elements, MachineEntity entity) {
    super(elements, entity);
  }

  public ElementManager addEnergy(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction) {
    get().add(new EnergyElement(this, x, y, message, emptyTexture, filledTexture, id, direction));
    return this;
  }

  public ElementManager addEnergy(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id) {
    get().add(new EnergyElement(this, x, y, message, emptyTexture, filledTexture, id, ElementDirection.VERTICAL));
    return this;
  }

  public ElementManager addItem(int x, int y, Component message, ResourceLocation texture, String id) {
    get().add(new ItemElement(this, x, y, message, id, texture));
    return this;
  }

  public ElementManager addPlayerInventory(int x, int y, Component message, ResourceLocation texture) {
    get().add(new PlayerInventoryElement(this, x, y, texture, message));
    return this;
  }

  public ElementManager addExperience(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction) {
    get().add(new ExperienceElement(this, x, y, id, message, emptyTexture, filledTexture, direction));
    return this;
  }

  public ElementManager addExperience(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id) {
    return addExperience(x, y, message, emptyTexture, filledTexture, id, ElementDirection.HORIZONTAL);
  }

  public ElementManager addProgress(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture) {
    return addProgress(x, y, message, emptyTexture, filledTexture, ElementDirection.HORIZONTAL);
  }

  public ElementManager addProgress(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, ElementDirection direction) {
    get().add(new ProgressElement(this, x, y, message, emptyTexture, filledTexture, direction));
    return this;
  }

  public Optional<IElement<?>> getElement(String id) {
    return get().stream().filter(element -> element.getId().equals(id)).findFirst();
  }

  public void clientTick() {
    get().forEach(IElement::clientTick);
  }

  public void serverTick() {
    get().forEach(IElement::serverTick);
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    get().forEach(type -> type.serialize(nbt));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    get().forEach(type -> type.deserialize(nbt));
  }


  public void markDirty() {
    get().forEach(IElement::markDirty);
  }
  @Override
  public String toString() {
    return "Element" + super.toString();
  }
}
