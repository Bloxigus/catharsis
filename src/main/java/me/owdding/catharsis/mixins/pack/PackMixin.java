package me.owdding.catharsis.mixins.pack;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;
import me.owdding.catharsis.hooks.pack.PackMetadataHook;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? = 1.21.8
/*import com.llamalad7.mixinextras.sugar.Local;*/

//? >= 1.21.9
import net.minecraft.server.packs.metadata.pack.PackFormat;

@Mixin(Pack.class)
public class PackMixin implements PackMetadataHook {

    @Shadow
    @Final
    private Pack.Metadata metadata;

    //? if >= 1.21.9 {
    @WrapOperation(
        method = "readMetaAndCreate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/repository/Pack;readPackMetadata(Lnet/minecraft/server/packs/PackLocationInfo;Lnet/minecraft/server/packs/repository/Pack$ResourcesSupplier;Lnet/minecraft/server/packs/metadata/pack/PackFormat;Lnet/minecraft/server/packs/PackType;)Lnet/minecraft/server/packs/repository/Pack$Metadata;"
        )
    )
    private static Pack.Metadata readCatharsisMetadata(PackLocationInfo info, Pack.ResourcesSupplier resources, PackFormat format, PackType type, Operation<Pack.Metadata> original) {
        var metadata = original.call(info, resources, format, type);
    //?} else {
    /*@WrapOperation(
        method = "readMetaAndCreate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/repository/Pack;readPackMetadata(Lnet/minecraft/server/packs/PackLocationInfo;Lnet/minecraft/server/packs/repository/Pack$ResourcesSupplier;I)Lnet/minecraft/server/packs/repository/Pack$Metadata;"
        )
    )
    private static Pack.Metadata readCatharsisMetadata(PackLocationInfo info, Pack.ResourcesSupplier resources, int config, Operation<Pack.Metadata> original, @Local(argsOnly = true) PackType type) {
        var metadata = original.call(info, resources, config);
    *///?}
        if (type == PackType.CLIENT_RESOURCES) catharsis$parseMetadata(metadata, resources, info);
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

    @Unique
    private static void catharsis$parseMetadata(Pack.Metadata metadata, Pack.ResourcesSupplier resources, PackLocationInfo info) {
        try (var sources = resources.openPrimary(info)) {
            var catharsisMetadata = sources.getMetadataSection(CatharsisMetadataSection.TYPE);
            //noinspection ConstantValue
            if ((Object) metadata instanceof PackMetadataHook hook) {
                hook.catharsis$setMetadata(catharsisMetadata);
            }
        } catch (Exception ignored) {
        }
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
