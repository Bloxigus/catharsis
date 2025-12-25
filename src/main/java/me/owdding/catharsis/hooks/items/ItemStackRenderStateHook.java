package me.owdding.catharsis.hooks.items;

public interface ItemStackRenderStateHook {

    default void catharsis$setCanFallthrough(boolean canFallthrough) {
        throw new UnsupportedOperationException();
    }

    default boolean catharsis$canFallthrough() {
        throw new UnsupportedOperationException();
    }

}
