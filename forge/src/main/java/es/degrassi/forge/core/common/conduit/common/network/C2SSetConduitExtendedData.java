package es.degrassi.forge.core.common.conduit.common.network;

import es.degrassi.common.conduit.ConduitTypes;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.common.conduit.IExtendedConduitData;
import es.degrassi.forge.core.common.conduit.common.blockentity.ConduitBlockEntity;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2SSetConduitExtendedData implements Packet {
    private final BlockPos pos;
    private final IConduitType<?> conduitType;
    private final CompoundTag extendedConduitData;

    public C2SSetConduitExtendedData(BlockPos pos, IConduitType<?> conduitType, IExtendedConduitData<?> extendedConduitData) {
        this.pos = pos;
        this.conduitType = conduitType;
        this.extendedConduitData = extendedConduitData.serializeGuiNBT();
    }

    public C2SSetConduitExtendedData(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        conduitType = ConduitTypes.getRegistry().getValue(buf.readResourceLocation());
        extendedConduitData = buf.readNbt();
    }

    @Override
    public boolean isValid(NetworkEvent.Context context) {
        return context.getSender() != null && conduitType != null;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ServerLevel level = context.getSender().serverLevel();

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ConduitBlockEntity conduitBlockEntity) {
            conduitBlockEntity.handleExtendedDataUpdate(conduitType, extendedConduitData);
        }
    }

    protected void write(FriendlyByteBuf writeInto) {
        writeInto.writeBlockPos(pos);
        writeInto.writeResourceLocation(ConduitTypes.getRegistry().getKey(conduitType));
        writeInto.writeNbt(extendedConduitData);
    }

    public static class Handler extends Packet.PacketHandler<C2SSetConduitExtendedData> {

        @Override
        public C2SSetConduitExtendedData fromNetwork(FriendlyByteBuf buf) {
            return new C2SSetConduitExtendedData(buf);
        }

        @Override
        public void toNetwork(C2SSetConduitExtendedData packet, FriendlyByteBuf buf) {
            packet.write(buf);
        }

        @Override
        public Optional<NetworkDirection> getDirection() {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
    }
}
