package es.degrassi.forge.api.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class TextureSizeHelper {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<ResourceLocation, Pair<Integer, Integer>> SIZES = new HashMap<>();

    public static int getTextureWidth(@Nullable ResourceLocation texture) {
        if(texture == null)
            return 0;
        else if(SIZES.containsKey(texture))
            return SIZES.get(texture).getLeft();
        else {
            try {
                BufferedImage image = ImageIO.read(Minecraft.getInstance().getResourceManager().open(texture));
                SIZES.put(texture, Pair.of(image.getWidth(), image.getHeight()));
                return image.getWidth();
            } catch (IOException e) {
                LOGGER.warn("No texture found for location: " + texture);
            }
            return 0;
        }
    }

    public static int getTextureHeight(@Nullable ResourceLocation texture) {
        if(texture == null)
            return 0;
        else if(SIZES.containsKey(texture))
            return SIZES.get(texture).getRight();
        else {
            try {
                BufferedImage image = ImageIO.read(Minecraft.getInstance().getResourceManager().open(texture));
                SIZES.put(texture, Pair.of(image.getWidth(), image.getHeight()));
                return image.getHeight();
            } catch (IOException e) {
                LOGGER.warn("No texture found for location: " + texture);
            }
            return 0;
        }
    }
}