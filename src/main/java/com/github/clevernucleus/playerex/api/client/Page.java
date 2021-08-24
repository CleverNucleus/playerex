package com.github.clevernucleus.playerex.api.client;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class Page {
	private final Collection<PageLayer> layers;
	private final Identifier identifier;
	private final ItemStack icon;
	private final Text title;
	
	protected Page(final Identifier identifier, final Text title, final ItemStack icon) {
		this.layers = new ArrayList<PageLayer>();
		this.identifier = identifier;
		this.title = title;
		this.icon = icon;
	}
	
	public Text title() {
		return this.title;
	}
	
	public ItemStack tabIcon() {
		return this.icon;
	}
	
	public Collection<PageLayer> layers() {
		return ImmutableList.copyOf(this.layers);
	}
	
	public void buildLayers(ScreenHandler handler, PlayerInventory inv) {
		PageRegistry.findPageLayers(this.identifier).stream().map(builder -> builder.build(handler, inv, this.title)).forEach(this.layers::add);
	}
}
