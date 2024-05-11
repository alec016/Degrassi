package es.degrassi.forge.core.common.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CapUtils {
  public static <T, U extends T>ICapabilityProvider makeProvider(Capability<T> cap, U instance) {
    LazyOptional<T> lazyInstanceButNotReal = LazyOptional.of(() -> instance);
    return new CapProvider<>(cap, lazyInstanceButNotReal);
  }

  private CapUtils() {}

  private static class CapProvider<T> implements ICapabilityProvider {
    protected final Capability<T> cap;
    protected final LazyOptional<T> lazyInstanceButNotReally;

    public CapProvider(Capability<T> cap, LazyOptional<T> lazyInstanceButNotReally) {
      this.cap = cap;
      this.lazyInstanceButNotReally = lazyInstanceButNotReally;
    }

    @Override
    public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> capability, @Nullable Direction arg) {
      return cap.orEmpty(capability, lazyInstanceButNotReally);
    }
  }
}
