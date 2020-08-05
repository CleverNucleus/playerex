package clevernucleus.playerex.common;

import clevernucleus.playerex.common.util.IProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

/**
 * Mod init class; holds the modid.
 */
@Mod(PlayerEx.MODID)
public class PlayerEx {
	
	/** The modid used to identify playerex. */
	public static final String MODID = "playerex";
	
	/** Proxy instance to get side specific methods. */
	public static final IProxy PROXY = DistExecutor.runForDist(() -> clevernucleus.playerex.client.ClientProxy::new, () -> clevernucleus.playerex.server.ServerProxy::new);
	
	public PlayerEx() {}
}
