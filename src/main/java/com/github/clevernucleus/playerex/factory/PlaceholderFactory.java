package com.github.clevernucleus.playerex.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.opc.api.OfflinePlayerCache;
import com.github.clevernucleus.playerex.api.ExAPI;

import eu.pb4.placeholders.PlaceholderHandler;
import eu.pb4.placeholders.PlaceholderResult;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class PlaceholderFactory {
	public static final Map<Identifier, PlaceholderHandler> STORE = new HashMap<Identifier, PlaceholderHandler>();
	
	private static NameLevelPair nameLevelPair(final MinecraftServer server, final Collection<String> namesIn, final int indexIn) {
		return OfflinePlayerCache.getOfflinePlayerCache(server, new NameLevelPair("", 0), opc -> {
			NameLevelPair[] names = new NameLevelPair[namesIn.size()];
			int i = 0;
			
			for(String name : namesIn) {
				names[i++] = new NameLevelPair(name, opc.get(name, ExAPI.LEVEL_VALUE));
			}
			
			Arrays.sort(names, Comparator.comparing(pair -> pair.level));
			int j = MathHelper.clamp(indexIn, 1, names.length);
			return names[names.length - j];
		});
	}
	
	private static PlaceholderHandler top(Function<NameLevelPair, String> function) {
		return ctx -> {
			MinecraftServer server = ctx.getServer();
			Collection<String> names = OfflinePlayerCache.getOfflinePlayerCache(server, Collections.emptySet(), opc -> opc.playerNames());
			int index = 1;
			
			if(ctx.hasArgument()) {
				try {
					int i = Integer.parseInt(ctx.getArgument());
					index = Math.max(1, i);
				} catch(NumberFormatException e) {
					return PlaceholderResult.invalid("Invalid argument!");
				}
			}
			
			if(index > names.size()) return PlaceholderResult.value("");
			NameLevelPair pair = nameLevelPair(server, names, index);
			return PlaceholderResult.value(function.apply(pair));
		};
	}
	
	static {
		STORE.put(new Identifier(ExAPI.MODID, "level"), ctx -> {
			ServerPlayerEntity player = ctx.getPlayer();
			
			if(player == null) return PlaceholderResult.invalid("No player!");
			int level = DataAttributesAPI.ifPresent(player, ExAPI.LEVEL, 0, value -> (int)Math.round(value));
			return PlaceholderResult.value(String.valueOf(level));
		});
		STORE.put(new Identifier(ExAPI.MODID, "name_top"), top(pair -> pair.name));
		STORE.put(new Identifier(ExAPI.MODID, "level_top"), top(pair -> String.valueOf(pair.level)));
	}
	
	private static final class NameLevelPair {
		protected final String name;
		protected final int level;
		
		protected NameLevelPair(final String name, final int level) {
			this.name = name;
			this.level = level;
		}
	}
}
