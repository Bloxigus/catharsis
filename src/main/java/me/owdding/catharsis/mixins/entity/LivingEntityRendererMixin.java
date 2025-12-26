//~ named_identifier
package me.owdding.catharsis.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.owdding.catharsis.features.entity.CustomEntityModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>  {

    @WrapOperation(
        method = "getRenderType",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/Identifier;")
    )
    public Identifier catharsis$modifyEntityTexture(LivingEntityRenderer<T, S, M> instance, S entityRenderState, Operation<Identifier> original) {
        CustomEntityModel replacement = entityRenderState.catharsis$getCustomEntityModel();
        if (replacement != null) return replacement.getTexture();

        return original.call(instance, entityRenderState);
    }
}
