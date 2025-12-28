package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.Catharsis
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldNames
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import tech.thatgravyboat.skyblockapi.utils.extentions.serverValue
import kotlin.jvm.optionals.getOrNull

@GenerateCodec
data class AttributeEntityCondition(
    val attribute: Identifier,
    @FieldNames("value", "values") @Compact val values: List<Float>,
) : EntityCondition {
    private val actualAttribute = BuiltInRegistries.ATTRIBUTE.get(attribute).getOrNull()

    init {
        if (actualAttribute == null) Catharsis.error("Unknown attribute $attribute")
    }

    override fun matches(entity: Entity): Boolean {
        if (entity !is LivingEntity) return false

        val attributeInstance = entity.getAttribute(actualAttribute ?: return false) ?: return false

        val attributeValue = attributeInstance.serverValue

        return values.any { it == attributeValue }
    }

//     override fun codec(): MapCodec<AttributeEntityCondition> = CatharsisCodecs.getMapCodec()
}
