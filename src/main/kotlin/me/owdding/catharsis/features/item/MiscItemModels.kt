package me.owdding.catharsis.features.item

import com.google.gson.JsonObject
import me.owdding.catharsis.events.FinishRepoLoadEvent
import me.owdding.catharsis.events.StartRepoLoadEvent
import me.owdding.catharsis.repo.CatharsisRemoteRepo
import me.owdding.ktmodules.Module
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.utils.extentions.asString

@Module
object MiscItemModels {

    private val skinToModel: MutableMap<String, Identifier> = mutableMapOf()

    @JvmStatic
    fun getModel(stack: ItemStack): Identifier? {
        val properties = stack.get(DataComponents.PROFILE)?.partialProfile()?.properties() ?: return null
        val textures = properties.get("textures").firstOrNull() ?: return null
        val skin = textures.value() ?: return null
        return skinToModel[skin]
    }

    @Subscription
    private fun StartRepoLoadEvent.start() {
        skinToModel.clear()
    }

    @Subscription
    private fun FinishRepoLoadEvent.finish() {
        val json = CatharsisRemoteRepo.getFileContentAsJson("misc_items.json") as? JsonObject ?: return
        for (entry in json.entrySet()) {
            val skin = entry.key
            val model = entry.value.asString() ?: continue
            val identifier = Identifier.tryParse(model) ?: continue

            skinToModel[skin] = identifier
        }
    }
}
