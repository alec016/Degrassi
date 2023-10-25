package es.degrassi.forge.init.item.upgrade;

import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedUpgrade extends BaseUpgrade implements IFurnaceUpgrade {
  private Integer value;
  public SpeedUpgrade(Properties properties) {
    super(properties);
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }
  @Override
  public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
    if (getValue() == null) setValue(DegrassiConfig.speed_augment.get());

//    components.add(
//      Component.translatable(
//        "degrassi.upgrades.type",
//        getTypeString()
//      ).withStyle(ChatFormatting.AQUA)
//    );
    super.appendHoverText(stack, level, components, isAdvanced);
  }

  @Override
  public double getModifier() {
    return getValue() / 100d;
  }
}
