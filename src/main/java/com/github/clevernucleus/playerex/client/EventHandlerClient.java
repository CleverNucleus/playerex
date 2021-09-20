package com.github.clevernucleus.playerex.client;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public final class EventHandlerClient {
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
			Matrix4f matrix4f = matrices.peek().getModel();
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
	
	public static void nameplateRender(EntityRenderDispatcher dispatcher, PlayerEntityRenderer renderer, AbstractClientPlayerEntity player, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		if(ExAPI.CONFIG.get().showLevelNameplates()) {
			EntityAttribute attribute = ExAPI.LEVEL.get();
			AttributeContainer container = player.getAttributes();
			
			if(attribute != null && container.hasAttribute(attribute)) {
				int level = (int)Math.round(container.getValue(attribute));
				Text tag = new TranslatableText("text.playerex.nameplate.level", level);
				
				renderLevel(dispatcher, renderer, player, tag, matrixStack, vertexConsumerProvider, light);
			}
		}
	}
	
	public static void onScreenInit(MinecraftClient client, Screen screen, int width, int height) {
		if(screen instanceof InventoryScreen) {
			HandledScreen<?> handledScreen = (HandledScreen<?>)screen;
			
			if(Screens.getButtons(screen) != null) {
				int x = ExAPI.CONFIG.get().inventoryButtonPosX();
				int y = ExAPI.CONFIG.get().inventoryButtonPosY();
				
				Screens.getButtons(screen).add(new ScreenButtonWidget(handledScreen, x, y, 176, 0, 14, 13, btn -> NetworkHandlerClient.openAttributesScreen()));
			}
		}
	}
	
	public static void onKeyPressed(MinecraftClient client) {
		while(PlayerExClient.keyBinding.wasPressed()) {
			if(client.currentScreen == null && !client.interactionManager.hasRidingInventory()) {
				NetworkHandlerClient.openAttributesScreen();
			}
		}
	}
}
