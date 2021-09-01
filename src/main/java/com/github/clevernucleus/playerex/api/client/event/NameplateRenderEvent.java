package com.github.clevernucleus.playerex.api.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Offers a hook into the method {@link PlayerEntityRenderer#renderLabelIfPresent(AbstractClientPlayerEntity, Text, MatrixStack, VertexConsumerProvider, int)}.
 * Allows for rendering things ingame.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class NameplateRenderEvent {
	
	/**
	 * Called when rendering the player's nameplate (on multiplayer; on the client).
	 */
	public static final Event<NameplateRenderEvent.Render> EVENT = EventFactory.createArrayBacked(NameplateRenderEvent.Render.class, listeners -> (dispatcher, renderer, player, matrixStack, vertexConsumerProvider, light) -> {
		for(NameplateRenderEvent.Render listener : listeners) {
			listener.render(dispatcher, renderer, player, matrixStack, vertexConsumerProvider, light);
		}
	});
	
	@FunctionalInterface
	public interface Render {
		
		/**
		 * 
		 * @param dispatcher
		 * @param renderer
		 * @param player
		 * @param matrixStack
		 * @param vertexConsumerProvider
		 * @param light
		 */
		void render(final EntityRenderDispatcher dispatcher, final PlayerEntityRenderer renderer, final AbstractClientPlayerEntity player, final MatrixStack matrixStack, final VertexConsumerProvider vertexConsumerProvider, final int light);
	}
}
