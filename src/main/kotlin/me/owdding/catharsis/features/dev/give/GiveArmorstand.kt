package me.owdding.catharsis.features.dev.give

//? if > 1.21.8 {
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.TypedEntityData
import java.util.*
//?} else {
/*import net.minecraft.world.item.component.CustomData
*///?}

import me.owdding.catharsis.features.dev.GiveCommands
import me.owdding.catharsis.utils.types.commands.SkyBlockIdArgument
import me.owdding.ktmodules.Module
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent.Companion.argument
import tech.thatgravyboat.skyblockapi.api.remote.RepoItemsAPI
import tech.thatgravyboat.skyblockapi.api.remote.api.SimpleItemAPI
import tech.thatgravyboat.skyblockapi.api.remote.api.SkyBlockId
import tech.thatgravyboat.skyblockapi.api.remote.hypixel.museum.MuseumData
import tech.thatgravyboat.skyblockapi.utils.extentions.putCompound
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.api.events.misc.LiteralCommandBuilder
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.italic
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import net.minecraft.world.item.component.ResolvableProfile
import tech.thatgravyboat.skyblockapi.utils.extentions.get

@Module
object GiveArmorstand {

    private val regex = "(?i)_(?:HELMET|MASK|CHESTPLATE|LEGGINGS|PANTS|BOOTS)$".toRegex()

    @Subscription
    private fun RegisterCommandsEvent.onRegister() {
        register("catharsis dev give") {
            createGive("armorstand") { tag, skyBlockId ->
                Items.ARMOR_STAND.defaultInstance.apply {
                    set(
                        DataComponents.ENTITY_DATA,
                        //? if >1.21.8 {
                        TypedEntityData.of(EntityType.ARMOR_STAND, tag),
                        //?} else {
                        /*CustomData.of(tag.apply {
                            putString("id", "minecraft:armor_stand")
                        })
                        *///?}
                    )
                    set(DataComponents.CUSTOM_NAME, Text.of(skyBlockId) { italic = false })
                }
            }

            //? if > 1.21.8 {
            createGive("mannequin") { tag, skyBlockId ->
                Items.FOX_SPAWN_EGG.defaultInstance.apply {
                    set(DataComponents.ENTITY_DATA, TypedEntityData.of(EntityType.MANNEQUIN, tag))
                    set(DataComponents.CUSTOM_NAME, Text.of(skyBlockId) { italic = false })
                    set(DataComponents.PROFILE, ResolvableProfile.createUnresolved(UUID.fromString("16102479-7162-4ea9-9975-a5059c6a2be3")))
                }
            }
            //?}
        }
    }

    private val ignoredCategories = mutableSetOf(
        "necklace",
        "cloak",
        "belt",
        "gloves",
        "bracelet",
    )

    private fun LiteralCommandBuilder.createGive(name: String, itemConstructor: (CompoundTag, String) -> ItemStack) {
        val allIds = SimpleItemAPI.getAllIds()
        thenCallback("$name id", SkyBlockIdArgument(allIds)) {
            val id = argument<SkyBlockId>("id")
            val skyBlockId = id.skyblockId

            val items = (MuseumData.museumData.armorSets.entries.find { skyBlockId in it.value }?.value?.map { RepoItemsAPI.getItem(it) } ?: run {
                // If museum data is not found, try to find items by matching stripped IDs
                val shortenedId = skyBlockId.replace(regex, "")
                allIds.filter { it.skyblockId.replace(regex, "") == shortenedId }.map { it.toItem() }
            }).filterNot { it[DataTypes.CATEGORY]?.name in ignoredCategories }.associateBy { it.toSlotType() }

            val itemCompound = CompoundTag().apply {
                putCompound("equipment") {
                    put("head", (items[EquipmentSlot.HEAD] ?: ItemStack.EMPTY).toNBT())
                    put("chest", (items[EquipmentSlot.CHEST] ?: ItemStack.EMPTY).toNBT())
                    put("legs", (items[EquipmentSlot.LEGS] ?: ItemStack.EMPTY).toNBT())
                    put("feet", (items[EquipmentSlot.FEET] ?: ItemStack.EMPTY).toNBT())
                }
            }

            GiveCommands.tryGive(itemConstructor(itemCompound, skyBlockId))
        }
    }

    private fun ItemStack.toSlotType(): EquipmentSlot = this.get(DataComponents.EQUIPPABLE)?.slot ?: EquipmentSlot.HEAD
    private fun ItemStack.toNBT() = ItemStack.OPTIONAL_CODEC.encodeStart(NbtOps.INSTANCE, this).orThrow
}
