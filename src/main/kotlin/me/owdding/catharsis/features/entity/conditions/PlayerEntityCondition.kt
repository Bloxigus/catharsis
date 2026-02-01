package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldNames
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.client.entity.ClientAvatarEntity
import net.minecraft.core.ClientAsset
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import tech.thatgravyboat.skyblockapi.utils.extentions.isRealPlayer


sealed interface PlayerEntityConditions : EntityCondition {
    @GenerateCodec
    data class NpcSkin(
        @FieldNames("skins", "skin") @Compact val skins: Set<String>
    ) : PlayerEntityConditions {
        override fun matches(entity: Entity): Boolean {
            if (entity !is ClientAvatarEntity) return false
            if (entity is Player && entity.isRealPlayer()) return false

            val skin = getSkin(entity) ?: return false

            return skin in skins
        }

        override fun codec() = CatharsisCodecs.getMapCodec<NpcSkin>()
    }

    @GenerateCodec
    data class PlayerSkin(
        @FieldNames("skins", "skin") @Compact val skins: Set<String>
    ) : PlayerEntityConditions {
        override fun matches(entity: Entity): Boolean {
            if (entity !is ClientAvatarEntity) return false
            if (entity !is Player || !entity.isRealPlayer()) return false

            val skin = getSkin(entity) ?: return false

            return skin in skins
        }

        override fun codec() = CatharsisCodecs.getMapCodec<PlayerSkin>()
    }

    fun getSkin(entity: ClientAvatarEntity): String? {
        //? if > 1.21.8 {
        val bodySkin = entity.skin.body
        if (bodySkin !is ClientAsset.DownloadedTexture) return null
        val skinUrl = bodySkin.url
        //?} else {
        /*val skinUrl = entity.skin.textureUrl
        *///?}
        return skinUrl
    }
}
