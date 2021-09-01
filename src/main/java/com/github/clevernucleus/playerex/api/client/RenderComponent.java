package com.github.clevernucleus.playerex.api.client;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * A functional object used to hold render information in one place, but lazily so that acts as a reference.
 * 
 * @author CleverNucleus
 *
 */
public final class RenderComponent {
	private final Supplier<Boolean> shouldRender;
	private final Supplier<Text> text;
	private final Supplier<List<Text>> tooltip;
	private final int dx, dy;
	private final float scale;
	
	/**
	 * 
	 * @param shouldRender Renders text if true.
	 * @param text The text that is rendered.
	 * @param tooltip The tooltip that is displayed when hovering over the text.
	 * @param dx The x-offset of the text.
	 * @param dy The y-offset of the text.
	 * @param scale The scale of the text (not applied to the tooltip).
	 */
	public RenderComponent(final Supplier<Boolean> shouldRender, final Supplier<Text> text, final Supplier<List<Text>> tooltip, final int dx, final int dy, final float scale) {
		this.shouldRender = shouldRender;
		this.text = text;
		this.tooltip = tooltip;
		this.dx = dx;
		this.dy = dy;
		this.scale = scale;
	}
	
	private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
		return mouseX >= (float)x && mouseY >= (float)y && mouseX < (float)(x + width) && mouseY < (float)(y + height);
	}
	
	/**
	 * 
	 * @param matrices
	 * @param textRenderer
	 * @param x
	 * @param y
	 */
	public void renderText(MatrixStack matrices, TextRenderer textRenderer, int x, int y) {
		if(!this.shouldRender.get()) return;
		textRenderer.draw(matrices, this.text.get(), (x + this.dx) / this.scale, (y + this.dy) / this.scale, 4210752);
	}
	
	/**
	 * 
	 * @param consumer
	 * @param matrices
	 * @param textRenderer
	 * @param x
	 * @param y
	 * @param mouseX
	 * @param mouseY
	 */
	public void renderTooltip(RenderTooltip consumer, MatrixStack matrices, TextRenderer textRenderer, int x, int y, int mouseX, int mouseY) {
		if(!this.shouldRender.get()) return;
		if(this.isMouseOver(x + this.dx, y + this.dy, textRenderer.getWidth(this.text.get()) * this.scale, 7, mouseX, mouseY)) {
			consumer.renderTooltip(matrices, this.tooltip.get(), mouseX, mouseY);
		}
	}
	
	@FunctionalInterface
	public interface RenderTooltip {
		
		/**
		 * 
		 * @param matrices
		 * @param tooltip
		 * @param mouseX
		 * @param mouseY
		 */
		void renderTooltip(MatrixStack matrices, List<Text> tooltip, int mouseX, int mouseY);
	}
}
