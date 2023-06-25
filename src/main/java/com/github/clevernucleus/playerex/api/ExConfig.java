package com.github.clevernucleus.playerex.api;

import net.minecraft.entity.player.PlayerEntity;

/**
 * PlayerEx config access.
 * 
 * @author CleverNucleus
 *
 */
public interface ExConfig {
	
	/**
	 * Requires world restart. Server option, synced to Client.
	 * @return 100: resets all PlayerEx modifiers to their defaults; 0: no change to PlayerEx 
	 * modifiers; else: percentage of skill points kept.
	 */
	int resetOnDeath();
	
	/**
	 * Requires world restart. Server option, synced to Client.
	 * @return true: the PlayerEx attributes gui cannot be accessed; false: it can be accessed.
	 */
	boolean isAttributesGuiDisabled();
	
	/**
	 * Requires world restart. Server option, synced to Client.
	 * @return The number of Skill Points received upon levelling up.
	 */
	int skillPointsPerLevelUp();
	
	/**
	 * Dependent on Level Formula option. Requires world restart. Server option, synced to Client.
	 * @param player
	 * @return The number of vanilla experience levels to level up.
	 */
	int requiredXp(final PlayerEntity player);
	
	/**
	 * @return The number of ticks taken for a chunk to restore the experience negation factor.
	 */
	int restorativeForceTicks();
	
	/**
	 * @return The restorative force multiplier.
	 */
	float restorativeForceMultiplier();
	
	/**
	 * @return The multiplier for experience negation.
	 */
	float expNegationFactor();
	
	/**
	 * Client option.
	 * @return 0 - 1.5. Volume multiplier for level up event.
	 */
	float levelUpVolume();
	
	/**
	 * Client option.
	 * @return 0 - 1.5. Volume multiplier for skill up event.
	 */
	float skillUpVolume();
	
	/**
	 * Client option.
	 * @return true if inventory tabs are disabled; else false.
	 */
	boolean disableInventoryTabs();

	/**
	 * Client option.
	 * @return 0 - 0.75. Size multiplier for PlayerEx gui text in the x-axis.
	 */
	float textScaleX();
	
	/**
	 * Client option.
	 * @return 0 - 0.75. Size multiplier for PlayerEx gui text in the y-axis.
	 */
	float textScaleY();
	
	/**
	 * Client option.
	 * @return If nameplates have been enabled by the server, this determines for the client the height offset.
	 */
	float levelNameplateHeight();
}
