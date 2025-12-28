package me.owdding.catharsis.features.entity.conditions

import com.mojang.serialization.MapCodec
import net.minecraft.world.entity.Entity

object NotRealPlayerCondition : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        return entity.uuid.version() != 4
    }

    override fun codec(): MapCodec<NotRealPlayerCondition> = MapCodec.unit(this)
}
