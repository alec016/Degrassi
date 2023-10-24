package es.degrassi.forge.init.item.upgrade;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class PanelUpgrade extends BaseUpgrade {
  private Integer value;
  private UpgradeType type;

  public void setValue(Integer value) {
    this.value = value;
  }

  public void setType(UpgradeType type) {
    this.type = type;
  }

  public Integer getValue() {
    return value;
  }

  public UpgradeType getType() {
    return type;
  }
  public PanelUpgrade(@NotNull Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
    return super.use(level, player, usedHand);
  }

  public String getTypeString() {
    if (getType() == null) return "null";
    return getType().getName();
  }

  public abstract double getModifier();
}
