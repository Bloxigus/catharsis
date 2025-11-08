package me.owdding.catharsis.features.text

import me.owdding.catharsis.features.text.replacers.TextReplacer
import me.owdding.ktcodecs.GenerateCodec

@GenerateCodec
data class TextReplacement(
    val priority: Int = 0,
    val replacer: TextReplacer,
)
