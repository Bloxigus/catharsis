package me.owdding.catharsis.features.entity

import me.owdding.catharsis.features.entity.selection.EntityCondition
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity


@GenerateCodec
data class CustomEntityDefinition(
    val conditions: List<EntityCondition>,
    val replacement: Identifier
) {
    fun matches(entity: Entity): Boolean {
        if (conditions.isEmpty()) return false

        for (condition in conditions) {
            if (!condition.matches(entity)) return false
        }
        return true
    }
}
