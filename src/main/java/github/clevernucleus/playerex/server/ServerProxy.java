package github.clevernucleus.playerex.server;

import github.clevernucleus.playerex.common.util.IProxy;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Server side implementation of the common proxy.
 */
public class ServerProxy implements IProxy {
	
	@Override
	public PlayerEntity clientPlayer() {
		return null;
	}
}
