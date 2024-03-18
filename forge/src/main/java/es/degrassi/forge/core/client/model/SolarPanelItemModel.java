package es.degrassi.forge.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.zeith.hammerlib.client.model.IBakedModel;
import org.zeith.hammerlib.client.model.LoadUnbakedGeometry;
import org.zeith.hammerlib.util.java.Cast;

@LoadUnbakedGeometry(path = "sp")
public class SolarPanelItemModel implements IUnbakedGeometry<SolarPanelItemModel> {
  final SolarPanelBlock block;
  Material baseTx, topTx, sideTx;
  public SolarPanelItemModel(JsonObject obj, JsonDeserializationContext context) {
    this.block = Cast.optionally(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "panel"))), SolarPanelBlock.class)
      .orElseThrow(() -> new JsonSyntaxException("Unable to find solar panel block by id '" + GsonHelper.getAsString(obj, "panel") + "'"));

    var registryName = ForgeRegistries.BLOCKS.getKey(block);

    baseTx = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(registryName.getNamespace(), "block/panel/sp/" + registryName.getPath() + "_base"));
    topTx = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(registryName.getNamespace(), "block/panel/sp/" + registryName.getPath() + "_top"));
    sideTx = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(registryName.getNamespace(), "block/panel/sp/" + registryName.getPath() + "_side"));
  }

  @Override
  public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker arg, Function<Material, TextureAtlasSprite> function, ModelState arg2, ItemOverrides arg3, ResourceLocation modelLocation) {
    return new Baked(block, function.apply(topTx), function.apply(baseTx), function.apply(sideTx), modelLocation);
  }

  private static class Baked implements IBakedModel {
    public static final FaceBakery COOKER = new FaceBakery();

    public final ResourceLocation modelName;
    public final SolarPanelBlock block;
    public final TextureAtlasSprite top, base, side;

    public Baked(SolarPanelBlock block, TextureAtlasSprite top, TextureAtlasSprite base, TextureAtlasSprite side, ResourceLocation modelName) {
      this.block = block;
      this.base = base;
      this.side = side;
      this.top = top;
      this.modelName = modelName;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction sideIn, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
      List<BakedQuad> quads = new ArrayList<>();
      Direction[] sides = sideIn == null ? Direction.values() : new Direction[] { sideIn };
      for(Direction side : sides)
        if(side != null) {
          float h = 6;

          quads.add(COOKER.bakeQuad(
            new Vector3f(0, 0, 0), new Vector3f(16, h, 16),
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
              0,
              side.getAxis() == Direction.Axis.Y ? 0 : (16F - h),
              16,
              16
            }, 4)),
            side == Direction.UP ? top : side == Direction.DOWN ? base : this.side, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(0, h, 1), new Vector3f(1, h + 0.25F, 15), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(side != Direction.UP ? new float[] {
              0,
              0,
              16,
              1
            } : new float[] {
              0,
              0,
              1,
              16
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(15, h, 1), new Vector3f(16, h + 0.25F, 15), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(side != Direction.UP ? new float[] {
              0,
              0,
              16,
              1
            } : new float[] {
              15,
              0,
              16,
              16
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(1, h, 0), new Vector3f(15, h + 0.25F, 1), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
              0,
              0,
              16,
              1
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(1, h, 15), new Vector3f(15, h + 0.25F, 16), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
              0,
              0,
              16,
              1
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(0, h, 0), new Vector3f(1, h + 0.25F, 1), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
              0,
              0,
              1,
              1
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(15, h, 0), new Vector3f(16, h + 0.25F, 1), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
              15,
              0,
              16,
              1
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(15, h, 15), new Vector3f(16, h + 0.25F, 16), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
              15,
              15,
              16,
              16
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));

          quads.add(COOKER.bakeQuad( //
            new Vector3f(0, h, 15), new Vector3f(1, h + 0.25F, 16), //
            new BlockElementFace(null, 0, "#0", new BlockFaceUV(new float[] {
              0,
              15,
              1,
              16
            }, 4)), //
            this.top, side, BlockModelRotation.X0_Y0, null, true, modelName));
        }
      return quads;
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

    final RandomSource rand = RandomSource.create();

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
      return switch (rand.nextInt(1, 4)) {
        case 1 -> top;
        case 2 -> base;
        default -> side;
      };
    }
  }
}
