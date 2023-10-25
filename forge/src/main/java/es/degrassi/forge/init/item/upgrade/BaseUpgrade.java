package es.degrassi.forge.init.item.upgrade;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BaseUpgrade extends Item {
  protected final UpgradeUpgradeType upgradeType;

  public BaseUpgrade(Properties properties, UpgradeUpgradeType upgradeType) {
    super(properties);
    this.upgradeType = upgradeType;
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
    return super.use(level, player, usedHand);
  }

  public UpgradeUpgradeType getUpgradeType() {
    return this.upgradeType;
  }

  public abstract double getModifier();
}
