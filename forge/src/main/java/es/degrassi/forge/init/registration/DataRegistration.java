package es.degrassi.forge.init.registration;

import dev.architectury.fluid.FluidStack;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.core.network.DataType;
import es.degrassi.forge.api.impl.core.components.config.SideConfig;
import es.degrassi.forge.core.network.data.BooleanData;
import es.degrassi.forge.core.network.data.DoubleData;
import es.degrassi.forge.core.network.data.FloatData;
import es.degrassi.forge.core.network.data.FluidStackData;
import es.degrassi.forge.core.network.data.IntegerData;
import es.degrassi.forge.core.network.data.ItemStackData;
import es.degrassi.forge.core.network.data.LongData;
import es.degrassi.forge.core.network.data.NbtData;
import es.degrassi.forge.core.network.data.SideConfigData;
import es.degrassi.forge.core.network.data.StringData;
import es.degrassi.forge.core.network.syncable.BooleanSyncable;
import es.degrassi.forge.core.network.syncable.DoubleSyncable;
import es.degrassi.forge.core.network.syncable.FloatSyncable;
import es.degrassi.forge.core.network.syncable.FluidStackSyncable;
import es.degrassi.forge.core.network.syncable.IntegerSyncable;
import es.degrassi.forge.core.network.syncable.ItemStackSyncable;
import es.degrassi.forge.core.network.syncable.LongSyncable;
import es.degrassi.forge.core.network.syncable.NbtSyncable;
import es.degrassi.forge.core.network.syncable.SideConfigSyncable;
import es.degrassi.forge.core.network.syncable.StringSyncable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class DataRegistration {
  public static final Registrar<DataType<?, ?>> DATA_REGISTRY = Register.REGISTRIES.builder(DataType.REGISTRY_KEY.location(), new DataType<?, ?>[]{}).build();
  public static final DeferredRegister<DataType<?, ?>> DATAS = DeferredRegister.create(Degrassi.MODID, DataType.REGISTRY_KEY);
  public static final RegistrySupplier<DataType<BooleanData, Boolean>> BOOLEAN_DATA = DATAS.register("boolean", () -> DataType.create(Boolean.class, BooleanSyncable::create, BooleanData::new));
  public static final RegistrySupplier<DataType<IntegerData, Integer>> INTEGER_DATA = DATAS.register("integer", () -> DataType.create(Integer.class, IntegerSyncable::create, IntegerData::new));
  public static final RegistrySupplier<DataType<DoubleData, Double>> DOUBLE_DATA = DATAS.register("double", () -> DataType.create(Double.class, DoubleSyncable::create, DoubleData::new));
  public static final RegistrySupplier<DataType<FloatData, Float>> FLOAT_DATA = DATAS.register("float", () -> DataType.create(Float.class, FloatSyncable::create, FloatData::new));
  public static final RegistrySupplier<DataType<ItemStackData, ItemStack>> ITEMSTACK_DATA = DATAS.register("itemstack", () -> DataType.create(ItemStack.class, ItemStackSyncable::create, ItemStackData::new));
  public static final RegistrySupplier<DataType<FluidStackData, FluidStack>> FLUIDSTACK_DATA = DATAS.register("fluidstack", () -> DataType.create(FluidStack.class, FluidStackSyncable::create, FluidStackData::new));
  public static final RegistrySupplier<DataType<StringData, String>> STRING_DATA = DATAS.register("string", () -> DataType.create(String.class, StringSyncable::create, StringData::new));
  public static final RegistrySupplier<DataType<LongData, Long>> LONG_DATA = DATAS.register("long", () -> DataType.create(Long.class, LongSyncable::create, LongData::new));
  public static final RegistrySupplier<DataType<SideConfigData, SideConfig>> SIDE_CONFIG_DATA = DATAS.register("side_config", () -> DataType.create(SideConfig.class, SideConfigSyncable::create, SideConfigData::readData));
  public static final RegistrySupplier<DataType<NbtData, CompoundTag>> NBT_DATA = DATAS.register("nbt", () -> DataType.create(CompoundTag.class, NbtSyncable::create, NbtData::new));

}
