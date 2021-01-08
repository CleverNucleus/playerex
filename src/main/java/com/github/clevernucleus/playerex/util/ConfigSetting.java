package com.github.clevernucleus.playerex.util;

import org.apache.commons.lang3.tuple.Pair;

import com.github.clevernucleus.playerex.api.ExAPI;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigSetting {
	
	/** Initialised instance of the forge client config specifications. */
	public static final ForgeConfigSpec CLIENT_SPEC;
	
	/** Initialised instance of our client config. */
	public static final Client CLIENT;
	
	/** Initialised instance of the forge common config specifications. */
	public static final ForgeConfigSpec COMMON_SPEC;
	
	/** Initialised instance of our common config. */
	public static final Common COMMON;
	
	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();
	}
	
	static {
		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}
	
	/**
	 * The client config file.
	 */
	public static class Client {
		
		/** Is the custom gui enabled. */
		public final BooleanValue enableGui;
		
		public Client(ForgeConfigSpec.Builder par0) {
			par0.push("enableGui");
			
			this.enableGui = par0.comment("Set to false to disable the custom gui.").translation(ExAPI.MODID + ".config.client.enablegui").define("enableGui", true);
			
			par0.pop();
		}
	}
	
	/**
	 * The common config file (for the server really, but needs to be available to the client).
	 */
	public static class Common {
		
		/** Starting attribute. */
		public final IntValue constitution, strength, dexterity, intelligence, luckiness;
		
		public Common(ForgeConfigSpec.Builder par0) {
			par0.comment("These only have an affect when a player is first joining the world.").push("attributes");
			
			this.constitution = par0
					.comment("Starting Constitution (also starting hp in half hearts) - cannot and should not be set to 0 or less than 0.")
					.translation(ExAPI.MODID + ".config.common.constitution")
					.worldRestart()
					.defineInRange("constitution", 6, 1, 20);
			this.strength = par0
					.comment("Starting Strength - cannot and should not be set to less than 0.")
					.translation(ExAPI.MODID + ".config.common.strength")
					.worldRestart()
					.defineInRange("strength", 0, 0, 20);
			this.dexterity = par0
					.comment("Starting Dexterity - cannot and should not be set to less than 0.")
					.translation(ExAPI.MODID + ".config.common.dexterity")
					.worldRestart()
					.defineInRange("dexterity", 0, 0, 20);
			this.intelligence = par0
					.comment("Starting Intelligence - cannot and should not be set to less than 0.")
					.translation(ExAPI.MODID + ".config.common.intelligence")
					.worldRestart()
					.defineInRange("intelligence", 0, 0, 20);
			this.luckiness = par0
					.comment("Starting Luckiness - cannot and should not be set to less than 0.")
					.translation(ExAPI.MODID + ".config.common.luckiness")
					.worldRestart()
					.defineInRange("luckiness", 0, 0, 20);
			
			par0.pop();
		}
	}
}
