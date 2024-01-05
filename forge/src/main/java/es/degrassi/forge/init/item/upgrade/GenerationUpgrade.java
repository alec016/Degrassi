package es.degrassi.forge.init.item.upgrade;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.init.item.upgrade.types.IGeneratorUpgrade;
import es.degrassi.forge.init.item.upgrade.types.IPanelUpgrade;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenerationUpgrade extends BaseUpgrade implements IPanelUpgrade, IGeneratorUpgrade {
  private Integer value;
  private UpgradeType type;
  public GenerationUpgrade(Properties properties) {
    super(properties, UpgradeUpgradeType.GENERATION);
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
    if (getType() == null) setType(DegrassiConfig.get().upgradeConfig.gen_type);
    if (getValue() == null) setValue(DegrassiConfig.get().upgradeConfig.gen_augment);
    if (ClientHandler.isShiftKeyDown()) {
      components.add(
        Component.translatable(
          "degrassi.upgrades.type",
          getTypeString()
        ).withStyle(ChatFormatting.AQUA)
      );
      components.add(
        Component.translatable(
          "degrassi.upgrades.generation.tooltips",
          getGenerationModifier()
        ).withStyle(ChatFormatting.YELLOW)
      );
    } else {
      components.add(
        Component.translatable(
          "degrassi.upgrades.shift.tooltip",
          Component.translatable("degrassi.upgrades.shift.press.tooltip").withStyle(ChatFormatting.GRAY),
          Component.literal("[SHIFT]").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC, ChatFormatting.UNDERLINE),
          Component.translatable("degrassi.upgrades.shift.more.tooltip").withStyle(ChatFormatting.GRAY)
        )
      );
    }

    super.appendHoverText(stack, level, components, isAdvanced);
  }

  public double getGenerationModifier() {
    if (getType() == null) return 1d;
    return switch (getType()) {
      case ADD -> 1 + getValue() / 100d;
      case MULTIPLY -> 1 * getValue() * 1d;
      case EXPONENTIAL -> 1 * getValue() * getValue() * 1d;
    };
  }

  @Override
  public double getModifier() {
    return getGenerationModifier();
  }
}
