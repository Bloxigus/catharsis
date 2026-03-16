package me.owdding.catharsis.mixins.items;

import me.owdding.catharsis.hooks.items.ItemStackRenderStateHook;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin implements ItemStackRenderStateHook {

    @Shadow private int activeLayerCount;
    @Shadow private ItemStackRenderState.LayerRenderState[] layers;
    @Unique private boolean catharsis$canFallthrough = true;

    @Inject(method = "clear", at = @At("HEAD"))
    private void catharsis$resetCanFallthrough(CallbackInfo ci) {
        this.catharsis$canFallthrough = true;
    }

    @Override
    public void catharsis$setCanFallthrough(boolean canFallthrough) {
        this.catharsis$canFallthrough = canFallthrough;
    }

    @Override
    public boolean catharsis$canFallthrough() {
        return this.catharsis$canFallthrough;
    }

    @Override
    public int catharsis$layerCount() {
        return this.activeLayerCount;
    }

    @Override
    public ItemStackRenderState.LayerRenderState[] catharsis$getLayers(int fromInclusive, int toExclusive) {
        if (fromInclusive < 0 || toExclusive > this.activeLayerCount || fromInclusive >= toExclusive) {
            return null;
        }
        if (this.layers == null || this.layers.length < toExclusive) {
            return null;
        }

        var result = new ItemStackRenderState.LayerRenderState[toExclusive - fromInclusive];
        System.arraycopy(this.layers, fromInclusive, result, 0, result.length);
        return result;
    }
}
