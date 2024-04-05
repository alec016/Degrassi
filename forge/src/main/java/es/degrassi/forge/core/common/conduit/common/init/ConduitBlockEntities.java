package es.degrassi.forge.core.common.conduit.common.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.common.blockentity.ConduitBlockEntity;

public class ConduitBlockEntities {
    private static final Registrate REGISTRATE = Degrassi.registrate();

    public static final BlockEntityEntry<ConduitBlockEntity> CONDUIT = REGISTRATE
        .blockEntity("conduit", ConduitBlockEntity::new)
        .validBlock(ConduitBlocks.CONDUIT)
        .register();

    public static void register() {}
}
