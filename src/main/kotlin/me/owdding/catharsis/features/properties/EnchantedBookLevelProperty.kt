package me.owdding.catharsis.features.properties

import com.mojang.serialization.MapCodec
import me.owdding.catharsis.Catharsis
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty
import net.minecraft.world.entity.ItemOwner
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import tech.thatgravyboat.skyblockapi.api.datatype.getData

object EnchantedBookLevelProperty : RangeSelectItemModelProperty {

    val ID = Catharsis.id("enchanted_book_level")
    val CODEC = MapCodec.unit { EnchantedBookLevelProperty }

    override fun get(stack: ItemStack, level: ClientLevel?, owner: ItemOwner?, seed: Int): Float {
        return stack.getData(DataTypes.ENCHANTMENTS).takeIf { it?.size == 1 }?.values?.firstOrNull()?.toFloat() ?: 0f
    }

    override fun type(): MapCodec<out RangeSelectItemModelProperty> = CODEC
}
