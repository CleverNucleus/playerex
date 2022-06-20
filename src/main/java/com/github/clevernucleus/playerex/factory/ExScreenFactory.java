package com.github.clevernucleus.playerex.factory;

import com.github.clevernucleus.playerex.PlayerEx;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ExScreenFactory implements ExtendedScreenHandlerFactory {
	private final int pageId;
	
	public ExScreenFactory(final int pageId) {
		this.pageId = pageId;
	}
	
	public static ExtendedScreenHandlerType<Handler> type() {
		return new ExtendedScreenHandlerType<>((syncId, inv, buf) -> new ExScreenFactory.Handler(syncId, inv, buf.readInt()));
	}
	
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
		return new Handler(syncId, inventory, this.pageId);
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeInt(this.pageId);
	}
	
	@Override
	public Text getDisplayName() {
		return new TranslatableText("playerex.gui.page.attributes.title");
	}
	
	public static class Handler extends ScreenHandler {
		public final int pageId;
		
		public Handler(int syncId, PlayerInventory inventory, int pageId) {
			super(PlayerEx.EX_SCREEN, syncId);
			this.pageId = pageId;
		}
		
		@Override
		public boolean canUse(PlayerEntity player) {
			return true;
		}
	}
}
