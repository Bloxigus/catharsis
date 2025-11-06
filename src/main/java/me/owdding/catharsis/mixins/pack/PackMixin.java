package me.owdding.catharsis.mixins.pack;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;
import me.owdding.catharsis.hooks.pack.PackMetadataHook;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Pack.class)
public class PackMixin implements PackMetadataHook {

    @Shadow
    @Final
    private Pack.Metadata metadata;

    @WrapOperation(
        method = "readPackMetadata",
        at = @At(
            value = "NEW",
            target = "Lnet/minecraft/server/packs/repository/Pack$Metadata;"
        )
    )
    private static Pack.Metadata meow(
        Component description,
        PackCompatibility compatibility,
        FeatureFlagSet features,
        List<String> overlays,
        Operation<Pack.Metadata> original,
        @Local(argsOnly = true) PackLocationInfo info,
        @Local(argsOnly = true) PackType type,
        @Local PackResources resources
    ) {
        var metadata = original.call(description, compatibility, features, overlays);
        if (type == PackType.CLIENT_RESOURCES) {
            try {
                var catharsisMetadata = resources.getMetadataSection(CatharsisMetadataSection.TYPE);
                //noinspection ConstantValue
                if ((Object) metadata instanceof PackMetadataHook hook) {
                    hook.catharsis$setMetadata(catharsisMetadata);
                }
            } catch (Exception ignored) {
            }
        }
        return metadata;
    }

    @Override
    public void catharsis$setMetadata(CatharsisMetadataSection metadata) {
        ((PackMetadataHook) (Object) this.metadata).catharsis$setMetadata(metadata);
    }

    @Override
    public CatharsisMetadataSection catharsis$getMetadata() {
        return ((PackMetadataHook) (Object) this.metadata).catharsis$getMetadata();
    }

    @Mixin(Pack.Metadata.class)
    public static class MetadataMixin implements PackMetadataHook {

        @Unique
        private CatharsisMetadataSection catharsis$metadata;

        @Override
        public void catharsis$setMetadata(CatharsisMetadataSection metadata) {
            this.catharsis$metadata = metadata;
        }

        @Override
        public CatharsisMetadataSection catharsis$getMetadata() {
            return this.catharsis$metadata;
        }
    }
}
