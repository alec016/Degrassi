package es.degrassi.forge.core.common.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import es.degrassi.forge.core.network.component.FluidPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class FluidComponent extends FluidTank implements IComponent {
  private final ComponentManager manager;
  private final String id;
  private boolean whitelist;
  private List<Fluid> filter;
  private final MachineEntity<?> entity;
  private ComponentIOMode mode;

  public FluidComponent(ComponentManager manager, String id, boolean whitelist, int capacity, MachineEntity<?> entity, ComponentIOMode mode, Fluid...fluids) {
    super(capacity);
    this.manager = manager;
    this.capacity = capacity;
    this.id = id;
    this.whitelist = whitelist;
    this.entity = entity;
    this.filter = new ArrayList<>();
    this.mode = mode;
    filter.addAll(Arrays.asList(fluids));
  }

  @Override
  public boolean isFluidValid(FluidStack stack) {
    return filter.stream().filter(fluid -> fluid.isSame(stack.getFluid())).findFirst().map(i -> whitelist).orElse(stack.isFluidEqual(getFluid()) || getFluid().isEmpty());
  }

  @Override
  public void onContentsChanged() {
    markDirty();
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new FluidPacket(this.fluid, this.capacity, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    if (requirement instanceof FluidRequirement req) {
      this.capacity = req.getAmount();
      this.filter.add(req.getFluid());
      this.whitelist = true;
      if (req.getMode().isInput())
        this.fluid = new FluidStack(req.getFluid(), req.getAmount());
      markDirty();
    }
  }

  @Override
  public CompoundTag serialize() {
    CompoundTag tag = new CompoundTag();
    super.writeToNBT(tag);
    tag.putInt("capacity", capacity);
    tag.putString("mode", mode.serialize());
    tag.putBoolean("whitelist", whitelist);
    ListTag filterListTag = new ListTag();
    filter.forEach(fluid -> filterListTag.add(new FluidStack(fluid, 0).writeToNBT(new CompoundTag())));
    tag.put("filter", filterListTag);
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    super.readFromNBT(nbt);
    capacity = nbt.getInt("capacity");
    whitelist = nbt.getBoolean("whitelist");
    mode = ComponentIOMode.deserialize(nbt.getString("mode"));
    filter.clear();
    ListTag filterTagList = nbt.getList("filter", Tag.TAG_COMPOUND);
    filterTagList.forEach(tag -> filter.add(FluidStack.loadFluidStackFromNBT((CompoundTag) tag).getFluid()));
  }

  public int toComparatorPower() {
    return (int) (subSized() * 15);
  }

  public float subSized() {
    return this.capacity > 0 ? (float) this.getFluidAmount() / this.capacity : 0;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (mode.output()) return 0;
    return super.fill(resource, action);
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    if (mode.input()) return FluidStack.EMPTY;
    return super.drain(maxDrain, action);
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    if (mode.input()) return FluidStack.EMPTY;
    return super.drain(resource, action);
  }

  public float getFillState() {
    return this.getFluidAmount() / (float) this.capacity;
  }

  // recipe stuff
  public int fillRecipe(FluidStack resource, FluidAction action) {
    return super.fill(resource, action);
  }

  public @NotNull FluidStack drainRecipe(int maxDrain, FluidAction action) {
    return super.drain(maxDrain, action);
  }

  public @NotNull FluidStack drainRecipe(FluidStack resource, FluidAction action) {
    return super.drain(resource, action);
  }

  @Override
  public String getTypeString() {
    return "fluid";
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("id", id);
    json.addProperty("whitelist", whitelist);
    json.addProperty("mode", mode.serialize());
    JsonArray filter = new JsonArray();
    this.filter.forEach(fluid -> filter.add(fluid.getFluidType().getDescription().getString()));
    json.add("filter", filter);
    json.addProperty("type", "fluid");
    return json;
  }

  public FluidComponent copy(MachineEntity<?> entity, ComponentManager manager) {
    return new FluidComponent(manager, id, whitelist, capacity, entity, mode, filter.toArray(Fluid[]::new));
  }
}
