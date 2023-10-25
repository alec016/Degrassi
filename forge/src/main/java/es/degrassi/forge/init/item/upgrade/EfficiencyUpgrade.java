package es.degrassi.forge.init.item.upgrade;

import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EfficiencyUpgrade extends BaseUpgrade implements IPanelUpgrade {
  private Integer value;
  private UpgradeType type;
  public EfficiencyUpgrade(Properties properties) {
    super(properties);
  }

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

  @Override
  public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
    if (getType() == null) setType(DegrassiConfig.eff_type.get());
    if (getValue() == null) setValue(DegrassiConfig.eff_augment.get());

    components.add(
      Component.translatable(
        "degrassi.upgrades.type",
        getTypeString()
      ).withStyle(ChatFormatting.AQUA)
    );
    components.add(
      Component.translatable(
        "degrassi.upgrades.efficiency.tooltips",
        getEfficiencyModifier()
      ).withStyle(ChatFormatting.YELLOW)
    );
    super.appendHoverText(stack, level, components, isAdvanced);
  }

  public double getEfficiencyModifier() {
    if (getType() == null) return 1d;
    return switch (getType()) {
      case ADD -> 1 + getValue() / 100d;
      case MULTIPLY -> 1 * getValue() * 1d;
      case EXPONENTIAL -> 1 * getValue() * getValue() * 1d;
    };
  }

  @Override
  public double getModifier() {
    return getEfficiencyModifier();
  }
}
