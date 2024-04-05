package es.degrassi.forge.core.common.conduit.common.types.item;

import es.degrassi.forge.Degrassi;
import es.degrassi.common.conduit.IClientConduitData;
import es.degrassi.common.misc.Vector2i;
import es.degrassi.forge.core.common.conduit.client.gui.widget.CheckBox;
import es.degrassi.forge.core.common.conduit.common.init.EnderConduitTypes;
import es.degrassi.forge.core.common.conduit.common.lang.DegrassiLang;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemClientConduitData implements IClientConduitData<ItemExtendedData> {
    @Override
    public List<AbstractWidget> createWidgets(Screen screen, ItemExtendedData extendedConduitData,
        UpdateExtendedData<ItemExtendedData> updateExtendedConduitData, Supplier<Direction> direction, Vector2i widgetsStart) {
        // TODO: Method of doing sync that does not require CoreNetwork in API.
        List<AbstractWidget> widgets = new ArrayList<>();
        widgets.add(new CheckBox(Degrassi.rl("textures/gui/round_robin.png"), widgetsStart.add(110, 20), () -> extendedConduitData.get(direction.get()).roundRobin, bool -> {
            updateExtendedConduitData.update(data -> {
                data.compute(direction.get()).roundRobin = bool;
                return data;
            });
        }, () -> DegrassiLang.ROUND_ROBIN_ENABLED, () -> DegrassiLang.ROUND_ROBIN_DISABLED));
        widgets.add(new CheckBox(Degrassi.rl("textures/gui/self_feed.png"), widgetsStart.add(130, 20), () -> extendedConduitData.get(direction.get()).selfFeed, bool -> {
            updateExtendedConduitData.update(data -> {
                data.compute(direction.get()).selfFeed = bool;
                return data;
            });
        }, () -> DegrassiLang.SELF_FEED_ENABLED, () -> DegrassiLang.SELF_FEED_DISABLED));
        return widgets;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return EnderConduitTypes.ICON_TEXTURE;
    }

    @Override
    public Vector2i getTexturePosition() {
        return new Vector2i(0, 96);
    }
}
