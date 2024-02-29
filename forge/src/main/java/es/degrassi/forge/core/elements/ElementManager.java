package es.degrassi.forge.core.elements;

import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.element.IElementManager;
import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.api.core.machine.MachineTile;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class ElementManager implements IElementManager {
  @Override
  public Map<ElementType<?>, IGuiElement> getMap() {
    return null;
  }

  @Override
  public MachineTile getTile() {
    return null;
  }

  @Override
  public Level getLevel() {
    return null;
  }

  @Override
  public MinecraftServer getServer() {
    return null;
  }

  @Override
  public void markDirty() {

  }
}
