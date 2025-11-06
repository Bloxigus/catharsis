package me.owdding.catharsis.hooks.pack;

import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;

public interface PackMetadataHook extends PackEntryHook {

    void catharsis$setMetadata(CatharsisMetadataSection metadata);
}
