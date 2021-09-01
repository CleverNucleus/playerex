package com.github.clevernucleus.playerex.api.client.page;

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

/**
 * Page registry class. You can use this to register additional render layers and create new pages.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class PageRegistry {
	private static final Map<Identifier, Page> PAGES = new HashMap<Identifier, Page>();
	private static final Multimap<Identifier, PageLayer.Builder> LAYERS = ArrayListMultimap.create();
	
	/**
	 * Creates a new page for the attributes screen (including tab).
	 * @param id Should follow the format modid:page_name
	 * @param title The text displayed at the top left of the page.
	 * @param icon The tab icon.
	 */
	public static void registerPage(final Identifier id, final Text title, final ItemStack icon) {
		PAGES.putIfAbsent(id, new Page(id, title, icon));
	}
	
	/**
	 * Registers a new render layer to a page.
	 * @param id The id of the page (i.e. to add a render layer to the attributes page, the id would be playerex:attributes).
	 * @param builder Should be an extended constructor of PageLayer, using the format YourPage::new.
	 */
	public static void registerLayer(final Identifier id, PageLayer.Builder builder) {
		LAYERS.put(id, builder);
	}
	
	/**
	 * @return An immutable map of the pages registry.
	 */
	public static Map<Identifier, Page> pages() {
		return ImmutableMap.copyOf(PAGES);
	}
	
	/**
	 * @param id
	 * @return Returns the page for the input identifier if it exists; if not, returns an empty page (not null).
	 */
	public static Page findPage(final Identifier id) {
		return PAGES.getOrDefault(id, new Page(id, LiteralText.EMPTY, ItemStack.EMPTY));
	}
	
	/**
	 * @param id
	 * @return Returns an immutable collection of the render layers for the input page; or an empty collection if no layers are present.
	 */
	public static Collection<PageLayer.Builder> findPageLayers(final Identifier id) {
		return ImmutableList.copyOf(LAYERS.get(id));
	}
}
