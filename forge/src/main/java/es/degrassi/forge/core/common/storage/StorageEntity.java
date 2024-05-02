package es.degrassi.forge.core.common.storage;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.item.wrench.IWrenchable;
import es.degrassi.forge.core.common.machines.item.wrench.WrenchMode;
import es.degrassi.forge.core.common.recipe.StorageRecipe;
import es.degrassi.forge.core.tiers.Storage;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class StorageEntity<T extends Storage.S<?, ?>> extends MachineEntity<StorageRecipe> implements IWrenchable {
  protected final SideConfig config = new SideConfig(this);
  protected T tier;
  public StorageEntity(BlockEntityType<?> type , BlockPos pos , BlockState blockState , T tier) {
    super(type , pos , blockState);
    this.tier = tier;
    if (tier.isCreative()) {
      config.init(ComponentIOMode.OUTPUT);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    if (tag.contains("config"))
      config.deserializeNBT(tag.getCompound("config"));
    tier = (T) tier.deserializeNBT(tag.getString("tier"));
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("config", config.serializeNBT());
    tag.putString("tier", tier.serializeNBT());
  }

  public static void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull StorageEntity<?> entity
  ) {
    MachineEntity.serverTick(level, pos, state, entity);
    entity.config.serverTick(level, pos);
    setChanged(level, pos, state);
  }

  @Override
  public boolean onWrench(BlockState state , Level world , BlockPos pos , Player player , InteractionHand hand , Direction side , WrenchMode mode , Vec3 hit) {
    ComponentIOMode oldMode = config.getMode(side);
    boolean changed = config.onClick(side);
    ComponentIOMode newMode = config.getMode(side);

    if (changed) player.displayClientMessage(
      Component.translatable("info.degrassi.config.mode",
        Component.translatable("info.degrassi.config.mode.from",
          Component.literal(oldMode.serialize()).withStyle(ChatFormatting.YELLOW)
        ),
        Component.translatable("info.degrassi.config.mode.to",
          Component.literal(newMode.serialize()).withStyle(ChatFormatting.YELLOW)
        )
      ),
      true);

    return changed;
  }
}
