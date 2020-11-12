package github.clevernucleus.playerex.common.util;

import org.apache.commons.lang3.tuple.Pair;

import github.clevernucleus.playerex.common.PlayerEx;
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
			
			this.enableGui = par0.comment("Set to false to disable the custom gui.").translation(PlayerEx.MODID + ".config.client.enablegui").define("enableGui", true);
			
			par0.pop();
		}
	}
	
	/**
	 * The common config file (for the server really, but needs to be available to the client).
	 */
	public static class Common {
		
		/** Starting attribute. */
		public final IntValue constitution, strength, dexterity, intelligence, luck;
		
		/** Relic Drop Chance. */
		public final IntValue dropChance;
		
		/** Enable/Disable relic/potion mob drops. */
		public final BooleanValue enableDivineDrops, enableCurioDrops, enablePotionDrops;
		
		public Common(ForgeConfigSpec.Builder par0) {
			par0.comment("These only have an affect when a player is first joining the world.").push("attributes");
			
			this.constitution = par0
					.comment("Starting Constitution (also starting hp in half hearts) - cannot and should not be set to 0 or less than 0.")
					.translation(PlayerEx.MODID + ".config.common.constitution")
					.worldRestart()
					.defineInRange("constitution", 6, 1, 20);
			this.strength = par0
					.comment("Starting Strength - cannot and should not be set to less than 0.")
					.translation(PlayerEx.MODID + ".config.common.strength")
					.worldRestart()
					.defineInRange("strength", 0, 0, 20);
			this.dexterity = par0
					.comment("Starting Dexterity - cannot and should not be set to less than 0.")
					.translation(PlayerEx.MODID + ".config.common.dexterity")
					.worldRestart()
					.defineInRange("dexterity", 0, 0, 20);
			this.intelligence = par0
					.comment("Starting Intelligence - cannot and should not be set to less than 0.")
					.translation(PlayerEx.MODID + ".config.common.intelligence")
					.worldRestart()
					.defineInRange("intelligence", 0, 0, 20);
			this.luck = par0
					.comment("Starting Luck - cannot and should not be set to less than 0.")
					.translation(PlayerEx.MODID + ".config.common.luck")
					.worldRestart()
					.defineInRange("luck", 0, 0, 20);
			
			par0.pop();
			par0.push("drops");
			
			this.dropChance = par0
					.comment("Percentage chance for a mob to drop a relic or health potion - set to 0 to disable all drops.")
					.translation(PlayerEx.MODID + ".config.common.dropchance")
					.defineInRange("dropChance", 10, 0, 100);
			
			this.enableDivineDrops = par0.comment("Set to false to disable mobs dropping divine relics.").translation(PlayerEx.MODID + ".config.common.enabledivinedrops").define("enableDivineDrops", true);
			this.enableCurioDrops = par0.comment("Set to false to disable mobs dropping curio relics.").translation(PlayerEx.MODID + ".config.common.enablecuriodrops").define("enableCurioDrops", true);
			this.enablePotionDrops = par0.comment("Set to false to disable mobs dropping health potions.").translation(PlayerEx.MODID + ".config.common.enablepotiondrops").define("enablePotionDrops", true);
			
			par0.pop();
		}
	}
}
