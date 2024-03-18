package es.degrassi.forge.core.client.model;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class SolarPanelModel implements IDynamicBakedModel {
  public static final FaceBakery COOKER = new FaceBakery();
  public final SolarPanelBlock block;
  public final ResourceLocation registryName;
  final ResourceLocation modelName;

  public SolarPanelModel(SolarPanelBlock block) {
    this.block = block;
    this.registryName = ForgeRegistries.BLOCKS.getKey(block);
    this.modelName = new ModelResourceLocation(Degrassi.MODID, "sp", "");
  }

  @Override
  public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction sideIn, @NotNull RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType) {
    List<BakedQuad> quads = new ArrayList<>();
    Direction[] sides = sideIn == null ? Direction.values() : new Direction[] { sideIn };
    for (Direction side : sides) {
      if (side != null) {
        Level level = modelData.get(SolarPanelEntity.WORLD_PROP);
        BlockPos pos = modelData.get(SolarPanelEntity.POS_PROP);

        TextureAtlasSprite tasTop = t_top(), tasBase = t_base(), tasSide = t_side();
        float h = 6;

        quads.add(COOKER.bakeQuad(
          new Vector3f(0, 0, 0), new Vector3f(16, h, 16),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
            0,
            side.getAxis() == Direction.Axis.Y ? 0 : (16f - h),
            16,
            16
          }, 4)),
          side == Direction.UP ? tasTop : side == Direction.DOWN ? tasBase : tasSide,
          side,
          BlockModelRotation.X0_Y0,
          null,
          true,
          modelName
        ));

        if (level == null || pos == null) return quads;

        boolean west = false, east = false, north = false, south = false;

        if (west = level.getBlockState(pos.west()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(0, h, 1), new Vector3f(1, h + 0.25f, 15),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(side != Direction.UP
            ? new float[] { 0, 0, 16, 1 }
            : new float[] { 0, 0, 1, 16 },
            4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));

        if (east = level.getBlockState(pos.east()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(15, h, 1), new Vector3f(16, h + 0.25f, 15),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(side != Direction.UP
            ? new float[] { 0, 0, 16, 1 }
            : new float[] { 15, 0, 16, 16 },
          4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));

        if (north = level.getBlockState(pos.north()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(1, h, 0), new Vector3f(15, h + 0.25f, 1),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(
            new float[] { 0, 0, 16, 1 },
            4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));

        if (south = level.getBlockState(pos.south()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(1, h, 15), new Vector3f(15, h + 0.25f, 16),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(
            new float[] { 0, 0, 16, 1 },
            4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));

        if (west || north || level.getBlockState(pos.east().north()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(0, h, 0), new Vector3f(1, h + 0.25F, 1),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(
            new float[] { 0, 0, 1, 1 },
            4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));

        if (east || north || level.getBlockState(pos.east().north()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(15, h, 0), new Vector3f(16, h + 0.25F, 1),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(
            new float[] { 15, 0, 16, 1 },
            4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));

        if (south || east || level.getBlockState(pos.south().east()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(15, h, 15), new Vector3f(16, h + 0.25F, 16),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(
            new float[] { 15, 15, 16, 16 },
            4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));

        if (west ||south || level.getBlockState(pos.west().south()).getBlock() != block) quads.add(COOKER.bakeQuad(
          new Vector3f(0, h, 15), new Vector3f(1, h + 0.25F, 16),
          new BlockElementFace(null, 0, "#0", new BlockFaceUV(
            new float[] { 0, 15, 1, 16 },
            4)
          ),
          tasTop, side, BlockModelRotation.X0_Y0, null, true, modelName
        ));
      }
    }
    return quads;
  }

  @Override
  public @NotNull TextureAtlasSprite getParticleIcon() {
    int number = ThreadLocalRandom.current().nextInt(1, 4);
    return switch (number) {
      case 1 -> t_base();
      case 2 -> t_top();
      default -> t_side();
    };
  }

  final Function<ResourceLocation, TextureAtlasSprite> spriteGetter = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
  ResourceLocation baseTx, topTx, sideTx;

  public TextureAtlasSprite t_base() {
    if(baseTx == null)
      baseTx = new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath() + "_base");
    return spriteGetter.apply(baseTx);
  }

  public TextureAtlasSprite t_top() {
    if(topTx == null)
      topTx = new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath() + "_top");
    return spriteGetter.apply(topTx);
  }

  public TextureAtlasSprite t_side() {
    if(sideTx == null)
      sideTx = new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath() + "_side");
    return spriteGetter.apply(sideTx);
  }

  @Override
  public @NotNull ItemOverrides getOverrides() {
    return ItemOverrides.EMPTY;
  }


  @Override
  public boolean useAmbientOcclusion() {
    return false;
  }

  @Override
  public boolean isGui3d() {
    return false;
  }

  @Override
  public boolean usesBlockLight() {
    return true;
  }

  @Override
  public boolean isCustomRenderer() {
    return false;
  }
}
