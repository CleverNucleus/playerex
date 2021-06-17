package com.github.clevernucleus.playerex.api.util;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;

import net.minecraft.entity.player.PlayerEntity;

/**
 * A helper class for maths.
 * 
 * @author CleverNucleus
 *
 */
public final class Maths {
	
	/**
	 * Adds or subtracts the current and adding values, with diminishing returns tending towards the limit.
	 * @param current Current value (add to or subtract from).
	 * @param adding Modifying value (+/-).
	 * @param limit The limiting value (output respects this, unless limit == 0).
	 * @return The diminishing returns.
	 */
	public static double add(final double current, final double adding, final double limit) {
		if(limit == 0.0D) return current;
		if(adding > 0.0D) return limit * ((current + adding) / (limit + adding));
		if(adding < 0.0D) return adding + current - (current * adding / limit);
		
		return current;
	}
	
	/**
	 * @param player
	 * @return The number of vanilla levels needed to reach the next PlayerEx level - dependent on server config options (synced to client).
	 */
	public static int requiredXp(PlayerEntity player) {
		AttributeData data = ExAPI.DATA.get(player);
		IConfig config = ExAPI.CONFIG.get();
		float level = (float)data.get(ExAPI.ATTRIBUTES.get().level.get());
		float mult = config.levelMultiplier();
		float offset = (float)config.levelOffset();
		float result = 8.0F + offset + (float)Math.pow((double)((mult * level) - 2.0F), 3.0D);
		
		return Math.round(result);
	}
}
