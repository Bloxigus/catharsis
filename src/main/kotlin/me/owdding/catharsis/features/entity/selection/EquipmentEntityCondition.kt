package me.owdding.catharsis.features.entity.selection

import com.mojang.serialization.MapCodec
import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity

@GenerateCodec
data class EquipmentEntityCondition(
    val slot: EquipmentSlot,
) : EntityCondition {
    override fun matches(entity: Entity): Boolean {
        if (entity !is LivingEntity) return false

        val equipmentInSlot = entity.getItemBySlot(slot)

        //TODO: implement
        return true
    }

    override fun codec(): MapCodec<SkinEntityCondition> = CatharsisCodecs.getMapCodec()
}
