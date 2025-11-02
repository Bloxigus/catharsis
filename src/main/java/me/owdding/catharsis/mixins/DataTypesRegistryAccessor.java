package me.owdding.catharsis.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import tech.thatgravyboat.skyblockapi.api.datatype.DataType;
import tech.thatgravyboat.skyblockapi.impl.DataTypesRegistry;

import java.util.List;

@Mixin(DataTypesRegistry.class)
public interface DataTypesRegistryAccessor {

    @Accessor("types")
    public List<DataType<?>> catharsis$getDataTypes();

}
