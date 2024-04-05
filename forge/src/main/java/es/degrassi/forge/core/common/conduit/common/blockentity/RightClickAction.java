package es.degrassi.forge.core.common.conduit.common.blockentity;

import es.degrassi.common.conduit.ConduitTypes;
import es.degrassi.common.conduit.IConduitType;

public sealed interface RightClickAction permits RightClickAction.Upgrade, RightClickAction.Blocked, RightClickAction.Insert{
     record Upgrade(IConduitType<?> notInConduit) implements RightClickAction {
        public IConduitType<?> getNotInConduit() {
            return notInConduit;
        }

         @Override
         public String toString() {
             return "Upgrade[" + ConduitTypes.getRegistry().getKey(notInConduit) + "]";
         }
     }

    final class Insert implements RightClickAction {
        @Override
        public String toString() {
            return "Insert";
        }
    }

    final class Blocked implements RightClickAction {
        @Override
        public String toString() {
            return "Blocked";
        }
    }

    default boolean hasChanged() {
        return !(this instanceof Blocked);
    }
}
