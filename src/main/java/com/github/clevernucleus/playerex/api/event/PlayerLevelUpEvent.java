package com.github.clevernucleus.playerex.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

/**
 * Provides a hook into the method {@link PlayerEntity#addExperience(int)}.
 * 
 * @author CleverNucleus
 *
 */
public final class PlayerLevelUpEvent {
	
	/**
	 * Fired ONCE on the server every time the player first gains enough xp to reach the next level. 
	 * For example:
	 * <br></br>
	 * If the player needs two experience levels to level up, then the instant the player first gains two experience levels, this event fires.
	 * If however, the player then continues to gain experience levels past the required number, this event does not fire again until the 
	 * player has skilled an attribute and the experience levels once again drop below the required number.
	 */
	public static final Event<PlayerLevelUpEvent.LevelUp> EVENT = EventFactory.createArrayBacked(PlayerLevelUpEvent.LevelUp.class, listeners -> player -> {
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
		
		/**
		 * 
		 * @param player
		 * @return
		 */
		ActionResult onLevelUp(final ServerPlayerEntity player);
	}
}
