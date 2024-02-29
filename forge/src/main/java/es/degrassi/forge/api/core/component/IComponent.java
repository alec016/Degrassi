package es.degrassi.forge.api.core.component;

import es.degrassi.forge.api.core.machine.MachineStatus;
import net.minecraft.network.chat.Component;

public interface IComponent {

  ComponentType<?> getType();

  ComponentIOMode getMode();

  IComponentManager getManager();

  default void init() {}
  default void onRemoved() {}

  default void onStatusChanged(MachineStatus oldStatus, MachineStatus newStatus, Component message) {}

  IComponentTemplate<? extends IComponent> template();
}
