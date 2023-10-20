package es.degrassi.forge.init.item.upgrade;

import es.degrassi.comon.init.item.upgrade.PanelUpgrade;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TransferUpgrade extends PanelUpgrade {
  public TransferUpgrade(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
    if (getType() == null) setType(DegrassiConfig.trans_type.get());
    if (getValue() == null) setValue(DegrassiConfig.trans_augment.get());

    components.add(
      Component.translatable(
        "degrassi.upgrades.type",
        getTypeString()
      ).withStyle(ChatFormatting.AQUA)
    );
    components.add(
      Component.translatable(
        "degrassi.upgrades.transfer.tooltips",
        getTransferModifier()
      ).withStyle(ChatFormatting.YELLOW)
    );
    super.appendHoverText(stack, level, components, isAdvanced);
  }

  public double getTransferModifier() {
    if (getType() == null) return 1d;
    return switch (getType()) {
      case ADD -> 1 + getValue() / 100d;
      case MULTIPLY -> 1 * getValue() * 1d;
      case EXPONENTIAL -> 1 * getValue() * getValue() * 1d;
    };
  }

  @Override
  public double getModifier() {
    return getTransferModifier();
  }

}
