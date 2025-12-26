package me.owdding.catharsis.mixins.entity;

import me.owdding.catharsis.features.entity.CustomEntityDefinitions;
import me.owdding.catharsis.features.entity.CustomEntityModel;
import me.owdding.catharsis.features.entity.CustomEntityModels;
import me.owdding.catharsis.hooks.entity.EntityHook;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class EntityMixin implements EntityHook {

    @Unique
    private boolean catharsis$hasComputedTexture = false;
    @Unique
    private CustomEntityModel catharsis$computedReplacement = null;

    //TODO: call this whenever attributes or equipment gets updated
    @Override
    public void catharsis$removeCustomTexture() {
        catharsis$hasComputedTexture = false;
    }

    @Override
    public CustomEntityModel catharsis$getCustomEntityModel() {
        if (catharsis$hasComputedTexture) {
            if (catharsis$computedReplacement != null) return catharsis$computedReplacement;
            return null;
        }

        var customEntity = CustomEntityDefinitions.getFor((Entity) (Object) this);

        catharsis$hasComputedTexture = true;

        CustomEntityModel customModel = null;

        if (customEntity != null) {
            customModel = CustomEntityModels.getModel(customEntity.getReplacement());
        }


        catharsis$computedReplacement = customModel;

        return customModel;
    }
}
