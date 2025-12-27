package me.owdding.catharsis.features.entity

import me.owdding.catharsis.features.entity.models.SafeModelPart
import me.owdding.catharsis.utils.geometry.BedrockGeometry
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.resources.Identifier

@GenerateCodec
data class CustomEntityModel(
    val texture: Identifier,
    @FieldName("emissive_texture") val emissiveTexture: Identifier?,
    val model: BedrockGeometry?
) {
    private var cachedModel: ModelPart? = null

    fun getModelPart(): ModelPart? {
        if (model == null) return null
        if (cachedModel != null) return cachedModel

        val modelPart = SafeModelPart.convertFromBedrockModel(model)

        cachedModel = modelPart

        return modelPart
    }
}
