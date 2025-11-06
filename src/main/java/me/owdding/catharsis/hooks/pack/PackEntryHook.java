package me.owdding.catharsis.hooks.pack;

import me.owdding.catharsis.features.pack.meta.CatharsisMetadataSection;

public interface PackEntryHook {

    CatharsisMetadataSection catharsis$getMetadata();
}
