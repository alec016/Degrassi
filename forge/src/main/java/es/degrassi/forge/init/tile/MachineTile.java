package es.degrassi.forge.init.tile;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentManager;
import es.degrassi.forge.api.core.element.IElementManager;
import es.degrassi.forge.api.core.machine.IMachine;
import es.degrassi.forge.api.core.machine.MachineStatus;
import es.degrassi.forge.api.core.network.ISyncable;
import es.degrassi.forge.api.core.network.ISyncableStuff;
import es.degrassi.forge.api.impl.util.TextComponentUtils;
import es.degrassi.forge.core.components.ComponentManager;
import es.degrassi.forge.core.elements.ElementManager;
import es.degrassi.forge.core.network.syncable.StringSyncable;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MachineTile extends BlockEntity implements ISyncableStuff {
  public static final ResourceLocation DUMMY = new DegrassiLocation("dummy");
  private ResourceLocation id = DUMMY;
  private boolean paused = false;
  protected ComponentManager componentManager = new ComponentManager(this);
  protected ElementManager elementManager = new ElementManager(this);
  private MachineStatus status = MachineStatus.IDLE;
  private Component errorMessage = Component.empty();

  @Nullable
  private Component ownerName;
  @Nullable
  private UUID ownerId;

  public MachineTile(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
  }
  public void refreshMachine(@Nullable ResourceLocation id) {
    if(this.level == null || this.level.isClientSide())
      return;
//    CompoundTag craftingManagerNBT = this.processor.serialize();
    CompoundTag componentManagerNBT = this.componentManager.serializeNBT();
    if(id == null)
      id = getId();
    this.id = id;
//    this.processor = getMachine().getProcessorTemplate().build(this);
    this.componentManager = new ComponentManager(componentManager.getTemplates(), this);
//    this.processor.deserialize(craftingManagerNBT);
    this.componentManager.deserializeNBT(componentManagerNBT);
    this.componentManager.getMap().values().forEach(IComponent::init);

//    new SRefreshCustomMachineTilePacket(this.worldPosition, id).sendToChunkListeners(this.level.getChunkAt(this.worldPosition));
  }
  public void setPaused(boolean paused) {
    this.paused = paused;
  }
  public boolean isPaused() {
    return this.paused;
  }
  public Component getMessage() {
    return this.errorMessage;
  }
  public void setStatus(MachineStatus status, Component message) {
    if(this.status != status) {
      this.componentManager.getMap().values().forEach(component -> component.onStatusChanged(this.status, status, message));
      this.status = status;
      this.errorMessage = message;
      this.setChanged();
      if(this.getLevel() != null && !this.getLevel().isClientSide()) {
        BlockPos pos = this.getBlockPos();
//        new SUpdateMachineStatusPacket(pos, this.status).sendToChunkListeners(this.getLevel().getChunkAt(pos));
      }
    }
  }
  public void setStatus(MachineStatus status) {
    this.setStatus(status, Component.empty());
  }
  public void resetProcess() {
    if(this.level == null || this.level.isClientSide())
      return;
//    this.processor.reset();
  }
  public void refreshClientData() {
    requestModelDataUpdate();
  }
  public IComponentManager getComponentManager(){
    return componentManager;
  }
  public IElementManager getElementManager(){
    return elementManager;
  }

  public ResourceLocation getId() {
    return id;
  }

  public void setId(ResourceLocation id) {
    this.id = id;
    this.componentManager = new ComponentManager(componentManager.getTemplates(), this);
    this.componentManager.getMap().values().forEach(IComponent::init);
  }

  //  public abstract IMachineUpgradeManager getUpgradeManager();
  //public abstract IProcessor getProcessor();

  public MachineStatus getStatus () {
    return this.isPaused() ? MachineStatus.PAUSED : status;
  }

  public void setOwner(LivingEntity entity){
    if (entity == null) return;
    this.ownerId = entity.getUUID();
    this.ownerName = entity.getName();
  }
  @Nullable
  public UUID getOwnerId() {
    return ownerId;
  }
  @Nullable
  public Component getOwnerName() {
    return ownerName;
  }
  public boolean isOwner(LivingEntity entity) {
    return entity.getUUID().equals(this.getOwnerId());
  }
  @Nullable
  public LivingEntity getOwner() {
    if(this.getOwnerId() == null || this.getLevel() == null || this.getLevel().getServer() == null)
      return null;

    //Try to get the owner as a player
    ServerPlayer player = this.getLevel().getServer().getPlayerList().getPlayer(this.getOwnerId());
    if(player != null)
      return player;

    //Try to get the owner as a player
    for(ServerLevel level : this.getLevel().getServer().getAllLevels()) {
      Entity entity = level.getEntity(this.getOwnerId());
      if(entity instanceof LivingEntity living)
        return living;
    }

    return null;
  }@Override
  public void saveAdditional(CompoundTag nbt) {
    super.saveAdditional(nbt);
    nbt.putString("machineID", this.id.toString());
//    nbt.put("craftingManager", this.processor.serialize());
    nbt.put("componentManager", this.componentManager.serializeNBT());
    nbt.putString("status", this.status.toString());
    nbt.putString("message", TextComponentUtils.toJsonString(this.errorMessage));
  }

  @Override
  public void load(CompoundTag nbt) {
    super.load(nbt);
    if(nbt.contains("machineID", Tag.TAG_STRING)/* && getMachine() == CustomMachine.DUMMY*/)
      this.setId(new ResourceLocation(nbt.getString("machineID")));

//    if(nbt.contains("craftingManager", Tag.TAG_COMPOUND))
//      this.processor.deserialize(nbt.getCompound("craftingManager"));

    if(nbt.contains("componentManager", Tag.TAG_COMPOUND))
      this.componentManager.deserializeNBT(nbt.getCompound("componentManager"));

    if(nbt.contains("status", Tag.TAG_STRING))
      this.setStatus(MachineStatus.value(nbt.getString("status")));

    if(nbt.contains("message", Tag.TAG_STRING))
      this.errorMessage = TextComponentUtils.fromJsonString(nbt.getString("message"));

//    if(nbt.contains("appearance", Tag.TAG_COMPOUND))
//      this.customAppearance = MachineAppearance.CODEC.read(NbtOps.INSTANCE, nbt.get("appearance")).result().map(MachineAppearance::new).orElse(null);
  }

  //Needed for multiplayer sync
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    nbt.putString("machineID", getId().toString());
    nbt.putString("status", this.status.toString());
    nbt.putString("message", TextComponentUtils.toJsonString(this.errorMessage));
//    if(this.customAppearance != null)
//      MachineAppearance.CODEC.encodeStart(NbtOps.INSTANCE, this.customAppearance.getProperties()).result().ifPresent(appearance -> nbt.put("appearance", appearance));
    return nbt;
  }

  //Needed for multiplayer sync
  @Nullable
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  /**CONTAINER STUFF**/

  @Override
  public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
