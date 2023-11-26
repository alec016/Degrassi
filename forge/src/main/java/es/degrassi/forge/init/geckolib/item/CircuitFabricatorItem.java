package es.degrassi.forge.init.geckolib.item;

import es.degrassi.forge.init.geckolib.entity.CircuitFabricatorEntity;
import es.degrassi.forge.init.geckolib.renderer.CircuitFabricatorItemRenderer;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.CreativeTabs;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public class CircuitFabricatorItem extends BlockItem implements IAnimatable {
  private static final AnimationBuilder ACTIVATE_ANIM = new AnimationBuilder().loop("chunk_loader");
  public final AnimationFactory factory = GeckoLibUtil.createFactory(this);
  public CircuitFabricatorItem() {
    super(BlockRegister.CIRCUIT_FABRICATOR.get(), new Properties().tab(CreativeTabs.MACHINES));
  }

  @Override
  public void initializeClient(Consumer<IClientItemExtensions> consumer) {
    consumer.accept(new IClientItemExtensions() {
      private BlockEntityWithoutLevelRenderer renderer = null;
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) renderer = new CircuitFabricatorItemRenderer();
        return renderer;
      }
    });
  }

  @Override
  public void registerControllers(AnimationData data) {
    data.addAnimationController(new AnimationController<>(this, "chunk_loader", 0, event -> {
      AnimationController<CircuitFabricatorItem> controller = event.getController();
      controller.transitionLengthTicks = 0;
      controller.setAnimation(ACTIVATE_ANIM);
      return PlayState.CONTINUE;
    }));
  }

  @Override
  public AnimationFactory getFactory() {
    return factory;
  }
}
