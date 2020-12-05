package git.clevernucleus.playerex;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.util.IProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ExAPI.MODID)
public class PlayerEx {
	
	/** Proxy instance to get side specific methods. */
	public static final IProxy PROXY = DistExecutor.safeRunForDist(() -> git.clevernucleus.playerex.client.ClientProxy::new, () -> git.clevernucleus.playerex.util.ServerProxy::new);
	
	public PlayerEx() {}
}
