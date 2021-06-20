package com.github.clevernucleus.playerex.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	
	protected void renderLevel(AbstractClientPlayerEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		PlayerEntityRenderer renderer = (PlayerEntityRenderer)(Object)this;
		EntityRenderDispatcher dispatcher = renderer.getRenderManager();
		
		double d = dispatcher.getSquaredDistanceToCamera(entity);
		
		if(!(d > 4096.0D)) {
			boolean bl = !entity.isSneaky();
			float f = entity.getHeight() + 0.3F;
			int i = 0;
			matrices.push();
			matrices.translate(0.0D, (double)f, 0.0D);
			matrices.multiply(dispatcher.getRotation());
			matrices.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrices.peek().getModel();
			float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
			int j = (int)(g * 255.0F) << 24;
			TextRenderer textRenderer = renderer.getFontRenderer();
			float h = (float)(-textRenderer.getWidth((StringVisitable)text) / 2);
			textRenderer.draw(text, h, (float)i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
			
			if(bl) {
				textRenderer.draw((Text)text, h, (float)i, -1, false, matrix4f, vertexConsumers, false, 0, light);
			}
			
			matrices.pop();
		}
	}
	
	@Inject(method = "renderLabelIfPresent", at = @At("RETURN"))
	private void appendLabel(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if(ExAPI.CONFIG.get().showLevelNameplates()) {
			AttributeData data = ExAPI.DATA.get(abstractClientPlayerEntity);
			int level = Math.round((float)data.get(PlayerAttributes.LEVEL.get()));
			Text tag = new TranslatableText("text.playerex.level", level);
			
			renderLevel(abstractClientPlayerEntity, tag, matrixStack, vertexConsumerProvider, i);
		}
	}
}
