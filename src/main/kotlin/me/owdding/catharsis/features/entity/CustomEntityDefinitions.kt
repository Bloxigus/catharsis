package me.owdding.catharsis.features.entity

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import me.owdding.catharsis.Catharsis
import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktmodules.Module
import net.minecraft.resources.FileToIdConverter
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.Entity
import tech.thatgravyboat.skyblockapi.helpers.McClient

@Module
object CustomEntityDefinitions : SimplePreparableReloadListener<List<CustomEntityDefinition>>() {
    private val converter = FileToIdConverter.json("catharsis/entity_definitions")
    private val gson = GsonBuilder().create()
    private val codec = CatharsisCodecs.getCodec<CustomEntityDefinition>()

    private val definitions = mutableListOf<CustomEntityDefinition>()

    override fun prepare(
        resourceManager: ResourceManager,
        profiler: ProfilerFiller,
    ): List<CustomEntityDefinition> {
        return converter.listMatchingResources(resourceManager)
            .mapNotNull { (_, resource) ->
                resource.openAsReader().use { bufferedReader ->
                    codec.parse(JsonOps.INSTANCE, gson.fromJson(bufferedReader, JsonElement::class.java)).orThrow
                }
            }
    }

    override fun apply(
        definitions: List<CustomEntityDefinition>,
        resourceManager: ResourceManager,
        profiler: ProfilerFiller,
    ) {
        this.definitions.clear()
        this.definitions.addAll(definitions)
    }

    @JvmStatic
    fun getFor(entity: Entity): CustomEntityDefinition? {
        for (definition in definitions) {
            if (definition.matches(entity)) return definition
        }

        return null
    }

    init {
        McClient.registerClientReloadListener(Catharsis.id("custom_entity_definitions"), this)
    }
}
