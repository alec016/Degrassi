package es.degrassi.forge.init.item.upgrade;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.init.item.upgrade.types.IFurnaceUpgrade;
import es.degrassi.forge.init.item.upgrade.types.IMelterUpgrade;
import es.degrassi.forge.init.item.upgrade.types.IUpgradeMakerUpgrade;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedUpgrade extends BaseUpgrade implements IFurnaceUpgrade, IMelterUpgrade, IUpgradeMakerUpgrade {
  private Integer value;
  private Integer energy_augment;
  public SpeedUpgrade(Properties properties) {
    super(properties, UpgradeUpgradeType.SPEED);
  }

  public Integer getValue() {
    return value;
  }

  public Integer getEnergyValue() {
    return energy_augment;
  }

  public void setValue(Integer value) {
    this.value = value;
  }
  public void setEnergyValue(Integer value) {
    this.energy_augment = value;
  }
  @Override
  public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
    if (getValue() == null) setValue(DegrassiConfig.get().upgradeConfig.speed_augment);
    if (getEnergyValue() == null) setEnergyValue(DegrassiConfig.get().upgradeConfig.speed_energy_augment);
    if (ClientHandler.isShiftKeyDown()) {
      components.add(
        Component.literal(
          Component.translatable(
            "degrassi.upgrades.speed.tooltip",
            1 + getModifier()
          ).getString() + "%"
        ).withStyle(ChatFormatting.YELLOW)
      );

      components.add(
        Component.literal(
          Component.translatable(
            "degrassi.upgrades.speed.energy.tooltip",
            1 + (getEnergyValue() / 100d)
          ).getString() + "%"
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

  @Override
  public double getModifier() {
    if (getValue() == null) setValue(DegrassiConfig.get().upgradeConfig.speed_augment);
    if (getEnergyValue() == null) setEnergyValue(DegrassiConfig.get().upgradeConfig.speed_energy_augment);
    return getValue() / 100d;
  }
}
