package es.degrassi.forge.api.core.machine;

import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum MachineStatus implements StringRepresentable {
  IDLE, RUNNING, ERRORED, PAUSED;

  public static MachineStatus value (String string) {
    return valueOf(string.toUpperCase(Locale.ENGLISH));
  }

  public String toString () {
    return super.toString().toUpperCase(Locale.ENGLISH);
  }

  @Override
  public @NotNull String getSerializedName() {
    return toString();
  }

  public MutableComponent getTranslatedName() {
    return Component.translatable("degrassi.craftingstatus." + this);
  }
}
