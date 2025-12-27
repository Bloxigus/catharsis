package me.owdding.catharsis.hooks.entity;

import me.owdding.catharsis.features.entity.CustomEntityModel;

public interface EntityHook {

    default void catharsis$removeCustomModel() {
        throw new UnsupportedOperationException();
    }

    default CustomEntityModel catharsis$getCustomEntityModel() {
        throw new UnsupportedOperationException();
    }
}
