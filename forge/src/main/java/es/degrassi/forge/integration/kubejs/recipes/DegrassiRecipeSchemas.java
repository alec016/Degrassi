package es.degrassi.forge.integration.kubejs.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ArrayRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.TimeComponent;
import es.degrassi.forge.requirements.IRequirement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface DegrassiRecipeSchemas {
  RecipeComponent<IRequirement<?>> REQUIREMENT_COMPONENT = new RecipeComponent<>() {
    @Override
    public Class<?> componentClass() {
      return String.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, IRequirement value) {
      return IRequirement.CODEC.encodeStart(JsonOps.INSTANCE, value).result().orElseThrow(() -> new RecipeExceptionJS("Can't write requirement: " + value));
    }

    @Contract("_, null -> fail")
    @Override
    public @NotNull IRequirement<?> read(RecipeJS recipe, Object from) {
      if(from instanceof JsonElement json)
        return IRequirement.CODEC.read(JsonOps.INSTANCE, json).result().orElseThrow(() -> new RecipeExceptionJS("Can't parse requirement: " + from));
      throw new RecipeExceptionJS("Can't parse requirement: " + from);
    }
  };
  ArrayRecipeComponent<IRequirement<?>> REQUIREMENT_LIST = REQUIREMENT_COMPONENT.asArray();
  RecipeKey<Long> TIME = TimeComponent.TICKS.key("time");
  RecipeKey<IRequirement<?>[]> REQUIREMENTS = REQUIREMENT_LIST.key("requirements").optional(new IRequirement[0]).alwaysWrite().exclude();

}
