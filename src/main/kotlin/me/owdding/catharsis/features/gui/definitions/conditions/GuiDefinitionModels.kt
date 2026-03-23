package me.owdding.catharsis.features.gui.definitions.conditions

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import me.owdding.catharsis.Catharsis
import me.owdding.catharsis.hooks.gui.AbstractContainerMenuHook
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.inventory.MenuType

sealed interface GuiMenuType {

    fun matches(screen: AbstractContainerScreen<*>): Boolean

    class Minecraft(val type: MenuType<*>) : GuiMenuType {

        override fun matches(screen: AbstractContainerScreen<*>): Boolean {
            return (screen.menu as? AbstractContainerMenuHook)?.`catharsis$getMenuTypeOrNull`() == this.type
        }
    }

    enum class Custom(
        private val id: Identifier,
        private val predicate: (AbstractContainerScreen<*>) -> Boolean,
    ) : GuiMenuType {
        INVENTORY(Catharsis.id("inventory"), { it is InventoryScreen }),
        ;

        override fun matches(screen: AbstractContainerScreen<*>): Boolean {
            return this.predicate(screen)
        }

        companion object {

            private val BY_ID = Custom.entries.associateBy(Custom::id)

            val CODEC: Codec<Custom> = Identifier.CODEC.comapFlatMap(
                { id -> BY_ID[id]?.let(DataResult<Custom>::success) ?: DataResult.error { "Unknown GuiMenuType id:, $id" } },
                { it.id },
            )
        }
    }

    companion object {

        val CODEC: Codec<GuiMenuType> = Codec.either(BuiltInRegistries.MENU.byNameCodec(), Custom.CODEC).xmap(
            { it.map(::Minecraft) { type -> type } },
            { if (it is Minecraft) Either.left(it.type) else Either.right(it as Custom) },
        )
    }
}
