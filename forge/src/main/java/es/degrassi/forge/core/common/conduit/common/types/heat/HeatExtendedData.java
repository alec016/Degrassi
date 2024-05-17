package es.degrassi.forge.core.common.conduit.common.types.heat;

import es.degrassi.common.NBT.ConduitNBTKeys;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.common.conduit.IExtendedConduitData;
import es.degrassi.forge.api.core.capability.IHeatStorage;
import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class HeatExtendedData implements IExtendedConduitData<HeatExtendedData> {

    private final Map<Direction, HeatSidedData> heatSidedData = new EnumMap<>(Direction.class);


    @Setter
    @Getter
    private double heatCapacity = 500;
    @Setter
    @Getter
    private double heat = 0;

    private LazyOptional<IHeatStorage> selfCap = LazyOptional.of( () -> new ConduitHeatStorage(this));


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (Direction direction: Direction.values()) {
            @Nullable HeatSidedData sidedData = heatSidedData.get(direction);
            if (sidedData != null) {
                tag.put(direction.name(), sidedData.toNbt());
            }
        }
        tag.putDouble(ConduitNBTKeys.HEAT_MAX_STORED, heatCapacity);
        tag.putDouble(ConduitNBTKeys.HEAT_STORED, heat);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        heatSidedData.clear();
        for (Direction direction: Direction.values()) {
            if (nbt.contains(direction.name())) {
                heatSidedData.put(direction, HeatSidedData.fromNbt(nbt.getCompound(direction.name())));
            }
        }
        if (nbt.contains(ConduitNBTKeys.HEAT_MAX_STORED)) {
            heatCapacity = Math.max(nbt.getDouble(ConduitNBTKeys.HEAT_MAX_STORED), 500);
        }
        if (nbt.contains(ConduitNBTKeys.HEAT_STORED)) {
            heat = nbt.getDouble(ConduitNBTKeys.HEAT_STORED);
        }
    }


  @Override
    public void onRemoved(IConduitType<?> type, Level level, BlockPos pos) {
        selfCap.invalidate();
    }

    public HeatSidedData compute(Direction direction) {
        return heatSidedData.computeIfAbsent(direction, dir -> new HeatSidedData());
    }

    LazyOptional<IHeatStorage> getSelfCap() {
        if (!selfCap.isPresent()) {
            selfCap = LazyOptional.of(() -> new ConduitHeatStorage(this));
        }
        return selfCap;
    }

    public static class HeatSidedData {
        public int rotatingIndex = 0;

        // region Serialization

        private static final String KEY_ROTATING_INDEX = "RotatingIndex";

        private CompoundTag toNbt() {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt(KEY_ROTATING_INDEX, rotatingIndex);
            return nbt;
        }

        private static HeatSidedData fromNbt(CompoundTag nbt) {
            HeatSidedData sidedData = new HeatSidedData();
            if (nbt.contains(KEY_ROTATING_INDEX, Tag.TAG_INT)) {
                sidedData.rotatingIndex = nbt.getInt(KEY_ROTATING_INDEX);
            }

            return sidedData;
        }

        // endregion
    }

    private record ConduitHeatStorage(HeatExtendedData data) implements IHeatStorage {

        @Override
        public double receive(double maxReceive, boolean simulate) {
            double receivable = Math.min(data.getHeatCapacity() - data().getHeat(), maxReceive);
            if (!simulate) {
                data.setHeat(data.getHeat()+receivable);
            }
            return receivable;
        }

        @Override
        public double extract(double maxExtract, boolean simulate) {
            double extractable = Math.min(data().getHeat(), maxExtract);
            if (!simulate) {
                data.setHeat(data.getHeat() - extractable);
            }
            return extractable;
        }

        @Override
        public double getHeat() {
            return data.getHeat();
        }

        @Override
        public double getHeatCapacity() {
            return data().getHeatCapacity();
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canInsert() {
            return true;
        }
    }
}
