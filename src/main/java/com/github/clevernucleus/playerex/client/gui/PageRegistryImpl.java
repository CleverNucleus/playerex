package com.github.clevernucleus.playerex.client.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.clevernucleus.playerex.api.client.Page;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class PageRegistryImpl {
	private static final Map<Identifier, Page> PAGES = new HashMap<Identifier, Page>();
	private static final Multimap<Identifier, PageLayer.Builder> LAYERS = ArrayListMultimap.create();
	
	public static void addPage(final Page page) {
		if(page == null) return;
		
		PAGES.putIfAbsent(page.id(), page);
	}
	
	public static void addLayer(final Identifier pageId, PageLayer.Builder builder) {
		LAYERS.put(pageId, builder);
	}
	
	protected static Map<Identifier, Page> pages() {
		return PAGES;
	}
	
	protected static Page findPage(final Identifier pageId) {
		return PAGES.getOrDefault(pageId, new Page(pageId, LiteralText.EMPTY, () -> ItemStack.EMPTY));
	}
	
	protected static Collection<PageLayer.Builder> findPageLayers(final Identifier pageId) {
		return LAYERS.get(pageId);
	}
}
