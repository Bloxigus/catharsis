package me.owdding.catharsis.features.entity.models

import me.owdding.catharsis.utils.geometry.SafeModelPart
import me.owdding.catharsis.utils.TypedResourceManager
import me.owdding.catharsis.utils.extensions.unsafeCast
import me.owdding.catharsis.utils.geometry.BedrockGeometry
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.ktcodecs.NamedCodec
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.resources.Identifier

data class CustomEntityModel(
    val texture: Identifier,
    val emissiveTexture: Identifier?,
    val model: ModelPart?
) {

    private var cachedEntityModel: EntityModel<out EntityRenderState>? = null

    fun <T : EntityRenderState> replaceModel(oldModel: EntityModel<T>): EntityModel<T> {
        val newCustomEntityModelPart = model ?: return oldModel

        if (cachedEntityModel != null) {
            return (cachedEntityModel as EntityModel<*>).unsafeCast()
        }

        val modelConstructor = oldModel.javaClass.getConstructor(ModelPart::class.java)

        val newModel = modelConstructor.newInstance(newCustomEntityModelPart)

        cachedEntityModel = newModel

        return newModel
    }

    @GenerateCodec
    @NamedCodec("UnbakedCustomEntityModel")
    data class Unbaked(
        val texture: Identifier,
        @FieldName("emissive_texture") val emissiveTexture: Identifier?,
        val model: Identifier?
    ) {
        fun bake(resources: TypedResourceManager): CustomEntityModel {
            val bakedModel = if (model != null) {
                val bedrockModel = resources.getOrLoad(model, BedrockGeometry.RESOURCE_PARSER)!!.getOrThrow()

                SafeModelPart.convertFromBedrockModel(bedrockModel)
            } else null

            return CustomEntityModel(
                texture,
                emissiveTexture,
                bakedModel
            )
        }
    }
}
