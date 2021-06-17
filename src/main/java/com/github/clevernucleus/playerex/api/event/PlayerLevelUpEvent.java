package com.github.clevernucleus.playerex.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * This event is hooked into the RETURN of {@link PlayerEntity#addExperience(int)}
 * 
 * @author CleverNucleus
 *
 */
public final class PlayerLevelUpEvent {
	
	/**
	 * Event fired when the player first gains enough vanilla experience to spend on a playerex level.
	 */
	public static final Event<PlayerLevelUpEvent.LevelUp> LEVEL_UP = EventFactory.createArrayBacked(PlayerLevelUpEvent.LevelUp.class, listeners -> player -> {
		for(LevelUp listener : listeners) {
			listener.onLevelUp(player);
		}
	});
	
	/**
	 * The functional interface to provide the method hook.
	 * 
	 * @author CleverNucleus
	 *
	 */
	@FunctionalInterface
	public interface LevelUp {
		
		/**
		 * Event method;
		 * @param player
		 */
		void onLevelUp(PlayerEntity player);
	}
}
