package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.Catharsis
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import tech.thatgravyboat.skyblockapi.utils.extentions.cleanName
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@GenerateCodec
data class IdentityEntityCondition(
    @FieldName("entity_type") val entityType: Identifier?,
    val uuid: UUID?,
    val name: String?
) : EntityCondition {
    private val type = BuiltInRegistries.ENTITY_TYPE.get(entityType).getOrNull()?.value()

    init {
        if (type == null) Catharsis.error("Unknown entity $entityType")
    }

    override fun matches(entity: Entity): Boolean {
        if (type != null && entity.type != type) return false
        if (uuid != null && entity.uuid != uuid) return false
        if (name != null && entity.cleanName != name) return false

        return true
    }
}
