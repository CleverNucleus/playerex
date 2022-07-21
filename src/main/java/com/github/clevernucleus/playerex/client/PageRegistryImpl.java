package com.github.clevernucleus.playerex.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.client.gui.Page;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class PageRegistryImpl {
	private static final Map<Identifier, Supplier<Page>> PAGES = new HashMap<Identifier, Supplier<Page>>();
	private static final Multimap<Identifier, PageLayer.Builder> LAYERS = ArrayListMultimap.create();
	private static final Identifier BLANK = new Identifier(ExAPI.MODID, "textures/gui/blank.png");
	
	public static void addPage(final Identifier pageId, final Identifier icon, final Identifier texture, final Text title) {
		PAGES.putIfAbsent(pageId, () -> new Page(pageId, texture, title));
	}
	
	public static void addPage(final Identifier pageId, final Identifier icon, final Text title) {
		PAGES.putIfAbsent(pageId, () -> new Page(pageId, icon, title));
	}
	
	public static void addLayer(final Identifier pageId, PageLayer.Builder builder) {
		LAYERS.put(pageId, builder);
	}
	
	public static Map<Identifier, Supplier<Page>> pages() {
		return PAGES;
	}
	
	public static Page findPage(final Identifier pageId) {
		return PAGES.getOrDefault(pageId, () -> new Page(pageId, BLANK, Text.empty())).get();
	}
	
	public static Collection<PageLayer.Builder> findPageLayers(final Identifier pageId) {
		return LAYERS.get(pageId);
	}
}
