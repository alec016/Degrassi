package es.degrassi.forge.core.common.conduit.client.model;

import es.degrassi.common.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IQuadTransformer;

public record BlockColorQuadDataTransformer(BlockPos pos, Level level, BlockState texture) implements IQuadTransformer {
    @Override
    public void processInPlace(BakedQuad quad) {
        int rgb = Minecraft.getInstance().getBlockColors().getColor(texture, level, pos);
        if (rgb != -1) {
            for (int i = 0; i < 4; i++) {
                RenderUtil.multiplyColor(quad.getVertices(), i, rgb);
            }
        }
    }
}
