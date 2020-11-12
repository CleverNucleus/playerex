package github.clevernucleus.playerex.common.util;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface for things that must be run on the Side Distributer (ugh!).
 */
public interface IProxy {
	
	/**
	 * @return The client player if on the client; null otherwise.
	 */
	PlayerEntity clientPlayer();
}
