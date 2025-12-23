package me.owdding.catharsis.hooks.armor;

import me.owdding.catharsis.features.armor.ArmorDefinitionRenderState;

public interface LivingEntityRenderStateHook {

    default boolean catharsis$getAndSetFirstDraw() {
        throw new UnsupportedOperationException();
    }

    default ArmorDefinitionRenderState catharsis$getArmorDefinitionRenderState() {
        throw new UnsupportedOperationException();
    }
}
