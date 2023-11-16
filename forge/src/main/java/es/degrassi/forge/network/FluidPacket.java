package es.degrassi.forge.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import es.degrassi.forge.init.entity.MelterEntity;
import es.degrassi.forge.init.entity.renderer.LerpedFloat;
import es.degrassi.forge.init.entity.type.IFluidEntity;
import es.degrassi.forge.init.gui.container.MelterContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FluidPacket extends BaseS2CMessage {
  private final FluidStack fluid;
  private final BlockPos pos;

  public FluidPacket(FluidStack fluid, BlockPos pos) {
    this.fluid = fluid;
    this.pos = pos;
  }

  @Override
  public MessageType getType() {
    return PacketManager.FLUID;
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeFluidStack(fluid);
    buf.writeBlockPos(pos);
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    if (context.getEnvironment() == Env.CLIENT)
      context.queue(() -> {
        assert Minecraft.getInstance().level != null;
        if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof IFluidEntity entity) {
          entity.setFluid(this.fluid);
          if (entity instanceof MelterEntity tank) {
            float fillState = tank.getFluidStorage().getFluidAmount() / (float) tank.getFluidStorage().getCapacity();
            if (tank.getFluidLevel() == null)
              tank.setFluidLevel(LerpedFloat.linear()
                .startWithValue(fillState));
            tank.getFluidLevel()
              .chase(fillState, 0.5, LerpedFloat.Chaser.EXP);
          }
          if (Minecraft.getInstance().player.containerMenu instanceof MelterContainer menu && menu.getEntity().getBlockPos().equals(pos)) {
            entity.setFluid(this.fluid);
            if (entity instanceof MelterEntity tank) {
              float fillState = tank.getFluidStorage().getFluidAmount() / (float) tank.getFluidStorage().getCapacity();
              if (tank.getFluidLevel() == null)
                tank.setFluidLevel(LerpedFloat.linear()
                  .startWithValue(fillState));
              tank.getFluidLevel()
                .chase(fillState, 0.5, LerpedFloat.Chaser.EXP);
            }
          }
        }
      });
  }

  @Contract("_ -> new")
  public static @NotNull FluidPacket read(@NotNull FriendlyByteBuf buf) {
    return new FluidPacket(buf.readFluidStack(), buf.readBlockPos());
  }
}
