package es.degrassi.common.block;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.ModelProperty;

public interface IPaintableBlockEntity {

    Set<ModelProperty<Block>> PAINT_DATA_PROPERTIES = new HashSet<>();

    Block getPaint();

    default Block[] getPaints() {
        return new Block[] { getPaint() };
    }

    static ModelProperty<Block> createAndRegisterModelProperty() {
        ModelProperty<Block> property = new ModelProperty<>();
        PAINT_DATA_PROPERTIES.add(property);
        return property;
    }
}
