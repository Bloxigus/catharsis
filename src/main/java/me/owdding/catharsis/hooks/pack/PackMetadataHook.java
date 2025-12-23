package me.owdding.catharsis.hooks.pack;

import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;

public interface PackMetadataHook extends PackEntryHook {

    default void catharsis$setMetadata(CatharsisMetadataSection metadata) {
        throw new UnsupportedOperationException();
    }
}
