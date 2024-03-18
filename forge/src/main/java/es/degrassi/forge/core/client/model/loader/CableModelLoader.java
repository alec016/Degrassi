package es.degrassi.forge.core.client.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.client.model.CableBakedModel;
import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.tiers.CableTier;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public class CableModelLoader implements IGeometryLoader<CableModelLoader.CableModelGeometry> {
  public static final ResourceLocation GENERATOR_LOADER = new DegrassiLocation("cableloader");

  public static void register(ModelEvent.RegisterGeometryLoaders event){
    event.register("cableloader", new CableModelLoader());
  }

  @Override
  public CableModelGeometry read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    boolean facade = jsonObject.has("facade") && jsonObject.get("facade").getAsBoolean();
    CableType type = CableType.NONE;
    CableTier.Tier tier = new CableTier.Tier();
    if (jsonObject.has("cable_type")) {
      type = CableType.of(jsonObject.get("cable_type").getAsString());
    }

    if (jsonObject.has("cable_tier"))
      tier = CableTier.of(jsonObject.get("cable_tier").getAsString(), type);
    return new CableModelGeometry(facade, type, tier);
  }

  public static class CableModelGeometry implements IUnbakedGeometry<CableModelGeometry> {
    private final boolean facade;
    private final CableType type;
    private final CableTier.Tier tier;
    public CableModelGeometry(boolean facade, CableType type, CableTier.Tier tier) {
      this.facade = facade;
      this.type = type;
      this.tier = tier;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker arg, Function<Material, TextureAtlasSprite> function, ModelState arg2, ItemOverrides arg3, ResourceLocation arg4) {
      return new CableBakedModel(context, facade, type, tier);
    }
  }
}
