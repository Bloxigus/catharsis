package me.owdding.catharsis.features.entity.conditions

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.owdding.catharsis.generated.CatharsisCodecs
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import tech.thatgravyboat.skyblockapi.helpers.McLevel

data class EquipmentEntityCondition(
    val slot: EquipmentSlot,
    val property: ConditionalItemModelProperty,
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        if (entity !is LivingEntity) return false

        val equipmentInSlot = entity.getItemBySlot(slot)

        return property.get(equipmentInSlot, McLevel.level, entity, 0, ItemDisplayContext.NONE)
    }

    override fun codec() = CODEC

    companion object {
        val CODEC: MapCodec<EquipmentEntityCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                CatharsisCodecs.getCodec<EquipmentSlot>().fieldOf("slot").forGetter(EquipmentEntityCondition::slot),
                ConditionalItemModelProperties.MAP_CODEC.forGetter(EquipmentEntityCondition::property),
            ).apply(it, ::EquipmentEntityCondition)
        }
    }
}
