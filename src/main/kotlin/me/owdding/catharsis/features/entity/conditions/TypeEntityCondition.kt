package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.Catharsis
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import kotlin.jvm.optionals.getOrNull

@GenerateCodec
data class TypeEntityCondition(
    @FieldName("entity_type") val entityType: Identifier,
) : EntityCondition {
    private val type = BuiltInRegistries.ENTITY_TYPE.get(entityType).getOrNull()?.value()

    init {
        if (type == null) Catharsis.error("Unknown entity $entityType")
    }

    override fun matches(entity: Entity) = entity.type == type
}
