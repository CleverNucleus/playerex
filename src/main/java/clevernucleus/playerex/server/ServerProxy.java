package clevernucleus.playerex.server;

import clevernucleus.playerex.common.util.IProxy;
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
