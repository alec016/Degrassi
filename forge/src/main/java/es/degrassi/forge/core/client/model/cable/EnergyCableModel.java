package es.degrassi.forge.core.client.model.cable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.client.renderer.cable.EnergyCableRenderer;
import es.degrassi.forge.core.common.cables.energy.EnergyCableEntity;
import es.degrassi.forge.core.common.cables.Transfer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyCableModel extends CableModel<EnergyCableEntity, EnergyCableRenderer, EnergyCableModel> {

  public EnergyCableModel(ModelPart root) {
    super(root);
    TEXTURES.put(Transfer.ALL, new DegrassiLocation("textures/model/tile/energy_cable_all.png"));
    TEXTURES.put(Transfer.EXTRACT, new DegrassiLocation("textures/model/tile/energy_cable_out.png"));
    TEXTURES.put(Transfer.RECEIVE, new DegrassiLocation("textures/model/tile/energy_cable_in.png"));
  }

  @Override
  public void render(EnergyCableEntity te, EnergyCableRenderer renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
    if (te.getLevel() == null)
      return;
    final Direction[] flags = new Direction[6];
    for (Direction side : te.sides) {
      final BlockPos pos = te.getBlockPos().relative(side);
      final BlockEntity tile = te.getLevel().getBlockEntity(pos);
      final Transfer config = te.getSideConfig().getType(side);
      if (!(tile instanceof EnergyCableEntity) && EnvHandler.INSTANCE.hasEnergy(te.getLevel(), pos, side.getOpposite())
        && (config.canExtract() || config.canReceive())) {
        flags[side.get3DDataValue()] = side;
      }
    }

    if (flags[0] != null) {
      Transfer type = te.getSideConfig().getType(flags[0]);
      if (!type.equals(Transfer.NONE)) {
        VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
        this.up.render(matrix, buffer, light, ov);
        this.upPlate.render(matrix, buffer, light, ov);
      }
    }

    if (flags[1] != null) {
      Transfer type = te.getSideConfig().getType(flags[1]);
      if (!type.equals(Transfer.NONE)) {
        VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
        this.down.render(matrix, buffer, light, ov);
        this.downPlate.render(matrix, buffer, light, ov);
      }
    }

    if (flags[2] != null) {
      Transfer type = te.getSideConfig().getType(flags[2]);
      if (!type.equals(Transfer.NONE)) {
        VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
        this.south.render(matrix, buffer, light, ov);
        this.southPlate.render(matrix, buffer, light, ov);
      }
    }
    if (flags[3] != null) {
      Transfer type = te.getSideConfig().getType(flags[3]);
      if (!type.equals(Transfer.NONE)) {
        VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
        this.north.render(matrix, buffer, light, ov);
        this.northPlate.render(matrix, buffer, light, ov);
      }
    }

    if (flags[4] != null) {
      Transfer type = te.getSideConfig().getType(flags[4]);
      if (!type.equals(Transfer.NONE)) {
        VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
        this.west.render(matrix, buffer, light, ov);
        this.westPlate.render(matrix, buffer, light, ov);
      }
    }

    if (flags[5] != null) {
      Transfer type = te.getSideConfig().getType(flags[5]);
      if (!type.equals(Transfer.NONE)) {
        VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
        this.east.render(matrix, buffer, light, ov);
        this.eastPlate.render(matrix, buffer, light, ov);
      }
    }
  }
}
