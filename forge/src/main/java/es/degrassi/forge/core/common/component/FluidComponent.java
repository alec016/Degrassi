package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IManager;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.FluidPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidComponent extends FluidTank implements IComponent {
  private final ComponentManager manager;
  private final String id;
  private final boolean whitelist;
  private final List<Fluid> filter;
  private final MachineEntity<?> entity;

  public FluidComponent(ComponentManager manager, String id, boolean whitelist, int capacity, MachineEntity<?> entity, Fluid...fluids) {
    super(capacity);
    this.manager = manager;
    this.capacity = capacity;
    this.id = id;
    this.whitelist = whitelist;
    this.entity = entity;
    this.filter = new ArrayList<>();
    filter.addAll(Arrays.asList(fluids));
  }

  @Override
  public boolean isFluidValid(FluidStack stack) {
    return filter.stream().filter(fluid -> fluid.isSame(stack.getFluid())).findFirst().map(i -> whitelist).orElse(!whitelist);
  }

  @Override
  public IManager<IComponent> getManager() {
    return manager;
  }

  @Override
  protected void onContentsChanged() {
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
  public String getId() {
    return id;
  }

  @Override
  public void serialize(CompoundTag nbt) {
    CompoundTag tag = new CompoundTag();
    super.writeToNBT(tag);
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    CompoundTag tag = nbt.getCompound(id);
    super.readFromNBT(tag);
  }
}
