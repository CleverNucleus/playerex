package com.github.clevernucleus.playerex.api.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


@Environment(EnvType.CLIENT)
public final class PageRegistry {
	private static final Map<Identifier, Page> PAGES = new HashMap<Identifier, Page>();
	private static final Multimap<Identifier, PageLayer.Builder> LAYERS = ArrayListMultimap.create();
	
	
	public static void registerPage(final Identifier id, final Text title, final ItemStack icon) {
		PAGES.putIfAbsent(id, new Page(id, title, icon));
	}
	
	
	public static void registerLayer(final Identifier id, PageLayer.Builder builder) {
		LAYERS.put(id, builder);
	}
	
	
	public static Map<Identifier, Page> pages() {
		return ImmutableMap.copyOf(PAGES);
	}
	
	
	public static Page findPage(final Identifier id) {
		return PAGES.getOrDefault(id, new Page(id, LiteralText.EMPTY, ItemStack.EMPTY));
	}
	
	
	public static Collection<PageLayer.Builder> findPageLayers(final Identifier id) {
		return ImmutableList.copyOf(LAYERS.get(id));
	}
}
