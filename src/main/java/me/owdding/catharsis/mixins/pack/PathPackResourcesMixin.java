package me.owdding.catharsis.mixins.pack;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.packs.PathPackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PathPackResources.class)
public class PathPackResourcesMixin {

    @ModifyExpressionValue(
        method = "isRegularFile",
        at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;IS_RUNNING_IN_IDE:Z")
    )
    private static boolean catharsis$hideDsStoreWarnings(boolean original) {
        return true;
    }

}
