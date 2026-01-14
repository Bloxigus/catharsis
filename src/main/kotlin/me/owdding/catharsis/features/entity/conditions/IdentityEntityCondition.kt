package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import tech.thatgravyboat.skyblockapi.utils.extentions.cleanName
import java.util.UUID

@GenerateCodec
data class IdentityEntityCondition(
    @FieldName("entity_type") val entityType: EntityType<*>?,
    val uuid: UUID?,
    val name: String?
) : EntityCondition {

    override fun matches(entity: Entity): Boolean {
        if (entityType != null && entity.type != entityType) return false
        if (uuid != null && entity.uuid != uuid) return false
        if (name != null && entity.cleanName != name) return false

        return true
    }

    override fun codec() = CatharsisCodecs.getMapCodec<IdentityEntityCondition>()
}
