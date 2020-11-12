package github.clevernucleus.playerex.client;

import github.clevernucleus.playerex.common.util.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Client Side implementation of the common proxy interface.
 */
public class ClientProxy implements IProxy {
	
	@Override
	public PlayerEntity clientPlayer() {
		return Minecraft.getInstance().player;
	}
}
