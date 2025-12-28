package me.owdding.catharsis.features.entity.conditions

import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.entity.Entity
import java.util.UUID

@GenerateCodec
data class UuidEntityCondition(
    val uuid: UUID
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        return entity.uuid == uuid
    }

}
