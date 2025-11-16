package me.owdding.catharsis.features.pack.config

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import me.owdding.catharsis.utils.CatharsisLogger
import me.owdding.ktmodules.Module
import net.minecraft.util.GsonHelper
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.TimePassed
import tech.thatgravyboat.skyblockapi.api.events.time.TickEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.Scheduling
import tech.thatgravyboat.skyblockapi.utils.time.currentInstant
import tech.thatgravyboat.skyblockapi.utils.time.since
import java.util.concurrent.CompletableFuture
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.time.isDistantPast

data class PackConfig(
    val default: JsonObject = JsonObject(),
    val current: JsonObject = JsonObject(),
) {

    fun set(id: String, value: JsonElement) {
        current.add(id, value)
    }

    fun get(id: String): JsonElement? {
        return current.get(id) ?: default.get(id)
    }
}

@Module
object PackConfigHandler {

    private const val SAVE_PATH = "catharsis/pack_configs.json"

    private val logger = CatharsisLogger.named("PackConfigHandler")
    private val path = McClient.config.resolve(SAVE_PATH)
    private val configs = mutableMapOf<String, PackConfig>()
    private var saveRequestedAt = Instant.DISTANT_PAST

    init {
        logger.runCatching("Loading pack configurations") {
            if (path.notExists()) {
                path.parent?.createDirectories()
                path.createFile()
                logger.info("No existing config found")
                return@runCatching
            }
            val json = GsonHelper.parse(path.readText())
            for ((key, value) in json.entrySet()) {
                configs[key] = PackConfig(JsonObject(), value.asJsonObject)
            }
        }
    }

    fun getConfig(packId: String): PackConfig {
        return configs.getOrPut(packId) { PackConfig() }
    }

    fun save() {
        this.saveRequestedAt = currentInstant()
    }

    @Subscription(TickEvent::class)
    @TimePassed("10s")
    fun onTick() {
        if (!saveRequestedAt.isDistantPast && saveRequestedAt.since() >= 10.seconds) {
            val output = JsonObject()
            for ((key, value) in configs) {
                output.add(key, value.current.deepCopy())
            }

            Scheduling.async {
                logger.debug("Saving pack configurations to $SAVE_PATH")
                path.writeText(output.toString())
                saveRequestedAt = Instant.DISTANT_PAST
            }
        }
    }
}
