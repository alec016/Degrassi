package es.degrassi.forge.core.common.conduit.common.network;

import es.degrassi.forge.core.common.conduit.common.blockentity.EnderBlockEntity;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

public class C2SDataSlotChange implements Packet {

    private final BlockPos pos;

    // You shouldn't really send null, but its "technically" validate.
    @Nullable
    private final FriendlyByteBuf updateData;

    public C2SDataSlotChange(BlockPos pos, FriendlyByteBuf updateData) {
        this.pos = pos;
        this.updateData = updateData;
    }

    public C2SDataSlotChange(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        updateData = new FriendlyByteBuf(buf.copy());
    }

    @Override
    public boolean isValid(NetworkEvent.Context context) {
        return context.getSender() != null && updateData != null;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ServerLevel level = context.getSender().serverLevel();

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof EnderBlockEntity enderBlockEntity) {
            enderBlockEntity.serverHandleBufferChange(updateData);
        }
    }

    protected void write(FriendlyByteBuf writeInto) {
        writeInto.writeBlockPos(pos);
        writeInto.writeBytes(updateData);
    }

    public static class Handler extends PacketHandler<C2SDataSlotChange> {

        @Override
        public C2SDataSlotChange fromNetwork(FriendlyByteBuf buf) {
            return new C2SDataSlotChange(buf);
        }

        @Override
        public void toNetwork(C2SDataSlotChange packet, FriendlyByteBuf buf) {
            packet.write(buf);
        }

        @Override
        public Optional<NetworkDirection> getDirection() {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
    }
}