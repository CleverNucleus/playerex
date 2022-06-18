package com.github.clevernucleus.playerex.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Some useful hooks into player gameplay exposed for compatibility.
 * 
 * @author CleverNucleus
 *
 */
public final class PlayerEntityEvents {
	
	/**
	 * Fired if the player lands a critical hit. The result is the damage.
	 */
	public static final Event<AttackCritDamage> ON_CRIT = EventFactory.createArrayBacked(AttackCritDamage.class, callbacks -> (player, target, amount) -> {
		float previous = amount;
		
		for(AttackCritDamage callback : callbacks) {
			previous = callback.onCrit(player, target, previous);
		}
		
		return previous;
	});
	
	/**
	 * Fired when determining if the player's attack is critical. Return true if it is critical, return false if it is not.
	 */
	public static final Event<AttackCritBoolean> SHOULD_CRIT = EventFactory.createArrayBacked(AttackCritBoolean.class, callbacks -> (player, target, vanilla) -> {
		for(AttackCritBoolean callback : callbacks) {
			if(callback.shouldCrit(player, target, vanilla)) return true;
		}
		
		return false;
	});
	
	@FunctionalInterface
	public interface AttackCritDamage {
		float onCrit(final PlayerEntity player, final Entity target, final float amount);
	}
	
	@FunctionalInterface
	public interface AttackCritBoolean {
		boolean shouldCrit(final PlayerEntity player, final Entity target, final boolean vanilla);
	}
}
