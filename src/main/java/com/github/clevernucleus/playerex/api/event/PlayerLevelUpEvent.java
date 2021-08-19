package com.github.clevernucleus.playerex.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;


public final class PlayerLevelUpEvent {
	
	
	public static final Event<PlayerLevelUpEvent.LevelUp> LEVEL_UP = EventFactory.createArrayBacked(PlayerLevelUpEvent.LevelUp.class, listeners -> player -> {
		for(LevelUp listener : listeners) {
			ActionResult result = listener.onLevelUp(player);
			
			if(result != ActionResult.PASS) {
				return result;
			}
		}
		
		return ActionResult.PASS;
	});
	
	
	@FunctionalInterface
	public interface LevelUp {
		
		
		ActionResult onLevelUp(final ServerPlayerEntity player);
	}
}
