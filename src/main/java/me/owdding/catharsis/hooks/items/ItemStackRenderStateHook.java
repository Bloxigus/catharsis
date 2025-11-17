package me.owdding.catharsis.hooks.items;

public interface ItemStackRenderStateHook {

    void catharsis$setCanFallthrough(boolean canFallthrough);

    boolean catharsis$canFallthrough();

}
