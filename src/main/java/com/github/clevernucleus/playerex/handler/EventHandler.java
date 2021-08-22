package com.github.clevernucleus.playerex.handler;

import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttributeModifier;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.impl.ModifierDataManager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;

public final class EventHandler {
	public static void respawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		ModifierDataManager newData = (ModifierDataManager)ExAPI.DATA.get(newPlayer);
		ModifierDataManager oldData = (ModifierDataManager)ExAPI.DATA.get(oldPlayer);
		
		if(ExAPI.CONFIG.get().resetOnDeath()) {
			newData.reset();
		}
		
		newData.refresh(oldData);
	}
	
	public static void healthModified(final LivingEntity entity, final EntityAttribute attribute, final IEntityAttributeModifier modifier) {
		if(attribute == EntityAttributes.GENERIC_MAX_HEALTH) {
			float value = (float)modifier.getValue();
			int amount = Math.round(value);
			modifier.setValue(amount);
		}
	}
}
