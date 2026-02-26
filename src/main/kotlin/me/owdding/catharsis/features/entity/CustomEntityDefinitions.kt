package me.owdding.catharsis.features.entity

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import me.owdding.catharsis.Catharsis
import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktmodules.Module
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.Entity
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.json.Json.toDataOrThrow

@Module
object CustomEntityDefinitions : SimplePreparableReloadListener<Map<Identifier, CustomEntityDefinition>>() {

    private val logger = Catharsis.featureLogger("EntityDefinitions")
    private val converter = FileToIdConverter.json("catharsis/entity_definitions")
    private val gson = GsonBuilder().create()
    private val codec = CatharsisCodecs.getCodec<CustomEntityDefinition>()

    private val definitions = mutableMapOf<Identifier, CustomEntityDefinition>()

    override fun prepare(
        resourceManager: ResourceManager,
        profiler: ProfilerFiller,
    ): Map<Identifier, CustomEntityDefinition> {
        return converter.listMatchingResources(resourceManager)
            .mapNotNull { (file, resource) ->
                logger.runCatching("Error loading entity definition $file") {
                    resource.openAsReader().use { bufferedReader ->
                        val id = converter.fileToId(file)
                        val definition = gson.fromJson(bufferedReader, JsonElement::class.java).toDataOrThrow(codec)

                        id to definition
                    }
                }
            }
            .associate { it }
    }

    override fun apply(
        definitions: Map<Identifier, CustomEntityDefinition>,
        resourceManager: ResourceManager,
        profiler: ProfilerFiller,
    ) {
        this.definitions.clear()
        this.definitions.putAll(definitions)
    }

    @JvmStatic
    fun getFor(entity: Entity): Identifier? {
        for ((identifier, definition) in definitions) {
            if (definition.matches(entity)) return identifier
        }

        return null
    }

    init {
        McClient.registerClientReloadListener(Catharsis.id("entity_definitions"), this)
    }
}
