package es.degrassi.forge.api.ingredient;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemIngredient implements IIngredient<Item> {

    private final Item item;

    public ItemIngredient(Item item) {
        this.item = item;
    }

    @Override
    public List<Item> getAll() {
        return Collections.singletonList(this.item);
    }

    @Override
    public boolean test(Item item) {
        return this.item == item;
    }

    @Override
    public String toString() {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.item)).toString();
    }
}
