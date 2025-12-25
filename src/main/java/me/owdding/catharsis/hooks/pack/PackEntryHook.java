package me.owdding.catharsis.hooks.pack;

import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;

public interface PackEntryHook {

    default CatharsisMetadataSection catharsis$getMetadata() {
        throw new UnsupportedOperationException();
    }
}
