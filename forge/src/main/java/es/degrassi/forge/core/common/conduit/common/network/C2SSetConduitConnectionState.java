package es.degrassi.forge.core.common.conduit.common.network;

import es.degrassi.common.conduit.ConduitTypes;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.forge.core.common.conduit.common.blockentity.ConduitBlockEntity;
import es.degrassi.forge.core.common.conduit.common.blockentity.connection.DynamicConnectionState;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2SSetConduitConnectionState implements Packet {

    private final BlockPos pos;
    private final Direction direction;
    private final IConduitType<?> conduitType;
    private final DynamicConnectionState connectionState;

    public C2SSetConduitConnectionState(BlockPos pos, Direction direction, IConduitType<?> conduitType, DynamicConnectionState connectionState) {
        this.pos = pos;
        this.direction = direction;
        this.conduitType = conduitType;
        this.connectionState = connectionState;
    }

    public C2SSetConduitConnectionState(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        direction = buf.readEnum(Direction.class);
        conduitType = ConduitTypes.getRegistry().getValue(buf.readResourceLocation());
        connectionState = DynamicConnectionState.fromNetwork(buf);
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
            conduitBlockEntity.handleConnectionStateUpdate(direction, conduitType, connectionState);
        }
    }

    protected void write(FriendlyByteBuf writeInto) {
        writeInto.writeBlockPos(pos);
        writeInto.writeEnum(direction);
        writeInto.writeResourceLocation(ConduitTypes.getRegistry().getKey(conduitType));
        connectionState.toNetwork(writeInto);
    }

    public static class Handler extends PacketHandler<C2SSetConduitConnectionState> {

        @Override
        public C2SSetConduitConnectionState fromNetwork(FriendlyByteBuf buf) {
            return new C2SSetConduitConnectionState(buf);
        }

        @Override
        public void toNetwork(C2SSetConduitConnectionState packet, FriendlyByteBuf buf) {
            packet.write(buf);
        }

        @Override
        public Optional<NetworkDirection> getDirection() {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
    }
}
