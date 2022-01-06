package com.github.clevernucleus.playerex.api.client;

import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * This is where pages and pagelayers are registered.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class PageRegistry {
	
	/**
	 * Registers a page and tab to the PlayerEx screen.
	 * @param pageId unique - should be modid:name.
	 * @param texture the location of the background texture for the page.
	 * @param title the title of the page.
	 * @param icon the page's tab icon.
	 */
	public static void registerPage(final Identifier pageId, final Identifier texture, final Text title, final Supplier<ItemStack> icon) {
		com.github.clevernucleus.playerex.client.gui.PageRegistryImpl.addPage(pageId, texture, title, icon);
	}
	
	/**
	 * Registers a page and tab, but with the default background.
	 * @param pageId
	 * @param title
	 * @param icon
	 */
	public static void registerPage(final Identifier pageId, final Text title, final Supplier<ItemStack> icon) {
		com.github.clevernucleus.playerex.client.gui.PageRegistryImpl.addPage(pageId, title, icon);
	}
	
	/**
	 * Registers a page layer to a page of the same input pageId.
	 * @param pageId
	 * @param builder
	 */
	public static void registerLayer(final Identifier pageId, PageLayer.Builder builder) {
		com.github.clevernucleus.playerex.client.gui.PageRegistryImpl.addLayer(pageId, builder);
	}
}
