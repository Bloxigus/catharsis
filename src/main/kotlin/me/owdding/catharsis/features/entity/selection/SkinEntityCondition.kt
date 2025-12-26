package me.owdding.catharsis.features.entity.selection

import com.mojang.serialization.MapCodec
import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldNames
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.client.entity.ClientAvatarEntity
import net.minecraft.core.ClientAsset
import net.minecraft.world.entity.Entity

@GenerateCodec
data class SkinEntityCondition(
    @FieldNames("skin", "skins") @Compact val skins: List<String>,
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        if (entity !is ClientAvatarEntity) return false

        val bodySkin = entity.skin.body

        if (bodySkin !is ClientAsset.DownloadedTexture) return false

        return skins.any { it == bodySkin.url }
    }

    override fun codec(): MapCodec<SkinEntityCondition> = CatharsisCodecs.getMapCodec()
}
