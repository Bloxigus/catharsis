//~ named_identifier
package me.owdding.catharsis.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.owdding.catharsis.features.entity.CustomEntityModel;
import me.owdding.catharsis.features.entity.models.SafeModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Shadow
    protected abstract boolean isBodyVisible(S renderState);

    @Shadow
    protected M model;

    @WrapMethod(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"
    )
    private void catharsis$swapOutModel(S renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState, Operation<Void> original) {
        M originalModel = this.model;

        var customModel = renderState.catharsis$getCustomEntityModel();

        if (customModel != null && customModel.getModel() != null && model instanceof EntityModel<? super S> entityModel) {
            //noinspection unchecked
            this.model = (M) SafeModelPart.replaceModel(entityModel, renderState);
        }

        original.call(renderState, poseStack, nodeCollector, cameraRenderState);

        this.model = originalModel;
    }

    @WrapOperation(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
    )
    public void catharsis$modifyEntityTexture(SubmitNodeCollector instance, Model<? super S> model, Object o, PoseStack poseStack, RenderType renderType, int i, int j, int k, TextureAtlasSprite textureAtlasSprite, int l, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original, @Local(argsOnly = true) S renderState) {
        var customModel = renderState.catharsis$getCustomEntityModel();
        var customRenderType = renderType;

        if (customModel != null) {
            customRenderType = catharsis$createEntityTextureRenderType(renderState, customModel);
        }

        original.call(instance, model, o, poseStack, customRenderType, i, j, k, textureAtlasSprite, l, crumblingOverlay);
    }

    @WrapOperation(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V")
    )
    public void catharsis$modifyEntityLayers(RenderLayer<S, M> instance, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, EntityRenderState entityRenderState, float yRot, float xRot, Operation<Void> original) {
        var customModel = entityRenderState.catharsis$getCustomEntityModel();

        if (customModel != null) return;

        original.call(instance, poseStack, submitNodeCollector, light, entityRenderState, yRot, xRot);
    }

    @Unique
    private RenderType catharsis$createEntityTextureRenderType(S renderState, CustomEntityModel customEntityModel) {
        boolean bodyVisible = isBodyVisible(renderState);
        boolean spectatorVisible = !bodyVisible && !renderState.isInvisibleToPlayer;
        boolean isArmorStandMarker = renderState instanceof ArmorStandRenderState armorStandRenderState && armorStandRenderState.isMarker;
        var texture = customEntityModel.getTexture();

        if (isArmorStandMarker) {
            if (spectatorVisible) {
                return RenderTypes.entityTranslucent(texture, false);
            }
            if (bodyVisible) {
                return RenderTypes.entityCutoutNoCull(texture, false);
            }
        }

        if (spectatorVisible) {
            return RenderTypes.itemEntityTranslucentCull(texture);
        }

        if (bodyVisible) {
            return model.renderType(texture);
        }

        return RenderTypes.outline(texture);
    }
}
