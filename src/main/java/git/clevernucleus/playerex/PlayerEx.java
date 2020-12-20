package git.clevernucleus.playerex;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.util.IProxy;
import github.clevernucleus.playerex.common.util.ConfigSetting;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(ExAPI.MODID)
public class PlayerEx {
	
	/** Proxy instance to get side specific methods. */
	public static final IProxy PROXY = DistExecutor.safeRunForDist(() -> git.clevernucleus.playerex.client.ClientProxy::new, () -> git.clevernucleus.playerex.util.ServerProxy::new);
	
	public PlayerEx() {
		ModLoadingContext.get().registerConfig(Type.COMMON, ConfigSetting.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(Type.CLIENT, ConfigSetting.CLIENT_SPEC);
	}
}
