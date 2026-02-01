package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.entity.Entity

@GenerateCodec
data class AnyEntityCondition(
    val conditions: List<EntityCondition>
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        return conditions.any { it.matches(entity) }
    }

    override fun codec() = CatharsisCodecs.getMapCodec<AnyEntityCondition>()
}
