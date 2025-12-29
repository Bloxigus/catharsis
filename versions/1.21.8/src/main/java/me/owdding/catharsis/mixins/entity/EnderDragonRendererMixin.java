package me.owdding.catharsis.mixins.entity;

import com.llamalad7.mixinextras.sugar.Local;
import me.owdding.catharsis.features.entity.models.CustomEntityModel;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
//? if > 1.21.10 {
/*import net.minecraft.client.renderer.RenderTypes;
 *///?} else {
import net.minecraft.client.renderer.RenderType;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EnderDragonRenderer.class)
public class EnderDragonRendererMixin {

    @ModifyArgs(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 2
        ),
        method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    public void catharsis$handleDragonTextureReplacement(Args args, @Local(argsOnly = true) EnderDragonRenderState state) {
        CustomEntityModel customEntityModel = state.catharsis$getCustomEntityModel();

        if (customEntityModel == null) return;

        args.set(
            0,
            RenderType.entityCutoutNoCull(customEntityModel.getTexture())
        );
    }

    @ModifyArgs(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 1
        ),
        method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    public void catharsis$handleDragonTextureReplacementDecal(Args args, @Local(argsOnly = true) EnderDragonRenderState state) {
        CustomEntityModel customEntityModel = state.catharsis$getCustomEntityModel();

        if (customEntityModel == null) return;

        args.set(
            0,
            RenderType.entityDecal(customEntityModel.getTexture())
        );
    }
}
