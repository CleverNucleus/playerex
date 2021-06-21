package com.github.clevernucleus.playerex.client;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;
import com.github.clevernucleus.playerex.client.network.ClientNetworkHandler;
import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.Matrix4f;

public final class ClientEventHandler {
	public static final DecimalFormat MODIFIER_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("##.##"), (decimalFormat) -> {
		decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
	});
	
	private static void renderLevel(PlayerEntityRenderer renderer, AbstractClientPlayerEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
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
	
	public static void nameplateRender(PlayerEntityRenderer renderer, AbstractClientPlayerEntity player, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		if(ExAPI.CONFIG.get().showLevelNameplates()) {
			AttributeData data = ExAPI.DATA.get(player);
			int level = Math.round((float)data.get(PlayerAttributes.LEVEL.get()));
			Text tag = new TranslatableText("text.playerex.level", level);
			
			renderLevel(renderer, player, tag, matrixStack, vertexConsumerProvider, light);
		}
	}
	
	public static void screenInit(MinecraftClient client, Screen screen, int width, int height) {
		if(screen instanceof InventoryScreen) {
			HandledScreen<?> handledScreen = (HandledScreen<?>)screen;
			
			if(Screens.getButtons(screen) != null) {
				int x = ExAPI.CONFIG.get().guiButtonPosX();
				int y = ExAPI.CONFIG.get().guiButtonPosY();
				
				Screens.getButtons(screen).add(new ScreenButtonWidget(handledScreen, x, y, 176, 0, 14, 13, ClientNetworkHandler::openAttributesScreen));
			}
		}
	}
	
	public static void tooltipModify(ItemStack stack, TooltipContext context, List<Text> lines) {
		EquipmentSlot[] slots = EquipmentSlot.values();
		int offset = context.isAdvanced() ? 3 : 1;
		int index = context.isAdvanced() ? 2 : 0;
		
		for(int i = 0; i < slots.length; i++) {
			EquipmentSlot slot = slots[i];
			Multimap<EntityAttribute, EntityAttributeModifier> multimap = stack.getAttributeModifiers(slot);
			
			if(!multimap.isEmpty()) {
				for(Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
					EntityAttribute attribute = entry.getKey();
					
					if(attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE || attribute == EntityAttributes.GENERIC_ATTACK_SPEED) {
						lines.remove(lines.size() - offset);
					}
				}
				
				for(Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
					EntityAttribute attribute = entry.getKey();
					EntityAttributeModifier modifier = entry.getValue();
					double d = modifier.getValue();
					
					if(attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE || attribute == EntityAttributes.GENERIC_ATTACK_SPEED) {
						String prefix = d >= 0.0D ? "+" : "";
						
						lines.add(lines.size() - index, (new LiteralText(" ")).append(new TranslatableText("attribute.modifier.equals." + modifier.getOperation().getId(), new Object[] {prefix + MODIFIER_FORMAT.format(d), new TranslatableText(((EntityAttribute)entry.getKey()).getTranslationKey())})).formatted(Formatting.DARK_GREEN));
					}
				}
			}
		}
	}
}
