package com.github.clevernucleus.playerex;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.util.ConfigSetting;
import com.github.clevernucleus.playerex.util.IProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(ExAPI.MODID)
public class PlayerEx {
	
	/** Proxy instance to get side specific methods. */
	public static final IProxy PROXY = DistExecutor.safeRunForDist(() -> com.github.clevernucleus.playerex.client.ClientProxy::new, () -> com.github.clevernucleus.playerex.util.ServerProxy::new);
	
	public PlayerEx() {
		ModLoadingContext.get().registerConfig(Type.COMMON, ConfigSetting.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(Type.CLIENT, ConfigSetting.CLIENT_SPEC);
	}
}
