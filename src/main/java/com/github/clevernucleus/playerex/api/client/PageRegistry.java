package com.github.clevernucleus.playerex.api.client;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

/**
 * The API's client registry. Used to register new pages to the attributes screen.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class PageRegistry {
	
	/**
	 * Functional Interface that lets the page be lazily built during HandledScreen construction.
	 * 
	 * @author CleverNucleus
	 *
	 */
	@FunctionalInterface
	public interface PageBuilder {
		
		/**
		 * @param parent The main attributes screen
		 * @param handler The main attributes screen handler
		 * @param inventory The player's inventory
		 * @return The constructed page screen.
		 */
		PageScreen build(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory);
	}
	
	private static Map<Identifier, PageBuilder> pages = Maps.newHashMap();
	
	private static PageScreen orDefault(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory) {
		return new PageScreen(parent, handler, inventory, LiteralText.EMPTY, ItemStack.EMPTY) {};
	}
	
	/**
	 * Registers a new page to the attributes screen. A tab is automatically added to access the page.
	 * @param id The page's unique identifier; should follow the format "modid:page" e.g. -> "playerex:combat"
	 * @param page The page builder functional interface.
	 */
	public static void register(final Identifier id, final PageBuilder page) {
		pages.putIfAbsent(id, page);
	}
	
	/**
	 * @param id The page's identifier
	 * @return The page builder functional interface; if nothing is present for the input identifier, a default page builder is returned.
	 */
	public static PageBuilder find(final Identifier id) {
		return pages.getOrDefault(id, PageRegistry::orDefault);
	}
	
	/**
	 * @return An immutable copy of the page builders and their identifiers mapped.
	 */
	public static ImmutableMap<Identifier, PageBuilder> immutableMap() {
		return ImmutableMap.copyOf(pages);
	}
}
