package git.clevernucleus.playerex.api.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import git.clevernucleus.playerex.api.attribute.IPlayerAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

/**
 * Use this to register client side things.
 */
public class ClientReg {
	private static final Map<ResourceLocation, Page> INDEX = Maps.newHashMap();
	private static final Multimap<ResourceLocation, BiFunction<PlayerEntity, IPlayerAttributes, String>> TOOLTIPS = ArrayListMultimap.create();
	
	/**
	 * Add to the tooltip of an element.
	 * @param par0 The IPlayerAttribute's registryName.
	 * @param par1 The output string to display on the tooltip (dynamic).
	 */
	public static void addTooltip(final ResourceLocation par0, BiFunction<PlayerEntity, IPlayerAttributes, String> par1) {
		TOOLTIPS.put(par0, par1);
	}
	
	/**
	 * Registers a new player attributes page/tab.
	 * @param par1 Registry Name.
	 * @return the registered page.
	 */
	public static Page registerPage(final ResourceLocation par0, final Page par1) {
		return INDEX.put(par0, par1);
	}
	
	/**
	 * @return An immutable copy of the TOOLTIPS Multimap.
	 */
	public static Multimap<ResourceLocation, BiFunction<PlayerEntity, IPlayerAttributes, String>> tooltips() {
		return ImmutableListMultimap.copyOf(TOOLTIPS);
	}
	
	/**
	 * @param par0 Registry name.
	 * @return Collection of tooltips attached to the input registry name.
	 */
	public static Collection<BiFunction<PlayerEntity, IPlayerAttributes, String>> getTooltips(final ResourceLocation par0) {
		return ImmutableList.copyOf(TOOLTIPS.asMap().getOrDefault(par0, new ArrayList<>()));
	}
	
	/**
	 * @param par0 Registry Name.
	 * @return The relevant page from the registry.
	 */
	public static Page getPage(final ResourceLocation par0) {
		return INDEX.getOrDefault(par0, new Page(new StringTextComponent("")));
	}
	
	/**
	 * @return A collection of all the registry names for each page.
	 */
	public static Collection<ResourceLocation> pageRegistry() {
		return INDEX.keySet();
	}
}
