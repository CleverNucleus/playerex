package clevernucleus.playerex.api.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.collect.Maps;

import clevernucleus.playerex.api.client.gui.Page;
import clevernucleus.playerex.api.element.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

/**
 * Use this to register client side things.
 */
public class ClientReg {
	private static final Map<Integer, Page> INDEX = Maps.newHashMap();
	private static final Map<ResourceLocation, List<BiFunction<PlayerEntity, IPlayerElements, String>>> TOOLTIPS = Maps.newHashMap();
	
	private ClientReg() {}
	
	/**
	 * Add to the tooltip of an element.
	 * @param par0 The element's resource location
	 * @param par1 The output string to display on the tooltip (dynamic).
	 */
	public static void addTooltip(final ResourceLocation par0, BiFunction<PlayerEntity, IPlayerElements, String> par1) {
		List<BiFunction<PlayerEntity, IPlayerElements, String>> var0 = TOOLTIPS.getOrDefault(par0, new ArrayList<BiFunction<PlayerEntity, IPlayerElements, String>>());
		
		var0.add(par1);
		
		TOOLTIPS.put(par0, var0);
	}
	
	/**
	 * @param par0 Element's resource location
	 * @return The input element's tooltip list.
	 */
	public static List<BiFunction<PlayerEntity, IPlayerElements, String>> getTooltips(final ResourceLocation par0) {
		return TOOLTIPS.getOrDefault(par0, new ArrayList<BiFunction<PlayerEntity, IPlayerElements, String>>());
	}
	
	/**
	 * DO NOT use this method!
	 * @param par0
	 */
	public static void init(Page par0) {
		INDEX.put(0, par0);
	}
	
	/**
	 * Registers a new player attributes page/tab.
	 * @param par1
	 * @return
	 */
	public static Page registerPage(final Page par1) {
		int var0 = INDEX.size();
		
		if(var0 == 0) var0++;
		
		INDEX.putIfAbsent(var0, par1);
		
		return par1;
	}
	
	public static Page getPage(int par0) {
		return INDEX.getOrDefault(par0, new Page(new StringTextComponent("")));
	}
	
	public static Collection<Page> getPages() {
		return INDEX.values();
	}
	
	public static int size() {
		return INDEX.size();
	}
}
