package com.github.clevernucleus.playerex.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.clevernucleus.playerex.api.ExAPI;

import eu.pb4.placeholders.PlaceholderHandler;
import eu.pb4.placeholders.PlaceholderResult;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class StoredPlaceholder {	
	public static final Map<Identifier, PlaceholderHandler> STORE = new HashMap<Identifier, PlaceholderHandler>();
	
	private static void register(final String key, final PlaceholderHandler handler) {
		STORE.put(new Identifier(ExAPI.MODID, key), handler);
	}
	
	private static PlaceholderHandler specificLevelPlacement() {
		return ctx -> {
			MinecraftServer server = ctx.getServer();
			PlayerManager manager = server.getPlayerManager();
			EntityAttribute attribute = ExAPI.LEVEL.get();
			
			if(attribute == null) return PlaceholderResult.value("");
			
			List<ServerPlayerEntity> players = manager.getPlayerList(); 
			int size = players.size();
			int place = 1;
			
			if(ctx.hasArgument()) {
				try {
					int i = Integer.parseInt(ctx.getArgument());
					place = Math.max(1, i);
				} catch(NumberFormatException e) {
					return PlaceholderResult.value("");
				}
			}
			
			PlayerLevelPair[] pairs = new PlayerLevelPair[size];
			
			if(place > size) return PlaceholderResult.value(""); 
			
			for(int i = 0; i < size; i++) {
				ServerPlayerEntity player = players.get(i);
				AttributeContainer container = player.getAttributes();
				
				if(!container.hasAttribute(attribute)) continue;
				
				int level = Math.round((float)container.getValue(attribute));
				pairs[i] = new PlayerLevelPair(player, level);
			}
			
			PlayerEntity result = PlayerLevelPair.sort(pairs, place).getLeft();
			
			return PlaceholderResult.value(result.getName());
		};
	}
	
	static {
		register("level", ctx -> {
			PlayerEntity player = ctx.getPlayer();
			
			if(player == null) return PlaceholderResult.value("n/a");
			
			AttributeContainer container = player.getAttributes();
			EntityAttribute attribute = ExAPI.LEVEL.get();
			
			if(attribute == null || !container.hasAttribute(attribute)) return PlaceholderResult.value("n/a");
			
			int level = Math.round((float)container.getValue(attribute));
			
			return PlaceholderResult.value(String.valueOf(level));
		});
		
		register("level_top", specificLevelPlacement());
	}
}
