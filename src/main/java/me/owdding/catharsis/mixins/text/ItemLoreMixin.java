package me.owdding.catharsis.mixins.text;

import me.owdding.catharsis.features.text.targets.ItemTextReplacements;
import me.owdding.catharsis.hooks.text.TooltipProviderHook;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemLore;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemLore.class)
public class ItemLoreMixin implements TooltipProviderHook {

    @Shadow
    @Final
    private List<Component> styledLines;

    @Unique private List<Component> catharsis$cachedLore = null;
    @Unique int catharsis$cacheKey = -1;

    @Override
    public void catharsis$addToTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag, DataComponentGetter components) {
        catharsis$getOrCreateCache(stack).forEach(adder);
    }

    @Unique
    private List<Component> catharsis$getOrCreateCache(ItemStack stack) {
        if (this.catharsis$cachedLore == null || this.catharsis$cacheKey != ItemTextReplacements.INSTANCE.getCacheKey()) {
            this.catharsis$cachedLore = ItemTextReplacements.INSTANCE.replace(stack, this.styledLines);
        }
        return this.catharsis$cachedLore;
    }
}
