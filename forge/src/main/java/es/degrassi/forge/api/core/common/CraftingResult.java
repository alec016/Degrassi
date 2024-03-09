package es.degrassi.forge.api.core.common;

import net.minecraft.network.chat.Component;

public class CraftingResult {
  private static final CraftingResult SUCCESS = new CraftingResult(Result.SUCCESS, Component.empty());
  private static final CraftingResult PASS = new CraftingResult(Result.PASS, Component.empty());

  private Component errorMessage;
  private final Result success;

  private CraftingResult(Result success, Component message) {
    this.success = success;
    this.errorMessage = message;
  }

  public static CraftingResult success() {
    return SUCCESS;
  }

  public static CraftingResult pass() {
    return PASS;
  }

  public static CraftingResult error(Component message) {
    return new CraftingResult(Result.ERROR, message);
  }

  public Component getMessage() {
    return errorMessage;
  }

  public void setMessage(Component message) {
    this.errorMessage = message;
  }

  public boolean isSuccess() {
    return this.success.isSuccess();
  }

  private enum Result {
    PASS,
    SUCCESS,
    ERROR;

    public boolean isSuccess() {
      return this == PASS || this == SUCCESS;
    }
  }
}
