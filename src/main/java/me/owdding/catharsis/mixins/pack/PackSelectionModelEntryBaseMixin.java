package me.owdding.catharsis.mixins.pack;

import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;
import me.owdding.catharsis.hooks.pack.PackEntryHook;
import me.owdding.catharsis.hooks.pack.PackMetadataHook;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PackSelectionModel.EntryBase.class)
public class PackSelectionModelEntryBaseMixin implements PackEntryHook {

    @Shadow
    @Final
    private Pack pack;

    @Override
    public CatharsisMetadataSection catharsis$getMetadata() {
        if (this.pack instanceof PackMetadataHook hook) {
            return hook.catharsis$getMetadata();
        }
        return null;
    }
}
