package me.owdding.catharsis.features.entity.conditions

import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldNames
import me.owdding.ktcodecs.GenerateCodec
//? if > 1.21.8 {
import net.minecraft.client.entity.ClientAvatarEntity
//?} else {
/*import net.minecraft.client.player.AbstractClientPlayer as ClientAvatarEntity
*///?}
import net.minecraft.core.ClientAsset
import net.minecraft.world.entity.Entity

@GenerateCodec
data class SkinEntityCondition(
    @FieldNames("skin", "skins") @Compact val skins: List<String>,
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        if (entity !is ClientAvatarEntity) return false

        //? if > 1.21.8 {
        val bodySkin = entity.skin.body
        if (bodySkin !is ClientAsset.DownloadedTexture) return false
        val skinUrl = bodySkin.url
        //?} else {
        /*val skinUrl = entity.skin.textureUrl
        *///?}



        return skins.any { it == skinUrl }
    }
}
