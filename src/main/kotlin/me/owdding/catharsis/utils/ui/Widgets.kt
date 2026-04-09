package me.owdding.catharsis.utils.ui

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.InputWithModifiers
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.Style
import tech.thatgravyboat.skyblockapi.platform.drawString
import tech.thatgravyboat.skyblockapi.utils.text.Text

abstract class BaseWidget(x: Int, y: Int, width: Int, height: Int) : AbstractWidget(x, y, width, height, Component.empty()) {

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
    }

}

abstract class BaseButtonWidget(x: Int, y: Int, width: Int, height: Int) : AbstractButton(x, y, width, height, Component.empty()) {

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
    }

}

class SelectedTextButton(x: Int, y: Int, width: Int, height: Int, text: Component, var selected: Boolean, val onPress: (SelectedTextButton) -> Unit) : BaseButtonWidget(x, y, width, height) {

    private val normalText: Component = text
    private val normalSelectedText: Component = Text.join("✔ ", text)
    private val hoveredText: Component = ComponentUtils.mergeStyles(normalText, Style.EMPTY.withUnderlined(true))
    private val hoveredSelectedText: Component = ComponentUtils.mergeStyles(normalSelectedText, Style.EMPTY.withUnderlined(true))

    override fun onPress(input: InputWithModifiers) {
        this.onPress(this)
    }

    override fun renderContents(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val component = when {
            this.selected && this.isHoveredOrFocused -> this.hoveredSelectedText
            this.selected -> this.normalSelectedText
            this.isHoveredOrFocused -> this.hoveredText
            else -> this.normalText
        }
        graphics.drawString(component, this.x, this.y)
    }
}
