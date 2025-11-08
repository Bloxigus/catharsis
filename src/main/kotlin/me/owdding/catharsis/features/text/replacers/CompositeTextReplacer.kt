package me.owdding.catharsis.features.text.replacers

import com.mojang.serialization.MapCodec
import me.owdding.catharsis.generated.CatharsisCodecs
import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.network.chat.Component

@GenerateCodec
data class CompositeTextReplacer(
    val replacers: List<TextReplacer>,
    val propagate: Boolean = true,
) : TextReplacer {

    override val codec: MapCodec<out TextReplacer> = CatharsisCodecs.getMapCodec<CompositeTextReplacer>()

    override fun replace(text: Component): ReplacementResult {
        var text = text

        for (replacer in replacers) {
            val result = replacer.replace(text)
            text = result.text
            if (result is ReplacementResult.Break) {
                if (!this.propagate) {
                    return ReplacementResult.Break(text)
                }
                break
            }
        }

        return ReplacementResult.Continue(text)
    }

}
