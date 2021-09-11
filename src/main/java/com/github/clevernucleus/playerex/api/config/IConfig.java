package com.github.clevernucleus.playerex.api.config;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;

/**
 * The config interface for PlayerEx. Provides a layer of abstraction.
 * 
 * @author CleverNucleus
 *
 */
public interface IConfig {
	
	/**
	 * Serverside - value syncs to client; requires restart to change value.
	 * @return If true, all attribute/modifiers provided by PlayerEx reset on death.
	 */
	boolean resetOnDeath();
	
	/**
	 * Serverside - value syncs to client; requires restart to change value.
	 * @return If true, players on multiplayer will display their level as a nameplate below their username.
	 */
	boolean showLevelNameplates();
	
	/**
	 * Serverside - value syncs to client; requires restart to change value.
	 * @param player
	 * @return Returns the number of levels required to reach the next level, depending on the config's level formula.
	 */
	int requiredXp(final PlayerEntity player);
	
	/**
	 * Clientside.
	 * @return How loud the levelup sound effect is - ranged from 0 (mute) to 1.5(max)
	 */
	float levelUpVolume();
	
	/**
	 * Clientside.
	 * @return How loud the skillup sound effect is - ranged from 0 (mute) to 1.5(max)
	 */
	float skillUpVolume();
	
	/**
	 * Clientside.
	 * @return The horizontal position of the inventory button.
	 */
	int inventoryButtonPosX();
	
	/**
	 * Clientside.
	 * @return The vertical position of the inventory button.
	 */
	int inventoryButtonPosY();
	
	/**
	 * Clientside.
	 * @return The horizontal squeeze of the gui text.
	 */
	float textSqueezeX();
	
	/**
	 * Clientside.
	 * @return The vertical squeeze of the gui text.
	 */
	float textSqueezeY();
	
	/**
	 * Clientside.
	 * @return Fixes item tooltips to show the correct attack damage/speed.
	 */
	boolean enableTooltipFix();
	
	@FunctionalInterface
	interface Provider extends Supplier<IConfig> {}
}
