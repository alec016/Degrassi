package es.degrassi.forge.api.core.network;

import java.util.function.Consumer;

public interface ISyncableStuff {
  void getStuffToSync(Consumer<ISyncable<?, ?>> container);
}
