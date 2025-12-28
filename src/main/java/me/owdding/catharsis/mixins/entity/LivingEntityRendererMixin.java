//~ named_identifier
package me.owdding.catharsis.mixins.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.owdding.catharsis.features.entity.CustomEntityModel;
import me.owdding.catharsis.features.entity.models.SafeModelPart;
import net.minecraft.client.model.EntityModel;
//? if > 1.21.8 {
import net.minecraft.client.renderer.SubmitNodeCollector;
//?} else {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
//? if > 1.21.10
import net.minecraft.client.renderer.rendertype.RenderTypes;
//? if > 1.21.8
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Shadow
    protected abstract boolean isBodyVisible(S renderState);

    @Shadow
    protected M model;

    //? if > 1.21.8 {
    @WrapMethod(
       method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"
    )
    private void catharsis$swapOutModel(S renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState, Operation<Void> original) {
    //?} else {
    /*@WrapMethod(
        method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    private void catharsis$swapOutModel(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Operation<Void> original) {
    *///?}
        M originalModel = this.model;

        var customModel = renderState.catharsis$getCustomEntityModel();

        if (customModel != null && customModel.getModel() != null && model instanceof EntityModel<? super S> entityModel) {
            //noinspection unchecked
            this.model = (M) SafeModelPart.replaceModel(entityModel, renderState);
        }

        //? if > 1.21.8 {
        original.call(renderState, poseStack, nodeCollector, cameraRenderState);
        //?} else {
        /*original.call(renderState, poseStack, bufferSource, packedLight);
        *///?}

        this.model = originalModel;
    }

    //? if > 1.21.8 {
    @ModifyArg(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
        //? if > 1.21.10 {
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"),
        //?} else {
        /*at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"),
        *///?}
        index = 3
    )
    //?} else {
    /*@ModifyArg(
        method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;")
    )
    *///?}
    private RenderType catharsis$modifyEntityTexture(RenderType renderType, @Local(argsOnly = true) S renderState) {
        var customModel = renderState.catharsis$getCustomEntityModel();

        if (customModel != null) {
            return catharsis$createEntityTextureRenderType(renderState, customModel);
        }

        return renderType;
    }

    //? if > 1.21.8 {
    @WrapWithCondition(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V")
    )
    private boolean catharsis$modifyEntityLayers(RenderLayer<S, M> instance, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, EntityRenderState entityRenderState, float yRot, float xRot) {
    //?} else {
    /*@WrapWithCondition(
        method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V")
    )
    private boolean catharsis$modifyEntityLayers(RenderLayer<S, M> instance, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, EntityRenderState entityRenderState, float yRot, float xRot) {
    *///?}
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
                //? if > 1.21.10 {
                return RenderTypes.entityTranslucent(texture, false);
                //?} else {
                /*return RenderType.entityTranslucent(texture, false);
                *///?}
            }
            if (bodyVisible) {
                //? if > 1.21.10 {
                return RenderTypes.entityCutoutNoCull(texture, false);
                //?} else {
                /*return RenderType.entityCutoutNoCull(texture, false);
                *///?}
            }
        }

        if (spectatorVisible) {
            //? if > 1.21.10 {
            return RenderTypes.itemEntityTranslucentCull(texture);
            //?} else {
            /*return RenderType.itemEntityTranslucentCull(texture);
            *///?}
        }

        if (bodyVisible) {
            return model.renderType(texture);
        }

        //? if > 1.21.10 {
        return RenderTypes.outline(texture);
        //?} else {
        /*return RenderType.outline(texture);
        *///?}
    }
}
