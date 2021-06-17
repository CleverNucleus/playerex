package com.github.clevernucleus.playerex.api.event;

import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public final class PlayerAttributeModifiedEvent {
	
	
	public static final Event<PlayerAttributeModifiedEvent.Modified> MODIFIED = EventFactory.createArrayBacked(PlayerAttributeModifiedEvent.Modified.class, listeners -> (player, attribute, oldValue, newValue) -> {
		for(Modified listener : listeners) {
			listener.onAttributeModified(player, attribute, oldValue, newValue);
		}
	});
	
	
	@FunctionalInterface
	public interface Modified {
		
		
		void onAttributeModified(PlayerEntity player, IPlayerAttribute attribute, double oldValue, double newValue);
	}
}
