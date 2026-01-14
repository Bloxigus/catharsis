package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.FieldNames
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.client.entity.ClientAvatarEntity
import net.minecraft.core.ClientAsset
import net.minecraft.world.entity.Entity

@GenerateCodec
data class PlayerEntityCondition(
    @FieldNames("skins", "skin") @Compact val skins: List<String>?,
    @FieldName("only_npc") val onlyNpc: Boolean = false,
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        if (onlyNpc && entity.uuid.version() == 4) return false

        if (skins == null) return true

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

    override fun codec() = CatharsisCodecs.getMapCodec<PlayerEntityCondition>()
}
