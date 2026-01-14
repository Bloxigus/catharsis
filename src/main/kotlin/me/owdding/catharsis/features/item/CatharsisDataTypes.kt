package me.owdding.catharsis.features.item

import me.owdding.catharsis.PreLoadModule
import tech.thatgravyboat.skyblockapi.api.datatype.DataType
import tech.thatgravyboat.skyblockapi.api.datatype.defaults.GenericDataTypes
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@PreLoadModule
object CatharsisDataTypes {

    val HAS_SKIN_FALLBACK = DataType.of("has_skin_fallback") {
        GenericDataTypes.HELMET_SKIN.factory(it) != null || it.hoverName.stripped.endsWith("✦")
    }

    val HAS_DYE_FALLBACK = DataType.of("has_dye_fallback") {
        GenericDataTypes.APPLIED_DYE.factory(it) != null || it.hoverName.stripped.startsWith("✿")
    }
    
}
