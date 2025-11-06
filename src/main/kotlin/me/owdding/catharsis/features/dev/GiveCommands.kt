package me.owdding.catharsis.features.dev

import com.google.gson.JsonElement
import com.mojang.brigadier.arguments.StringArgumentType
import me.owdding.catharsis.utils.extensions.sendWithPrefix
import me.owdding.catharsis.utils.types.colors.CatppuccinColors
import me.owdding.catharsis.utils.types.commands.SkyBlockIdArgument
import me.owdding.catharsis.utils.types.suggestion.IterableSuggestionProvider
import me.owdding.ktmodules.Module
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent.Companion.argument
import tech.thatgravyboat.skyblockapi.api.remote.api.SimpleItemAPI
import tech.thatgravyboat.skyblockapi.api.remote.api.SkyBlockId
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McPlayer
import tech.thatgravyboat.skyblockapi.utils.extentions.getSkyBlockId
import tech.thatgravyboat.skyblockapi.utils.json.Json.readJson
import tech.thatgravyboat.skyblockapi.utils.json.Json.toData
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@Module
object GiveCommands {

    @Subscription
    private fun RegisterCommandsEvent.onRegister() {
        register("catharsis dev give") {
            callback {
                val item = McClient.clipboard.readJson<JsonElement>().toData(ItemStack.CODEC)
                if (item == null) {
                    Text.of("Failed to read item from clipboard!", CatppuccinColors.Mocha.red).sendWithPrefix("catharsis-dev-give-failed-decode")
                    return@callback
                }
                tryGive(item)
            }

            val allIds = SimpleItemAPI.getAllIds()
            thenCallback("id id", SkyBlockIdArgument(allIds)) {
                val id = argument<SkyBlockId>("id")
                if (!allIds.contains(id)) {
                    Text.of("Unable to find item with id ") {
                        color = CatppuccinColors.Mocha.red
                        append(id.cleanId, CatppuccinColors.Mocha.peach)
                        append("!")
                    }.sendWithPrefix("catharsis-dev-give-not-found")
                    return@thenCallback
                }
                tryGive(id.toItem())
            }
            thenCallback("name name", StringArgumentType.greedyString(), IterableSuggestionProvider(SimpleItemAPI.getAllNames())) {
                val name = argument<String>("name")
                val id = SimpleItemAPI.findIdByName(name)
                if (id == null) {
                    Text.of("Unable to find item for name ") {
                        color = CatppuccinColors.Mocha.red
                        append(name, CatppuccinColors.Mocha.peach)
                        append("!")
                    }.sendWithPrefix("catharsis-dev-give-not-found")
                    return@thenCallback
                }
                tryGive(id.toItem())
            }
        }
    }

    fun tryGive(itemStack: ItemStack) {
        val item = itemStack.copyWithCount(1)
        if (McPlayer.self?.gameMode()?.isCreative != true && McClient.self.isSingleplayer) {
            Text.of("Not in singleplayer and creative!", CatppuccinColors.Mocha.red).sendWithPrefix("catharsis-dev-give-singleplayer")
            return
        }
        Text.of("Added ") {
            append(item.hoverName) {
                color = CatppuccinColors.Mocha.peach
            }
            append(" to your inventory!")
            color = CatppuccinColors.Frappe.green
        }.sendWithPrefix("catharsis-dev-give-added-${item.getSkyBlockId()}")
        McClient.self.player?.inventory?.add(item)
    }

}
