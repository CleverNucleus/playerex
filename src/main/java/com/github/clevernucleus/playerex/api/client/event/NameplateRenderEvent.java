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

@Environment(EnvType.CLIENT)
public final class NameplateRenderEvent {
	
	
	public static final Event<NameplateRenderEvent.Render> EVENT = EventFactory.createArrayBacked(NameplateRenderEvent.Render.class, listeners -> (dispatcher, renderer, player, matrixStack, vertexConsumerProvider, light) -> {
		for(NameplateRenderEvent.Render listener : listeners) {
			listener.render(dispatcher, renderer, player, matrixStack, vertexConsumerProvider, light);
		}
	});
	
	
	public interface Render {
		
		
		void render(final EntityRenderDispatcher dispatcher, final PlayerEntityRenderer renderer, final AbstractClientPlayerEntity player, final MatrixStack matrixStack, final VertexConsumerProvider vertexConsumerProvider, final int light);
	}
}
