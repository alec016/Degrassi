package es.degrassi.common.registry;

public interface IVariantEntry<V extends IVariant, R extends IVariantEntry<V, R>> {
    V getVariant();
}
