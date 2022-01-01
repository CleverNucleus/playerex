package com.github.clevernucleus.playerex.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ExScreenProvider implements NamedScreenHandlerFactory {
	
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new ExScreenHandler(syncId, inv);
	}
	
	@Override
	public Text getDisplayName() {
		return new TranslatableText("playerex.gui.page.attributes.title");
	}
}
