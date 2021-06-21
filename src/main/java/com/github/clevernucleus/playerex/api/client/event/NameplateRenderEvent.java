package com.github.clevernucleus.playerex.api.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Client-side event that allows for code to be run when the player's nameplate is rendered.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class NameplateRenderEvent {
	
	/**
	 * This event fires when the client player's name plate is rendered.
	 */
	public static final Event<NameplateRenderEvent.Render> ON_RENDER = EventFactory.createArrayBacked(NameplateRenderEvent.Render.class, listeners -> (renderer, player, matrixStack, vertexConsumerProvider, light) -> {
		for(Render listener : listeners) {
			listener.render(renderer, player, matrixStack, vertexConsumerProvider, light);
		}
	});
	
	@FunctionalInterface
	public interface Render {
		
		/**
		 * 
		 * @param renderer
		 * @param player
		 * @param matrixStack
		 * @param vertexConsumerProvider
		 * @param light
		 */
		void render(final PlayerEntityRenderer renderer, final AbstractClientPlayerEntity player, final MatrixStack matrixStack, final VertexConsumerProvider vertexConsumerProvider, final int light);
	}
}
