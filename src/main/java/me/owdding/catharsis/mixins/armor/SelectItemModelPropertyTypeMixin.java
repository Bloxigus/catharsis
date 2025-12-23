package me.owdding.catharsis.mixins.armor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.owdding.catharsis.features.armor.models.SelectArmorModel;
import me.owdding.catharsis.hooks.armor.SelectItemModelPropertyTypeHook;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SelectItemModelProperty.Type.class)
public class SelectItemModelPropertyTypeMixin<P extends SelectItemModelProperty<T>, T> implements SelectItemModelPropertyTypeHook<P, T> {

    @Unique
    private MapCodec<SelectArmorModel.UnbakedSwitch<P, T>> catharsis$armorSwitchCodec;

    @Override
    public MapCodec<SelectArmorModel.UnbakedSwitch<P, T>> catharsis$getArmorSwitchCodec() {
        return this.catharsis$armorSwitchCodec;
    }

    @Override
    public void catharsis$setArmorSwitchCodec(MapCodec<SelectArmorModel.UnbakedSwitch<P, T>> codec) {
        this.catharsis$armorSwitchCodec = codec;
    }

    @ModifyReturnValue(method = "create", at = @At("RETURN"))
    private static <P extends SelectItemModelProperty<T>, T> SelectItemModelProperty.Type<P, T> create(
        SelectItemModelProperty.Type<P, T> original,
        @Local(argsOnly = true, ordinal = 0) MapCodec<P> propertyCodec,
        @Local(argsOnly = true, ordinal = 0) Codec<T> valueCodec
    ) {
        var casesCodec = SelectArmorModel.UnbakedSwitch.createCasesFieldCodec(valueCodec);
        MapCodec<SelectArmorModel.UnbakedSwitch<P, T>> codec = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    propertyCodec.forGetter(SelectArmorModel.UnbakedSwitch::getProperty),
                    casesCodec.forGetter(SelectArmorModel.UnbakedSwitch::getCases)
                )
                .apply(instance, SelectArmorModel.UnbakedSwitch::new)
        );

        //noinspection unchecked
        original.catharsis$setArmorSwitchCodec(codec);
        return original;
    }
}
