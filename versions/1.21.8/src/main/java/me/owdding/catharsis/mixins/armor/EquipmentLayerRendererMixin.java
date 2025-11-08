package me.owdding.catharsis.mixins.armor;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.owdding.catharsis.features.armor.models.ArmorModelState;
import me.owdding.catharsis.hooks.armor.HumanoidRenderStateHook;
import me.owdding.catharsis.hooks.armor.LivingEntityRenderStateHook;
import net.minecraft.Optionull;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerRendererMixin {

    @Inject(
        method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/DyedItemColor;getOrDefault(Lnet/minecraft/world/item/ItemStack;I)I")
    )
    private void catharsis$modifyColorForLayer(
        CallbackInfo ci,
        @Local(argsOnly = true) ItemStack stack,
        @Local(ordinal = 0) LocalRef<List<EquipmentClientInfo.Layer>> layers,
        @Share("texture") LocalRef<ArmorModelState.@Nullable Texture> stateRef
    ) {
        var texture = catharsis$getTextureForSlot(HumanoidRenderStateHook.CURRENT_RENDER_STATE.get(), stack);
        stateRef.set(texture);

        if (texture != null) {
            layers.set(List.of());
        }
    }

    @Inject(
        method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;TRIM:Lnet/minecraft/core/component/DataComponentType;")
    )
    private void catharsis$modifyColorForLayer(
        CallbackInfo ci,

        @Local(argsOnly = true) Model model,
        @Local(argsOnly = true) PoseStack stack,
        @Local(argsOnly = true, ordinal = 0) int light,

        @Local(argsOnly = true) ItemStack item,
        @Local(argsOnly = true) MultiBufferSource buffers,
        @Share("texture") LocalRef<ArmorModelState.@Nullable Texture> stateRef
    ) {
        var state = stateRef.get();
        if (state != null) {
            var applyGlint = item.hasFoil();

            var textures = state.getTextures();
            var colors = state.getColors();

            for (int i = 0; i < state.getLayers(); i++) {
                var texture = RenderType.armorCutoutNoCull(textures[i]);
                var color = ARGB.opaque(colors[i]);

                VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffers, texture, applyGlint);
                model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, color);

                applyGlint = false;
            }
        }
    }

    @Unique
    private static ArmorModelState.Texture catharsis$getTextureForSlot(@Nullable Object state, ItemStack stack) {
        if (state instanceof LivingEntityRenderStateHook hook) {
            var slot = Optionull.map(stack.get(DataComponents.EQUIPPABLE), Equippable::slot);
            if (slot == null) return null;

            var renderer = hook.catharsis$getArmorDefinitionRenderState().fromSlot(slot);
            return renderer instanceof ArmorModelState.Texture texture ? texture : null;
        }
        return null;
    }
}
