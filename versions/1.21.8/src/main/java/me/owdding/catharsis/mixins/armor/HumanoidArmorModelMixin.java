package me.owdding.catharsis.mixins.armor;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.owdding.catharsis.features.armor.models.ArmorModelState;
import me.owdding.catharsis.hooks.armor.HumanoidRenderStateHook;
import me.owdding.catharsis.hooks.armor.LivingEntityRenderStateHook;
import me.owdding.catharsis.utils.extensions.MiscExtensionsKt;
import me.owdding.catharsis.utils.geometry.BedrockGeometryRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorModelMixin<S extends HumanoidRenderState, A extends HumanoidModel<S>> {

    @WrapMethod(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V")
    private void catharsis$onRender(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, HumanoidRenderState renderState, float yRot, float xRot, Operation<Void> original) {
        try {
            HumanoidRenderStateHook.CURRENT_RENDER_STATE.set(renderState);
            original.call(poseStack, bufferSource, packedLight, renderState, yRot, xRot);
        } finally {
            HumanoidRenderStateHook.CURRENT_RENDER_STATE.remove();
        }
    }

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void catharsis$onBake(
        CallbackInfo ci,
        @Local(argsOnly = true) EquipmentSlot slot,
        @Local(argsOnly = true) MultiBufferSource buffers,
        @Local(argsOnly = true) PoseStack stack,
        @Local(argsOnly = true) int light,
        @Local(argsOnly = true) A model
    ) {
        var state = HumanoidRenderStateHook.CURRENT_RENDER_STATE.get();
        if (!(state instanceof LivingEntityRenderStateHook hook)) return;
        if (!(hook.catharsis$getArmorDefinitionRenderState().fromSlot(slot) instanceof ArmorModelState.Bedrock renderer)) return;

        var textures = renderer.getTextures();
        var colors = renderer.getColors();

        for (int i = 0; i < renderer.getLayers(); i++) {
            var texture = textures[i];
            var color = colors[i];
            var consumer = buffers.getBuffer(RenderType.entityCutoutNoCull(texture));

            model.setupAnim(MiscExtensionsKt.unsafeCast(state));
            stack.pushPose();
            BedrockGeometryRenderer.render(renderer.getGeometry(), slot, model, stack.last(), consumer, color, light, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }
        ci.cancel();
    }
}
