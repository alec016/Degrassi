package es.degrassi.forge.init.recipe.builder;

import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.requirements.IRequirement;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AbstractRecipeBuilder<T extends IDegrassiRecipe> {
    private boolean hidden = false;
    protected final List<IRequirement<?>> requirements = new ArrayList<>();

    public AbstractRecipeBuilder() {}

    public AbstractRecipeBuilder(T recipe) {
        this.hidden = !recipe.showInJei();
    }

    public AbstractRecipeBuilder<T> hide() {
        this.hidden = true;
        return this;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public abstract T build(ResourceLocation id);

    public abstract AbstractRecipeBuilder<T> addRequirement(IRequirement<?> requirement);

    public List<IRequirement<?>> getRequirements() {
        return this.requirements;
    }
}
