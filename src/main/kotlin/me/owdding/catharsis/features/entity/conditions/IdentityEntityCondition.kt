package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.entity.Entity
import tech.thatgravyboat.skyblockapi.utils.extentions.cleanName
import java.util.*

@GenerateCodec
data class IdentityEntityCondition(
    val uuid: UUID?,
    val name: String?
) : EntityCondition {

    override fun matches(entity: Entity): Boolean {
        if (uuid != null && entity.uuid != uuid) return false
        if (name != null && entity.cleanName != name) return false

        return true
    }

    override fun codec() = CatharsisCodecs.getMapCodec<IdentityEntityCondition>()
}
