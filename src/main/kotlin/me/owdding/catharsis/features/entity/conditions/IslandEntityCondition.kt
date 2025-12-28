package me.owdding.catharsis.features.entity.conditions

import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldNames
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.entity.Entity
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@GenerateCodec
data class IslandEntityCondition(
    @FieldNames("islands", "island") @Compact val islands: List<SkyBlockIsland>,
) : EntityCondition {

    override fun matches(entity: Entity) = islands.any { it.inIsland() }
}
