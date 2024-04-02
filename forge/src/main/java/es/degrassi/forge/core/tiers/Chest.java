package es.degrassi.forge.core.tiers;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.registry.IVariant;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum Chest implements IVariant<Chest> {
  IRON(4, 9, 7, 98),
  GOLD(6, 9, 7, 140),
  DIAMOND(6, 12, 35, 140),
  EMERALD(9, 12, 35, 185),
  NETHERITE(9, 15, 62, 185);

  private final int rows, cols, invX, invY;

  Chest(int rows, int cols, int invX, int invY) {
    this.rows = rows;
    this.cols = cols;
    this.invX = invX;
    this.invY = invY;
  }

  public static Chest[] getNormalVariants() {
    return new Chest[] { IRON, GOLD, DIAMOND, EMERALD, NETHERITE };
  }

  public static Chest value(String tier) {
    return switch (tier.toLowerCase(Locale.ROOT)) {
      case "netherite" -> NETHERITE;
      case "emerald" -> EMERALD;
      case "diamond" -> DIAMOND;
      case "gold" -> GOLD;
      default -> IRON;
    };
  }

  public int getCols() {
    return cols;
  }

  public int getRows() {
    return rows;
  }

  public int getTotalSlots() {
    return rows * cols;
  }

  public Component getTranslation() {
    return Component.translatable("block.degrassi.chest_" + nameL());
  }

  public ResourceLocation getBackground() {
    return new DegrassiLocation("textures/gui/background_chest_" + nameL() + ".png");
  }

  @Override
  public Chest[] getVariants() {
    return values();
  }

  public int getInvX() {
    return invX;
  }
  public int getInvY() {
    return invY;
  }
}
