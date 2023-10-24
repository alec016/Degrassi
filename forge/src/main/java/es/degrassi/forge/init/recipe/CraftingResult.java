package es.degrassi.forge.init.recipe;

import net.minecraft.network.chat.Component;

public final class CraftingResult {
    private static final CraftingResult SUCCESS = new CraftingResult(RESULT.SUCCESS, Component.literal("success"));
    private static final CraftingResult PASS = new CraftingResult(RESULT.PASS, Component.literal("pass"));

    private final RESULT result;
    private final Component message;

    private CraftingResult(RESULT result, Component message) {
        this.result = result;
        this.message = message;
    }

    public static CraftingResult success() {
        return SUCCESS;
    }

    public static CraftingResult pass() {
        return PASS;
    }

    public static CraftingResult error(Component message) {
        return new CraftingResult(RESULT.ERROR, message);
    }

    public boolean isSuccess() {
        return result != RESULT.ERROR;
    }

    public Component getMessage() {
        return this.message;
    }

    public enum RESULT {
        SUCCESS,
        PASS,
        ERROR
    }
}
