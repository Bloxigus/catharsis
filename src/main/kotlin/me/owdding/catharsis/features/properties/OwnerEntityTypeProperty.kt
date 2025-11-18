package me.owdding.catharsis.features.properties

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import me.owdding.catharsis.Catharsis
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

object OwnerEntityTypeProperty : SelectItemModelProperty<EntityType<*>> {

    val ID = Catharsis.id("owner_entity_type")
    val TYPE: SelectItemModelProperty.Type<out SelectItemModelProperty<EntityType<*>>, EntityType<*>> = SelectItemModelProperty.Type.create(
        MapCodec.unit { OwnerEntityTypeProperty },
        EntityType.CODEC,
    )

    override fun get(
        stack: ItemStack,
        level: ClientLevel?,
        entity: LivingEntity?,
        seed: Int,
        displayContext: ItemDisplayContext,
    ): EntityType<*>? = entity?.type

    override fun valueCodec(): Codec<EntityType<*>> = EntityType.CODEC

    override fun type(): SelectItemModelProperty.Type<out SelectItemModelProperty<EntityType<*>>, EntityType<*>> = TYPE
}
