package com.github.clevernucleus.playerex.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PersistentPlayerCache;

import eu.pb4.placeholders.PlaceholderContext;
import eu.pb4.placeholders.PlaceholderHandler;
import eu.pb4.placeholders.PlaceholderResult;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class StoredPlaceholder {	
	public static final Map<Identifier, PlaceholderHandler> STORE = new HashMap<Identifier, PlaceholderHandler>();
	
	private static void register(final String key, final PlaceholderHandler handler) {
		STORE.put(new Identifier(ExAPI.MODID, key), handler);
	}
	
	private static PlaceholderResult result(PlaceholderContext ctx, Function<NameLevelPair, String> type) {
		PersistentPlayerCache cache = ExAPI.persistentPlayerCache(ctx.getServer());
		NameLevelPair[] pairs = cache.get();
		int size = pairs.length;
		int index = 1;
		
		if(ctx.hasArgument()) {
			try {
				int i = Integer.parseInt(ctx.getArgument());
				index = Math.max(1, i);
			} catch(NumberFormatException e) {
				return PlaceholderResult.invalid("Invalid argument!");
			}
		}
		
		if(index > size) return PlaceholderResult.value(""); 
		NameLevelPair pair = NameLevelPair.sort(pairs, index);
		
		return PlaceholderResult.value(type.apply(pair));
	}
	
	private static PlaceholderHandler topLevel() {
		return ctx -> result(ctx, NameLevelPair::level);
	}
	
	private static PlaceholderHandler topName() {
		return ctx -> result(ctx, NameLevelPair::name);
	}
	
	static {
		register("level", ctx -> {
			ServerPlayerEntity player = ctx.getPlayer();
			
			if(player == null) return PlaceholderResult.invalid("Null player!");
			
			PersistentPlayerCache cache = ExAPI.persistentPlayerCache(ctx.getServer());
			NameLevelPair pair = cache.get(player);
			
			return PlaceholderResult.value(pair.level());
		});
		register("name_top", topName());
		register("level_top", topLevel());
	}
}
