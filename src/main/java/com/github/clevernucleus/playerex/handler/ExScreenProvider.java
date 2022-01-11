package com.github.clevernucleus.playerex.handler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ExScreenProvider implements ExtendedScreenHandlerFactory {
	private final int pageId;
	
	public ExScreenProvider(final int pageId) {
		this.pageId = pageId;
	}
	
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new ExScreenHandler(syncId, inv, this.pageId);
	}
	
	@Override
	public Text getDisplayName() {
		return new TranslatableText("playerex.gui.page.attributes.title");
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeInt(this.pageId);
	}
}
