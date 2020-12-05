package git.clevernucleus.playerex.util;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;

public interface IProxy {
	
	/**
	 * @return An optional with empty conditional being a server player and a result being the client player.
	 */
	Optional<PlayerEntity> player();
}
