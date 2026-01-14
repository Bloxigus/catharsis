package me.owdding.catharsis.features.entity.conditions

import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import tech.thatgravyboat.skyblockapi.helpers.McLevel

@GenerateCodec
data class EquipmentEntityCondition(
    val slot: EquipmentSlot,
    val property: ConditionalItemModelProperty,
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        if (entity !is LivingEntity) return false

        val equipmentInSlot = entity.getItemBySlot(slot)

        return property.get(equipmentInSlot, McLevel.level, entity, 0, ItemDisplayContext.NONE)
    }

    override fun codec() = CatharsisCodecs.getMapCodec<EquipmentEntityCondition>()
}
