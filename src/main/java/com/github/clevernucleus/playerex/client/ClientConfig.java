package com.github.clevernucleus.playerex.client;

import org.apache.commons.lang3.tuple.Pair;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

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
		public final BooleanValue enableGui;
		
		public Client(ForgeConfigSpec.Builder par0) {
			par0.push("enableGui");
			
			this.enableGui = par0.comment("Set to false to disable the custom gui.").translation(ExAPI.MODID + ".config.client.enablegui").define("enableGui", true);
			
			par0.pop();
		}
	}
}
