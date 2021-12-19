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
	 * Fired when determining if the player's attack is critical. Return true if it is critical, return false if it is not.
	 */
	public static final Event<AttackCritBoolean> ATTACK_CRIT_BOOLEAN = EventFactory.createArrayBacked(AttackCritBoolean.class, listeners -> (player, target, vanilla) -> {
		for(AttackCritBoolean listener : listeners) {
			if(listener.shouldCrit(player, target, vanilla)) {
				return true;
			}
		}
		
		return false;
	});
	
	/**
	 * Fired if the player lands a critical hit. The result is the damage.
	 */
	public static final Event<AttackCritDamage> ATTACK_CRIT_DAMAGE = EventFactory.createArrayBacked(AttackCritDamage.class, listeners -> (player, target, amount) -> {
		float previous = amount;
		
		for(AttackCritDamage listener : listeners) {
			previous = listener.onCritDamage(player, target, previous);
		}
		
		return previous;
	});
	
	@FunctionalInterface
	public interface AttackCritBoolean {
		boolean shouldCrit(final PlayerEntity player, final Entity target, final boolean vanilla);
	}
	
	@FunctionalInterface
	public interface AttackCritDamage {
		float onCritDamage(final PlayerEntity player, final Entity target, final float amount);
	}
}
