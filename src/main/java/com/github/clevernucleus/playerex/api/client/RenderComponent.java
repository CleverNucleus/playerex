package com.github.clevernucleus.playerex.api.client;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public final class RenderComponent {
	private final Supplier<Boolean> shouldRender;
	private final Supplier<Text> text;
	private final Supplier<List<Text>> tooltip;
	private final int dx, dy;
	private final float scale;
	
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
	
	public void renderText(MatrixStack matrices, TextRenderer textRenderer, int x, int y) {
		if(!this.shouldRender.get()) return;
		textRenderer.draw(matrices, this.text.get(), (x + this.dx) / this.scale, (y + this.dy) / this.scale, 4210752);
	}
	
	public void renderTooltip(RenderTooltip consumer, MatrixStack matrices, TextRenderer textRenderer, int x, int y, int mouseX, int mouseY) {
		if(!this.shouldRender.get()) return;
		if(this.isMouseOver(x + this.dx, y + this.dy, textRenderer.getWidth(this.text.get()) * this.scale, 7, mouseX, mouseY)) {
			consumer.renderTooltip(matrices, this.tooltip.get(), mouseX, mouseY);
		}
	}
	
	@FunctionalInterface
	public interface RenderTooltip {
		
		void renderTooltip(MatrixStack matrices, List<Text> tooltip, int mouseX, int mouseY);
	}
}
