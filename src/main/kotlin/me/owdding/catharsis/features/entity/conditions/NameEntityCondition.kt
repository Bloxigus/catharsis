package me.owdding.catharsis.features.entity.conditions

import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.entity.Entity
import tech.thatgravyboat.skyblockapi.utils.extentions.cleanName

@GenerateCodec
data class NameEntityCondition(
    val name: String
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        return entity.cleanName == name
    }
}
