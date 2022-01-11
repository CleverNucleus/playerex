package com.github.clevernucleus.playerex.mixin.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.github.clevernucleus.playerex.client.gui.ExScreenData;
import com.github.clevernucleus.playerex.client.gui.Page;
import com.github.clevernucleus.playerex.client.gui.PageRegistryImpl;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(AbstractInventoryScreen.class)
abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T> implements ExScreenData {
	@Unique private List<Page> ex_pages = new ArrayList<Page>();
	
	private AbstractInventoryScreenMixin(T screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}
	
	private boolean filter(Map.Entry<Identifier, Supplier<Page>> entry) {
		return !(entry.getKey().equals(PlayerExClient.ATTRIBUTES_PAGE) || entry.getKey().equals(PlayerExClient.COMBAT_PAGE));
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(T screenHandler, PlayerInventory playerInventory, Text text, CallbackInfo info) {
		this.ex_pages.add(0, PageRegistryImpl.findPage(PlayerExClient.ATTRIBUTES_PAGE));
		this.ex_pages.add(1, PageRegistryImpl.findPage(PlayerExClient.COMBAT_PAGE));
		
		PageRegistryImpl.pages().entrySet().stream().filter(this::filter).map(Map.Entry::getValue).forEach(page -> this.ex_pages.add(page.get()));
	}
	
	@Override
	public int getX() {
		return this.x;
	}
	
	@Override
	public int getY() {
		return this.y;
	}
	
	@Override
	public List<Page> pages() {
		return this.ex_pages;
	}
}
