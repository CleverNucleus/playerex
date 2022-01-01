package com.github.clevernucleus.playerex.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;

@Mixin(PlayerEntityRenderer.class)
abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private PlayerEntityRendererMixin(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}
	
	private static void renderLevel(EntityRenderDispatcher dispatcher, PlayerEntityRenderer renderer, AbstractClientPlayerEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		double d = dispatcher.getSquaredDistanceToCamera(entity);
		
		if(!(d > 4096.0D)) {
			boolean bl = !entity.isSneaky();
			float f = entity.getHeight() + 0.3F;
			int i = 0;
			matrices.push();
			matrices.translate(0.0D, (double)f, 0.0D);
			matrices.multiply(dispatcher.getRotation());
			matrices.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrices.peek().getPositionMatrix();
			MinecraftClient mc = MinecraftClient.getInstance();
			float g = mc.options.getTextBackgroundOpacity(0.25F);
			int j = (int)(g * 255.0F) << 24;
			TextRenderer textRenderer = renderer.getTextRenderer();
			float h = (float)(-textRenderer.getWidth((StringVisitable)text) / 2);
			textRenderer.draw(text, h, (float)i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
			
			if(bl) {
				textRenderer.draw((Text)text, h, (float)i, -1, false, matrix4f, vertexConsumers, false, 0, light);
			}
			
			matrices.pop();
		}
	}
	
	@Inject(method = "renderLabelIfPresent", at = @At("RETURN"))
	private void onRenderLabelIfPresent(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if(ExAPI.getConfig().showLevelNameplates()) {
			DataAttributesAPI.ifPresent(abstractClientPlayerEntity, ExAPI.LEVEL, (Object)null, value -> {
				PlayerEntityRenderer renderer = (PlayerEntityRenderer)(Object)this;
				Text tag = new TranslatableText("playerex.gui.text.nameplate", Math.round(value));
				
				renderLevel(this.dispatcher, renderer, abstractClientPlayerEntity, tag, matrixStack, vertexConsumerProvider, i);
				
				return (Object)null;
			});
		}
	}
}
