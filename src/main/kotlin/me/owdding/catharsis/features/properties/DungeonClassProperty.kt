package me.owdding.catharsis.features.properties

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import me.owdding.catharsis.Catharsis
import me.owdding.catharsis.generated.CatharsisCodecs
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.area.dungeon.DungeonAPI
import tech.thatgravyboat.skyblockapi.api.area.dungeon.DungeonClass
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

object DungeonClassProperty : SelectItemModelProperty<DungeonClass> {

    val ID = Catharsis.id("dungeon_class")
    val TYPE: SelectItemModelProperty.Type<out SelectItemModelProperty<DungeonClass>, DungeonClass> = SelectItemModelProperty.Type.create(
        MapCodec.unit { DungeonClassProperty },
        CatharsisCodecs.getCodec()
    )

    override fun get(
        stack: ItemStack,
        level: ClientLevel?,
        entity: LivingEntity?,
        seed: Int,
        displayContext: ItemDisplayContext,
    ): DungeonClass? = DungeonAPI.teammates.find { it.name == entity?.name?.stripped }?.dungeonClass

    override fun valueCodec(): Codec<DungeonClass> = CatharsisCodecs.getCodec()

    override fun type(): SelectItemModelProperty.Type<out SelectItemModelProperty<DungeonClass>, DungeonClass> = TYPE
}
