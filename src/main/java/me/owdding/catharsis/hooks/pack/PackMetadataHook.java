package me.owdding.catharsis.hooks.pack;

import me.owdding.catharsis.features.pack.config.PackConfigOption;
import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;

import java.util.List;

public interface PackMetadataHook extends PackEntryHook {

    default void catharsis$setMetadata(CatharsisMetadataSection metadata) {
        throw new UnsupportedOperationException();
    }

    default void catharsis$setConfig(List<PackConfigOption> config) {
        throw new UnsupportedOperationException();
    }
}
