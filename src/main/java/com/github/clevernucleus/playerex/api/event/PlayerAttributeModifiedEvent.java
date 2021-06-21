package com.github.clevernucleus.playerex.api.event;

import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Access to an event that occurs when an attribute's value changes (in ANY way, including modifiers).
 * 
 * @author CleverNuclues
 *
 */
public final class PlayerAttributeModifiedEvent {
	
	/**
	 * Fired when any attribute's value changes (by any means, including with modifiers. Base value changes do not trigger this however).
	 * This only get's called on the server.
	 */
	public static final Event<PlayerAttributeModifiedEvent.Modified> MODIFIED = EventFactory.createArrayBacked(PlayerAttributeModifiedEvent.Modified.class, listeners -> (player, attribute, oldValue, newValue) -> {
		for(Modified listener : listeners) {
			listener.onAttributeModified(player, attribute, oldValue, newValue);
		}
	});
	
	@FunctionalInterface
	public interface Modified {
		
		/**
		 * FIRED on {@link PlayerAttributeModifiedEvent#MODIFIED}
		 * The method parameters are final and should not be modified. For example you can't change the final value by setting the parameter.
		 * This occurs on the server only, and AFTER all logic that modified the value has been done (i.e. at this point the current value of the attribute is the final/new value).
		 * @param player The player (server-side).
		 * @param attribute The attribute which has had its value modified
		 * @param oldValue the initial value before modification
		 * @param newValue the final value after modification.
		 */
		void onAttributeModified(final PlayerEntity player, final IPlayerAttribute attribute, final double oldValue, final double newValue);
	}
}
