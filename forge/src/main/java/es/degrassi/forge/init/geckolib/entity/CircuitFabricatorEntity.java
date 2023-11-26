package es.degrassi.forge.init.geckolib.entity;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.registration.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CircuitFabricatorEntity extends BaseEntity implements IAnimatable {
  private static final AnimationBuilder ACTIVATE_ANIM = new AnimationBuilder().loop("chunk_loader");

  public final AnimationFactory factory = GeckoLibUtil.createFactory(this);
  public CircuitFabricatorEntity(BlockPos blockPos, BlockState blockState) {
    super(EntityRegister.CIRCUIT_FABRICATOR.get(), blockPos, blockState);
  }

  @Override
  public void registerControllers(@NotNull AnimationData data) {
    data.addAnimationController(new AnimationController<>(this, "chunk_loader", 0, event -> {
      AnimationController<CircuitFabricatorEntity> controller = event.getController();
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
