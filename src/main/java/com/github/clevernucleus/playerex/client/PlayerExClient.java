package com.github.clevernucleus.playerex.client;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map.Entry;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageRegistry;
import com.github.clevernucleus.playerex.client.gui.AttributesPage;
import com.github.clevernucleus.playerex.client.gui.AttributesScreen;
import com.github.clevernucleus.playerex.client.gui.CombatPage;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;
import com.github.clevernucleus.playerex.client.network.ClientNetworkHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.google.common.collect.Multimap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public final class PlayerExClient implements ClientModInitializer {
	public static final Identifier ATTTRIBUTES_PAGE_KEY = new Identifier(ExAPI.MODID, "attributes");
	public static final Identifier COMBAT_PAGE_KEY = new Identifier(ExAPI.MODID, "combat");
	public static final DecimalFormat MODIFIER_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("##.##"), (decimalFormat) -> {
		decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
	});
	
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(NetworkHandler.SYNC_CONFIG, ClientNetworkHandler::syncConfig);
		ClientPlayNetworking.registerGlobalReceiver(NetworkHandler.SYNC_ATTRIBUTES, ClientNetworkHandler::syncAttributes);
		
		ScreenRegistry.register(PlayerEx.ATTRIBUTES_SCREEN_HANDLER, AttributesScreen::new);
		PageRegistry.register(ATTTRIBUTES_PAGE_KEY, (parent, handler, inv) -> new AttributesPage(parent, handler, inv));
		PageRegistry.register(COMBAT_PAGE_KEY, (parent, handler, inv) -> new CombatPage(parent, handler, inv));
		
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if(screen instanceof InventoryScreen) {
				HandledScreen<?> handledScreen = (HandledScreen<?>)screen;
				
				if(Screens.getButtons(screen) != null) {
					int x = ExAPI.CONFIG.get().guiButtonPosX();
					int y = ExAPI.CONFIG.get().guiButtonPosY();
					
					Screens.getButtons(screen).add(new ScreenButtonWidget(handledScreen, x, y, 176, 0, 14, 13, ClientNetworkHandler::openAttributesScreen));
				}
			}
		});
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
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
		});
	}
}
