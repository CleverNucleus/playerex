package com.github.clevernucleus.playerex.client;

import org.apache.commons.lang3.tuple.Pair;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ClientConfig {
	
	/** Initialised instance of the forge client config specifications. */
	public static final ForgeConfigSpec CLIENT_SPEC;
	
	/** Initialised instance of our client config. */
	public static final Client CLIENT;
	
	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();
	}
	
	/**
	 * The client config file.
	 */
	public static class Client {
		
		/** Is the custom gui enabled. */
		public final BooleanValue enableHUD, enableHealthBar, enableFoodInfo;
		public final IntValue guiButtonX, guiButtonY;
		
		public Client(ForgeConfigSpec.Builder par0) {
			par0.push("HUD");
			
			this.enableHUD = par0.comment("Set to false to disable the custom HUD.").translation(ExAPI.MODID + ".config.client.enablehud").define("HUD", true);
			this.enableHealthBar = par0.comment("Set to false to disable the custom health bar only.").translation(ExAPI.MODID + ".config.client.enablehealthbar").define("enableHealthBar", true);
			this.enableFoodInfo = par0.comment("Set to false to disable the food/saturation information provided when AppleSkin is installed.").translation(ExAPI.MODID + ".config.client.enablefoodinfo").define("enableFoodInfo", true);
			
			par0.pop();
			par0.push("GUI");
			
			this.guiButtonX = par0.comment("The x-position on the gui of the playerex attributes button.").translation(ExAPI.MODID + ".config.client.guibuttonx").defineInRange("guiButtonPosX", 155, -200, 200);
			this.guiButtonY = par0.comment("The y-position on the gui of the playerex attributes button.").translation(ExAPI.MODID + ".config.client.guibuttony").defineInRange("guiButtonPosY", 7, -200, 200);
			
			par0.pop();
		}
	}
}
