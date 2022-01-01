package com.github.clevernucleus.playerex.api.client;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public final class RenderComponent {
	private final Function<LivingEntity, Text> text;
	private final Supplier<List<Text>> tooltip;
	private final int dx, dy;
	
	
	public RenderComponent(final Supplier<EntityAttribute> attributeIn, final Function<Float, Text> functionIn, final Supplier<List<Text>> tooltipIn, final int dx, final int dy) {
		this.text = livingEntity -> DataAttributesAPI.ifPresent(livingEntity, attributeIn, LiteralText.EMPTY, functionIn);
		this.tooltip = tooltipIn;
		this.dx = dx;
		this.dy = dy;
	}
	
	private boolean isMouseOver(float xIn, float yIn, float widthIn, float heightIn, int mouseX, int mouseY) {
		return mouseX >= (float)xIn && mouseY >= (float)yIn && mouseX < (float)(xIn + widthIn) && mouseY < (float)(yIn + heightIn);
	}
	
	
	public void renderText(LivingEntity livingEntity, MatrixStack matrices, TextRenderer textRenderer, int x, int y, float scaleX, float scaleY) {
		textRenderer.draw(matrices, this.text.apply(livingEntity), (x + this.dx) / scaleX, (y + this.dy) / scaleY, 4210752);
	}
	
	
	public void renderTooltip(LivingEntity livingEntity, RenderTooltip consumer, MatrixStack matrices, TextRenderer textRenderer, int x, int y, int mouseX, int mouseY, float scaleX, float scaleY) {
		if(this.isMouseOver(x + this.dx, y + this.dy, textRenderer.getWidth(this.text.apply(livingEntity)) * scaleX, 7, mouseX, mouseY)) {
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
