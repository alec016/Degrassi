package es.degrassi.forge.api.core.machine;

import es.degrassi.forge.api.core.component.IComponentManager;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class MachineTile extends BlockEntity {
  public MachineTile(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
  }
  public abstract IMachine getMachine();
  public abstract void refreshMachine(@Nullable ResourceLocation machineId);
  public abstract void setPaused(boolean paused);
  public abstract boolean isPaused();
  public abstract boolean isUnloaded();
  public abstract MachineStatus getStatus();
  public abstract Component getMessage();
  public abstract void setStatus(MachineStatus status, Component message);
  public void setStatus(MachineStatus status) {
    this.setStatus(status, Component.empty());
  }
  public abstract void resetProcess();
  public abstract void refreshClientData();
  public abstract IComponentManager getComponentManager();
  //  public abstract IMachineUpgradeManager getUpgradeManager();
  //public abstract IProcessor getProcessor();
  public abstract void setOwner(LivingEntity entity);
  @Nullable
  public abstract UUID getOwnerId();
  @Nullable
  public abstract Component getOwnerName();
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
  }
}