//    if(this.processor instanceof ISyncableStuff syncableProcessor)
//      syncableProcessor.getStuffToSync(container);
    this.componentManager.getStuffToSync(container);
    container.accept(StringSyncable.create(() -> this.status.toString(), status -> this.status = MachineStatus.value(status)));
    container.accept(StringSyncable.create(() -> Component.Serializer.toJson(this.errorMessage), errorMessage -> this.errorMessage = Component.Serializer.fromJson(errorMessage)));
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state, MachineTile tile) {
    if(tile.componentManager == null/* || tile.processor == null*/)
      return;

    level.getProfiler().push("Component tick");
    tile.componentManager.serverTick();
    level.getProfiler().pop();

    if(tile.isPaused())
      return;

    level.getProfiler().push("Crafting Manager tick");
//    try {
//      tile.processor.tick();
//    } catch (ComponentNotFoundException e) {
//      Degrassi.LOGGER.error(e.getMessage());
//      tile.setPaused(true);
//    }
    level.getProfiler().pop();
  }

  public static void clientTick(Level level, BlockPos pos, BlockState state, MachineTile tile) {
    if(tile.componentManager == null /*|| tile.processor == null*/)
      return;

    tile.componentManager.clientTick();
  }

  @Override
  public void setLevel(Level level) {
    super.setLevel(level);
//    MachineList.addMachine(this);
    this.componentManager.getMap().values().forEach(IComponent::init);
  }

  @Override
  public void setRemoved() {
    if(this.level != null && !this.level.isClientSide())
      this.componentManager.getMap().values().forEach(IComponent::onRemoved);
    super.setRemoved();
  }
}
