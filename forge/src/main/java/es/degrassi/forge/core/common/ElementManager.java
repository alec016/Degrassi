package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.element.BarElement;
import es.degrassi.forge.core.common.element.EnergyElement;
import es.degrassi.forge.core.common.element.ExperienceElement;
import es.degrassi.forge.core.common.element.FluidElement;
import es.degrassi.forge.core.common.element.HeatElement;
import es.degrassi.forge.core.common.element.ItemElement;
import es.degrassi.forge.core.common.element.PlayerInventoryElement;
import es.degrassi.forge.core.common.element.ProgressElement;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import es.degrassi.forge.core.common.wrapper.DegrassiItemStackHandler;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unused")
public final class ElementManager extends Manager<IElement<?>> implements INBTSerializable<ListTag> {
  public ElementManager(MachineEntity<?> entity) {
    super(entity);
  }

  public ElementManager(List<IElement<?>> elements, MachineEntity<?> entity) {
    this(entity);
    elements.forEach(this::add);
  }

  public ElementManager addEnergy(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction) {
    addEnergy(x, y, message, emptyTexture, filledTexture, id, direction, true);
    return this;
  }

  public ElementManager addEnergy(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction, boolean jei) {
    get().add(new EnergyElement(this, x, y, message, emptyTexture, filledTexture, id, direction, jei));
    return this;
  }

  public ElementManager addEnergy(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id) {
    addEnergy(x, y, message, emptyTexture, filledTexture, id, ElementDirection.TOP);
    return this;
  }

  public ElementManager addEnergy(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, boolean jei) {
    addEnergy(x, y, message, emptyTexture, filledTexture, id, ElementDirection.TOP, jei);
    return this;
  }

  public ElementManager addItem(int x, int y, Component message, ResourceLocation texture, String id) {
    addItem(x, y, message, texture, id, true);
    return this;
  }

  public ElementManager addItem(int x, int y, Component message, ResourceLocation texture, String id, boolean jei) {
    get().add(new ItemElement(this, x, y, message, id, texture, jei));
    return this;
  }

  public ElementManager addPlayerInventory(int x, int y, Component message, ResourceLocation texture) {
    get().add(new PlayerInventoryElement(this, x, y, texture, message));
    return this;
  }

  public ElementManager addExperience(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction) {
    addExperience( x, y, message, emptyTexture, filledTexture, id, direction, true);
    return this;
  }

  public ElementManager addExperience(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id) {
    return addExperience(x, y, message, emptyTexture, filledTexture, id, ElementDirection.RIGHT, true);
  }

  public ElementManager addExperience(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction, boolean jei) {
    get().add(new ExperienceElement(this, x, y, id, message, emptyTexture, filledTexture, direction, jei));
    return this;
  }

  public ElementManager addExperience(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, boolean jei) {
    return addExperience(x, y, message, emptyTexture, filledTexture, id, ElementDirection.RIGHT, jei);
  }

  public ElementManager addProgress(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture) {
    return addProgress(x, y, message, emptyTexture, filledTexture, ElementDirection.RIGHT, true);
  }

  public ElementManager addProgress(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, ElementDirection direction) {
    addProgress(x, y, message, emptyTexture, filledTexture, direction, true);
    return this;
  }

  public ElementManager addProgress(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, boolean jei) {
    return addProgress(x, y, message, emptyTexture, filledTexture, ElementDirection.RIGHT, jei);
  }

  public ElementManager addProgress(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, ElementDirection direction, boolean jei) {
    get().add(new ProgressElement(this, x, y, message, emptyTexture, filledTexture, direction, jei));
    return this;
  }

  public ElementManager addFluid(int x, int y, Component message, ResourceLocation texture, String id) {
    addFluid(x, y, message, texture, id, true);
    return this;
  }

  public ElementManager addFluid(int x, int y, Component message, ResourceLocation texture, String id, boolean jei) {
    get().add(new FluidElement(this, x, y, texture, message, id, jei));
    return this;
  }

  public ElementManager addBar(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id) {
    return addBar(x, y, message, emptyTexture, filledTexture, id, ElementDirection.RIGHT);
  }

  public ElementManager addBar(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction) {
    addBar(x, y, message, emptyTexture, filledTexture, id, direction, true);
    return this;
  }

  public ElementManager addBar(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, boolean jei) {
    return addBar(x, y, message, emptyTexture, filledTexture, id, ElementDirection.RIGHT, jei);
  }

  public ElementManager addBar(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction, boolean jei) {
    get().add(new BarElement(this, x, y, emptyTexture, filledTexture, message, id, direction, jei));
    return this;
  }

  public ElementManager addHeat(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id) {
    return addHeat(x, y, message, emptyTexture, filledTexture, id, ElementDirection.RIGHT);
  }

  public ElementManager addHeat(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, boolean jei) {
    return addHeat(x, y, message, emptyTexture, filledTexture, id, ElementDirection.RIGHT, jei);
  }

  public ElementManager addHeat(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction) {
    return addHeat(x, y, message, emptyTexture, filledTexture, id, direction, true);
  }

  public ElementManager addHeat(int x, int y, Component message, ResourceLocation emptyTexture, ResourceLocation filledTexture, String id, ElementDirection direction, boolean jei) {
    get().add(new HeatElement(this, x, y, emptyTexture, filledTexture, message, id, direction, jei));
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
  public ListTag serializeNBT() {
    ListTag nbt = new ListTag();
    get().forEach(element -> nbt.add(element.serialize()));
    return nbt;
  }

  @Override
  public void deserializeNBT(ListTag nbt) {
    nbt.forEach(tag -> {
      if (tag instanceof CompoundTag compound)
        get().forEach(element -> {
          if (compound.contains("id", Tag.TAG_STRING) && element.getId().equals(compound.getString("id")))
            element.deserialize(compound);
        });
    });
  }

  public void markDirty() {
    get().forEach(IElement::markDirty);
  }

  public ElementManager mergeWith(ElementManager other, boolean copy, MachineEntity<?> entity) {
    ElementManager newManager = this;
    other.get().forEach(element -> newManager.add(copy ? element.copy(newManager) : element));
    return newManager;
  }

  public ElementManager copy(MachineEntity<?> entity, boolean copy) {
    List<IElement<?>> elements = this.get().stream().map(element -> copy ? element.copy(this) : element).toList();
    return new ElementManager(elements, entity);
  }
}
