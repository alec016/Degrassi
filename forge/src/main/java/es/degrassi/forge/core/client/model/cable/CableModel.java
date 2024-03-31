package es.degrassi.forge.core.client.model.cable;

import es.degrassi.forge.core.client.model.AbstractModel;
import es.degrassi.forge.core.client.renderer.cable.CableRenderer;
import es.degrassi.forge.core.common.cables.CableEntity;
import es.degrassi.forge.core.common.cables.Transfer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public abstract class CableModel<T extends CableEntity<?, ?>, R extends CableRenderer<T, M, R>, M extends CableModel<T, R, M>> extends AbstractModel<T, R> {

  private static final String NORTH = "north";
  private static final String NORTH_PLATE = "north_plate";
  private static final String SOUTH = "south";
  private static final String SOUTH_PLATE = "south_plate";
  private static final String WEST = "west";
  private static final String WEST_PLATE = "west_plate";
  private static final String EAST = "east";
  private static final String EAST_PLATE = "east_plate";
  private static final String DOWN = "down";
  private static final String DOWN_PLATE = "down_plate";
  private static final String UP = "up";
  private static final String UP_PLATE = "up_plate";

  final ModelPart north;
  final ModelPart northPlate;
  final ModelPart south;
  final ModelPart southPlate;
  final ModelPart west;
  final ModelPart westPlate;
  final ModelPart east;
  final ModelPart eastPlate;
  final ModelPart down;
  final ModelPart downPlate;
  final ModelPart upPlate;
  final ModelPart up;
  protected final Map<Transfer, ResourceLocation> TEXTURES = new HashMap<>();

  public CableModel(ModelPart root) {
    super(RenderType::entitySolid);
    this.north = root.getChild(NORTH);
    this.northPlate = root.getChild(NORTH_PLATE);
    this.south = root.getChild(SOUTH);
    this.southPlate = root.getChild(SOUTH_PLATE);
    this.west = root.getChild(WEST);
    this.westPlate = root.getChild(WEST_PLATE);
    this.east = root.getChild(EAST);
    this.eastPlate = root.getChild(EAST_PLATE);
    this.down = root.getChild(DOWN);
    this.downPlate = root.getChild(DOWN_PLATE);
    this.up = root.getChild(UP);
    this.upPlate = root.getChild(UP_PLATE);
  }

  public static LayerDefinition createDefinition() {
    var meshDefinition = new MeshDefinition();
    var root = meshDefinition.getRoot();

    var pos = PartPose.offset(0F, 14F, 0F);

    root.addOrReplaceChild(NORTH, CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, -1.5F, -7.75F, 3, 3, 6), pos);
    root.addOrReplaceChild(NORTH_PLATE, CubeListBuilder.create().texOffs(0, 20).addBox(-2.5F, -2.5F, -8.2F, 5, 5, 1), pos);
    root.addOrReplaceChild(SOUTH, CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, 1.75F, 3, 3, 6), pos);
    root.addOrReplaceChild(SOUTH_PLATE, CubeListBuilder.create().texOffs(0, 20).addBox(-2.5F, -2.5F, 7.2F, 5, 5, 1), pos);

    root.addOrReplaceChild(WEST, CubeListBuilder.create().texOffs(19, 0).addBox(-7.75F, -1.5F, -1.5F, 6, 3, 3), pos);
    root.addOrReplaceChild(WEST_PLATE, CubeListBuilder.create().texOffs(13, 20).addBox(-8.2F, -2.5F, -2.5F, 1, 5, 5), pos);
    root.addOrReplaceChild(EAST, CubeListBuilder.create().texOffs(19, 7).addBox(1.75F, -1.5F, -1.5F, 6, 3, 3), pos);
    root.addOrReplaceChild(EAST_PLATE, CubeListBuilder.create().texOffs(13, 20).addBox(7.2F, -2.5F, -2.5F, 1, 5, 5), pos);

    root.addOrReplaceChild(DOWN, CubeListBuilder.create().texOffs(38, 10).addBox(-1.5F, -7.75F, -1.5F, 3, 6, 3), pos);
    root.addOrReplaceChild(DOWN_PLATE, CubeListBuilder.create().texOffs(26, 20).addBox(-2.5F, -8.2F, -2.5F, 5, 1, 5), pos);
    root.addOrReplaceChild(UP, CubeListBuilder.create().texOffs(38, 0).addBox(-1.5F, 1.75F, -1.5F, 3, 6, 3), pos);
    root.addOrReplaceChild(UP_PLATE, CubeListBuilder.create().texOffs(26, 20).addBox(-2.5F, 7.2F, -2.5F, 5, 1, 5), pos);

    return LayerDefinition.create(meshDefinition, 64, 32);
  }
}
