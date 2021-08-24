package com.github.clevernucleus.playerex.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class AttributesScreenProvider implements NamedScreenHandlerFactory {
	
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new AttributesScreenHandler(syncId, inv);
	}
	
	@Override
	public Text getDisplayName() {
		return new TranslatableText("gui.playerex.page.attributes.title");
	}
}
