package es.degrassi.forge.core.common.conduit.common.integrations;

import es.degrassi.common.conduit.IFacadeItem;
import es.degrassi.common.integration.Integration;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ConduitSelfIntegration implements Integration {

    @Override
    public Optional<BlockState> getFacadeOf(ItemStack stack) {
        if (stack.getItem() instanceof IFacadeItem facadeItem) {
            return Optional.of(facadeItem.getTexture(stack));
        }
        return Optional.empty();
    }
}
