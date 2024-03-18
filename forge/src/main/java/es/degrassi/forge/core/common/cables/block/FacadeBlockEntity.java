package es.degrassi.forge.core.common.cables.block;

import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FacadeBlockEntity extends CableBlockEntity {
  public static final String MIMIC_TAG = "mimic";
  @Nullable private BlockState mimicBlock = null;
  public FacadeBlockEntity(BlockPos pos, BlockState blockState) {
    super(EntityRegistration.CABLE_FACADE.get(), pos, blockState, CableTier.EMPTY, CableType.FACADE);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);

    if (level.isClientSide()) {
      level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
      requestModelDataUpdate();
    }
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    CompoundTag nbtTag = super.getUpdateTag();
    saveMimic(nbtTag);
    return ClientboundBlockEntityDataPacket.create(this, (BlockEntity entity) -> nbtTag);
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    saveMimic(tag);
    return tag;
  }

  @Nullable
  public BlockState getMimicBlock() {
    return mimicBlock;
  }

  @Override
  public @NotNull ModelData getModelData() {
    return ModelData.builder()
      .with(CableBlock.FACADEID, mimicBlock)
      .build();
  }

  public void setMimicBlock(BlockState mimicBlock) {
    this.mimicBlock = mimicBlock;
    setChanged();
    getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    loadMimic(tag);
  }

  private void loadMimic(CompoundTag tag) {
    if (tag.contains(MIMIC_TAG))
      mimicBlock = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound(MIMIC_TAG));
    else
      mimicBlock = null;
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    saveMimic(tag);
  }

  private void saveMimic(CompoundTag tag) {
    if (mimicBlock != null) {
      CompoundTag nbt = NbtUtils.writeBlockState(mimicBlock);
      tag.put(MIMIC_TAG, nbt);
    }
  }
}
