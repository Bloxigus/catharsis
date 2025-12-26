package me.owdding.catharsis.features.entity

import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.resources.Identifier

@GenerateCodec
data class CustomEntityModel(
    val texture: Identifier
)
