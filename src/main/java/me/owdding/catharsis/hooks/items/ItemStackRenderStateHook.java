package me.owdding.catharsis.hooks.items;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public interface ItemStackRenderStateHook {

    default void catharsis$setCanFallthrough(boolean canFallthrough) {
        throw new UnsupportedOperationException();
    }

    default boolean catharsis$canFallthrough() {
        throw new UnsupportedOperationException();
    }

    default int catharsis$layerCount() {
        throw new UnsupportedOperationException();
    }

    default ItemStackRenderState.LayerRenderState[] catharsis$getLayers(int from, int to) {
        throw new UnsupportedOperationException();
    }

}
