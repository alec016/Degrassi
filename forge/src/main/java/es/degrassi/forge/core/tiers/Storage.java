package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;
import java.util.Locale;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class Storage {
  public enum Energy implements IVariant<Energy>, S<Integer, Capability<IEnergyStorage>> {
    BASIC(50_000, 5_000),
    ADVANCED(250_000, 25_000),
    EXTREME(1_000_000, 100_000),
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int capacity, transfer;

    Energy(int capacity, int transfer) {
      this.capacity = capacity;
      this.transfer = transfer;
    }

    @Override
    public Energy[] getVariants() {
      return values();
    }

    public static Energy[] getNormalVariants() {
      return new Energy[] { BASIC, ADVANCED, EXTREME, CREATIVE };
    }

    @Override
    public Integer getCapacity() {
      return capacity;
    }

    @Override
    public Integer getTransfer() {
      return transfer;
    }

    @Override
    public Capability<IEnergyStorage> getCapability() {
      return ForgeCapabilities.ENERGY;
    }

    public boolean isCreative() {
      return this == CREATIVE;
    }

    @Override
    public String serializeNBT() {
      return nameL();
    }

    @Override
    public Energy deserializeNBT(String tier) {
      return switch(tier.toLowerCase(Locale.ROOT)) {
        case "advanced" -> ADVANCED;
        case "extreme" -> EXTREME;
        case "creative" -> CREATIVE;
        default -> BASIC;
      };
    }
  }

  public enum Fluid implements IVariant<Fluid>, S<Integer, Capability<IFluidHandler>> {
    BASIC(16_000, 1_000),
    ADVANCED(64_000, 5_000),
    EXTREME(512_000, 15_000),
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int capacity, transfer;

    Fluid(int capacity, int transfer) {
      this.capacity = capacity;
      this.transfer = transfer;
    }

    @Override
    public Fluid[] getVariants() {
      return values();
    }

    public static Fluid[] getNormalVariants() {
      return new Fluid[] { BASIC, ADVANCED, EXTREME, CREATIVE };
    }

    @Override
    public Integer getCapacity() {
      return capacity;
    }

    @Override
    public Integer getTransfer() {
      return transfer;
    }

    @Override
    public Capability<IFluidHandler> getCapability() {
      return ForgeCapabilities.FLUID_HANDLER;
    }

    public boolean isCreative() {
      return this == CREATIVE;
    }

    @Override
    public String serializeNBT() {
      return nameL();
    }

    @Override
    public Fluid deserializeNBT(String tier) {
      return switch(tier.toLowerCase(Locale.ROOT)) {
        case "advanced" -> ADVANCED;
        case "extreme" -> EXTREME;
        case "creative" -> CREATIVE;
        default -> BASIC;
      };
    }
  }

  public interface S<T, C> {
    T getCapacity();
    T getTransfer();
    C getCapability();
    boolean isCreative();
    String serializeNBT();
    S<T, C> deserializeNBT(String tier);
  }
}
