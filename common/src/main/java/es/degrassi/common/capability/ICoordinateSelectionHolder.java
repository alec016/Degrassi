package es.degrassi.common.capability;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface ICoordinateSelectionHolder {

    @Nullable
    CoordinateSelection getSelection();

    void setSelection(CoordinateSelection selection);

    default boolean hasSelection() {
        return getSelection() != null;
    }

    default void ifSelectionPresent(Consumer<CoordinateSelection> cons) {
        if (hasSelection()) {
            cons.accept(getSelection());
        }
    }
}
