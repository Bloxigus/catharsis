package me.owdding.catharsis.mixins.pack;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import kotlin.Pair;
import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;
import me.owdding.catharsis.hooks.pack.PackEntryHook;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(TransferableSelectionList.PackEntry.class)
public abstract class TransferableSelectionListPackEntryMixin extends ObjectSelectionList.Entry<TransferableSelectionList.Entry> {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    @Final
    private PackSelectionModel.Entry pack;

    @Shadow
    private static MultiLineLabel cacheDescription(Minecraft minecraft, Component text) {
        return null;
    }

    @Shadow
    private static FormattedCharSequence cacheName(Minecraft minecraft, Component name) {
        return null;
    }

    @Unique
    private MultiLineLabel catharsis$incompatibleModDescriptionDisplayCache;

    @Unique
    private FormattedCharSequence catharsis$incompatibleModNameDisplayCache;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(TransferableSelectionList transferableSelectionList, Minecraft minecraft, TransferableSelectionList parent, PackSelectionModel.Entry entry, CallbackInfo ci) {
        this.catharsis$incompatibleModNameDisplayCache = cacheName(minecraft, Component.translatable("pack.catharsis.incompatible.title"));
        this.catharsis$incompatibleModDescriptionDisplayCache = cacheDescription(minecraft, Component.translatable("pack.catharsis.incompatible.desc"));
    }

    @Definition(id = "incompatibleDescriptionDisplayCache", field = "Lnet/minecraft/client/gui/screens/packs/TransferableSelectionList$PackEntry;incompatibleDescriptionDisplayCache:Lnet/minecraft/client/gui/components/MultiLineLabel;")
    @Expression("? = ?.incompatibleDescriptionDisplayCache")
    @Inject(
        method = "renderContent",
        at = @At(
            value = "MIXINEXTRAS:EXPRESSION",
            shift = At.Shift.AFTER
        )
    )
    private void renderDescription(
        CallbackInfo ci,
        @Local(ordinal = 0, argsOnly = true) GuiGraphics graphics,
        @Local(ordinal = 0, argsOnly = true) int mouseX,
        @Local(ordinal = 1, argsOnly = true) int mouseY,
        @Local(ordinal = 0) LocalRef<FormattedCharSequence> nameDisplayCache,
        @Local(ordinal = 0) LocalRef<MultiLineLabel> descriptionDisplayCache
    ) {
        var meta = catharsis$getMeta();
        List<Pair<String, ModContainer>> incompatibilities = meta != null ? meta.getIncompatibilities() : List.of();
        if (!incompatibilities.isEmpty()) {
            nameDisplayCache.set(this.catharsis$incompatibleModNameDisplayCache);
            descriptionDisplayCache.set(this.catharsis$incompatibleModDescriptionDisplayCache);
            graphics.setTooltipForNextFrame(this.minecraft.font, meta.getIncompatibleTooltip(), Optional.empty(), mouseX, mouseY);
        }
    }

    @Unique
    private CatharsisMetadataSection catharsis$getMeta() {
        if (this.pack instanceof PackEntryHook hook) {
            return hook.catharsis$getMetadata();
        }
        return null;
    }
}
