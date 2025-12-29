//~ named_identifier
package me.owdding.catharsis.mixins.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.owdding.catharsis.features.entity.models.CustomEntityModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Shadow
    protected abstract boolean isBodyVisible(S renderState);

    @Shadow
    protected M model;

    @WrapMethod(
        method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    private void catharsis$swapOutModel(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Operation<Void> original) {
        M originalModel = this.model;

        var customModel = renderState.catharsis$getCustomEntityModel();

        if (customModel != null && customModel.getModel() != null && model instanceof EntityModel<? super S> entityModel) {
            //noinspection unchecked
            this.model = (M) customModel.replaceModel(entityModel);
        }

        original.call(renderState, poseStack, bufferSource, packedLight);

        this.model = originalModel;
    }

    @ModifyArg(
        method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;")
    )
    private RenderType catharsis$modifyEntityTexture(RenderType renderType, @Local(argsOnly = true) S renderState) {
        var customModel = renderState.catharsis$getCustomEntityModel();

        if (customModel != null) {
            return catharsis$createEntityTextureRenderType(renderState, customModel);
        }

        return renderType;
    }

    @WrapWithCondition(
        method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V")
    )
    private boolean catharsis$modifyEntityLayers(RenderLayer<S, M> instance, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, EntityRenderState entityRenderState, float yRot, float xRot) {
        var customModel = entityRenderState.catharsis$getCustomEntityModel();

        return customModel == null;
    }

    @Unique
    private RenderType catharsis$createEntityTextureRenderType(S renderState, CustomEntityModel customEntityModel) {
        boolean bodyVisible = isBodyVisible(renderState);
        boolean spectatorVisible = !bodyVisible && !renderState.isInvisibleToPlayer;
        boolean isArmorStandMarker = renderState instanceof ArmorStandRenderState armorStandRenderState && armorStandRenderState.isMarker;
        var texture = customEntityModel.getTexture();

        if (isArmorStandMarker) {
            if (spectatorVisible) {
                return RenderType.entityTranslucent(texture, false);
            }
            if (bodyVisible) {
                return RenderType.entityCutoutNoCull(texture, false);
            }
        }

        if (spectatorVisible) {
            return RenderType.itemEntityTranslucentCull(texture);
        }

        if (bodyVisible) {
            return model.renderType(texture);
        }

        return RenderType.outline(texture);
    }
}
