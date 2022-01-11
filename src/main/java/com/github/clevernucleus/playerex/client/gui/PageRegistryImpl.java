package com.github.clevernucleus.playerex.client.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class PageRegistryImpl {
	private static final Map<Identifier, Supplier<Page>> PAGES = new HashMap<Identifier, Supplier<Page>>();
	private static final Multimap<Identifier, PageLayer.Builder> LAYERS = ArrayListMultimap.create();
	
	public static void addPage(final Identifier pageId, final Identifier texture, final Text title, final Supplier<ItemStack> icon) {
		PAGES.putIfAbsent(pageId, () -> new Page(pageId, texture, title, icon));
	}
	
	public static void addPage(final Identifier pageId, final Text title, final Supplier<ItemStack> icon) {
		PAGES.putIfAbsent(pageId, () -> new Page(pageId, title, icon));
	}
	
	public static void addLayer(final Identifier pageId, PageLayer.Builder builder) {
		LAYERS.put(pageId, builder);
	}
	
	public static Map<Identifier, Supplier<Page>> pages() {
		return PAGES;
	}
	
	public static Page findPage(final Identifier pageId) {
		return PAGES.getOrDefault(pageId, () -> new Page(pageId, LiteralText.EMPTY, () -> ItemStack.EMPTY)).get();
	}
	
	protected static Collection<PageLayer.Builder> findPageLayers(final Identifier pageId) {
		return LAYERS.get(pageId);
	}
}
