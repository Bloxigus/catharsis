package me.owdding.catharsis.features.entity.conditions

import com.mojang.serialization.MapCodec
import me.owdding.catharsis.Catharsis
import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.catharsis.utils.codecs.IncludedCodecs
import me.owdding.ktcodecs.IncludedCodec
import net.minecraft.resources.Identifier
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.entity.Entity

interface EntityCondition {
    fun matches(entity: Entity): Boolean

    //noinspection unchecked
    fun codec(): MapCodec<out EntityCondition> = CatharsisCodecs.getMapCodec(this::class.java) as MapCodec<out EntityCondition>
}

object EntityConditions {
    val ID_MAPPER = ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<out EntityCondition>>()

    @IncludedCodec
    val CODEC: MapCodec<EntityCondition> = ID_MAPPER.codec(IncludedCodecs.catharsisIdentifier).dispatchMap(EntityCondition::codec) { it }

    init {
        ID_MAPPER.put(Catharsis.id("player"), CatharsisCodecs.getMapCodec<PlayerEntityCondition>())
        ID_MAPPER.put(Catharsis.id("identity"), CatharsisCodecs.getMapCodec<IdentityEntityCondition>())
        ID_MAPPER.put(Catharsis.id("attribute"), CatharsisCodecs.getMapCodec<AttributeEntityCondition>())
        ID_MAPPER.put(Catharsis.id("island"), CatharsisCodecs.getMapCodec<IslandEntityCondition>())
        ID_MAPPER.put(Catharsis.id("equipment"), EquipmentEntityCondition.CODEC)
    }
}
