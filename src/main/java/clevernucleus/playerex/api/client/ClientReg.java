package clevernucleus.playerex.api.client;

import java.util.Map;

import com.google.common.collect.Maps;

import clevernucleus.playerex.api.client.gui.Page;
import net.minecraft.util.text.StringTextComponent;

public class ClientReg {
	private static final Map<Integer, Page> INDEX = Maps.newHashMap();
	
	private ClientReg() {}
	
	/**
	 * DO NOT use this method!
	 * @param par0
	 */
	public static void init(Page par0) {
		INDEX.put(0, par0);
	}
	
	public static Page registerPage(final Page par1) {
		int var0 = INDEX.size();
		
		if(var0 == 0) var0++;
		
		INDEX.putIfAbsent(var0, par1);
		
		return par1;
	}
	
	public static Page getPage(int par0) {
		return INDEX.getOrDefault(par0, new Page(new StringTextComponent("")));
	}
	
	public static int size() {
		return INDEX.size();
	}
}
