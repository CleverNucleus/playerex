package com.github.clevernucleus.playerex.api.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.text.Text;

/**
 * 
 * Utility wrapper object to allow static creation of lazily loaded text.
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class RenderComponent {
	private final Function<LivingEntity, Text> text;
	private final Function<LivingEntity, List<Text>> tooltip;
	private final int dx, dy;
	
	private RenderComponent(final Function<LivingEntity, Text> functionIn, final Function<LivingEntity, List<Text>> tooltipIn, final int dx, final int dy) {
		this.text = functionIn;
		this.tooltip = tooltipIn;
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * 
	 * @param functionIn display text.
	 * @param tooltipIn tooltip text.
	 * @param dx x position
	 * @param dy y position.
	 * @return
	 */
	public static RenderComponent of(final Function<LivingEntity, Text> functionIn, final Function<LivingEntity, List<Text>> tooltipIn, final int dx, final int dy) {
		return new RenderComponent(functionIn, tooltipIn, dx, dy);
	}
	
	/**
	 * 
	 * @param attributeIn the text (and therefore tooltip) only display if the player has this attribute and it is not null (i.e. registered to the game).
	 * @param functionIn display text.
	 * @param tooltipIn tooltip text.
	 * @param dx x position
	 * @param dy y position
	 * @return
	 */
	public static RenderComponent of(final Supplier<EntityAttribute> attributeIn, final Function<Double, Text> functionIn, final Function<Double, List<Text>> tooltipIn, final int dx, final int dy) {
		return new RenderComponent(livingEntity -> DataAttributesAPI.ifPresent(livingEntity, attributeIn, Text.empty(), functionIn), livingEntity -> DataAttributesAPI.ifPresent(livingEntity, attributeIn, new ArrayList<Text>(), tooltipIn), dx, dy);
	}
	
	private boolean isMouseOver(float xIn, float yIn, float widthIn, float heightIn, int mouseX, int mouseY) {
		return mouseX >= (float)xIn && mouseY >= (float)yIn && mouseX < (float)(xIn + widthIn) && mouseY < (float)(yIn + heightIn);
	}
	
	/**
	 * 
	 * @param livingEntity
	 * @param ctx
	 * @param textRenderer
	 * @param x
	 * @param y
	 * @param scaleX
	 * @param scaleY
	 */
	public void renderText(LivingEntity livingEntity, DrawContext ctx, TextRenderer textRenderer, int x, int y, float scaleX, float scaleY) {
		ctx.drawText(textRenderer, this.text.apply(livingEntity), (int)((x + this.dx) / scaleX), (int)((y + this.dy) / scaleY), 4210752, false);
	}
	
	/**
	 * 
	 * @param livingEntity
	 * @param consumer
	 * @param ctx
	 * @param textRenderer
	 * @param x
	 * @param y
	 * @param mouseX
	 * @param mouseY
	 * @param scaleX
	 * @param scaleY
	 */
	public void renderTooltip(LivingEntity livingEntity, RenderTooltip consumer, DrawContext ctx, TextRenderer textRenderer, int x, int y, int mouseX, int mouseY, float scaleX, float scaleY) {
		if(this.isMouseOver(x + this.dx, y + this.dy, textRenderer.getWidth(this.text.apply(livingEntity)) * scaleX, 7, mouseX, mouseY)) {
			consumer.renderTooltip(ctx, this.tooltip.apply(livingEntity), mouseX, mouseY);
		}
	}
	
	@FunctionalInterface
	public interface RenderTooltip {
		
		/**
		 * 
		 * @param ctx
		 * @param tooltip
		 * @param mouseX
		 * @param mouseY
		 */
		void renderTooltip(DrawContext ctx, List<Text> tooltip, int mouseX, int mouseY);
	}
}
