package com.github.clevernucleus.playerex.util;

import org.apache.commons.lang3.tuple.Pair;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class CommonConfig {
	
	/** Initialised instance of the forge common config specifications. */
	public static final ForgeConfigSpec COMMON_SPEC;
	
	/** Initialised instance of our common config. */
	public static final Common COMMON;
	
	static {
		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}
	
	/**
	 * The common config file (for the server really, but needs to be available to the client).
	 */
	public static class Common {
		
		/** Starting attribute. */
		public final IntValue constitution, strength, dexterity, intelligence, luckiness;
		/** expCoeff parameters. */
		public final DoubleValue offset, scale;
		/** reset attributes on death */
		public final BooleanValue resetOnDeath;
		
		public final BooleanValue maxHealth, knockbackRes, healthRegen, meleeDamage, armor, rangedDamage, attackSpeed, movementSpeed, meleeCritDamage, healthRegenAmp, rangedCritDamage, lifesteal, luck, meleeCritChance, rangedCritChance, evasion;
		
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
			par0.comment("These are adder/modifier functions implemented by default. This is a stopgap config option to disable some until datadriven mechanics are released.").push("functions");
			par0.comment("If you want to add custom attributes, use the API.");
			
			this.maxHealth = par0.comment("Added on Constitution").worldRestart().define("addMaxHealth", true);
			this.knockbackRes = par0.comment("Added on Constitution").worldRestart().define("addKnockbackRes", true);
			this.healthRegen = par0.comment("Added on Strength").worldRestart().define("addHealthRegen", true);
			this.meleeDamage = par0.comment("Added on Strength").worldRestart().define("addMeleeDamage", true);
			this.armor = par0.comment("Added on Strength").worldRestart().define("addArmor", true);
			this.rangedDamage = par0.comment("Added on Dexterity").worldRestart().define("addRangedDamage", true);
			this.attackSpeed = par0.comment("Added on Dexterity").worldRestart().define("addAttackSpeed", true);
			this.movementSpeed = par0.comment("Added on Dexterity").worldRestart().define("addMovementSpeed", true);
			this.meleeCritDamage = par0.comment("Added on Dexterity").worldRestart().define("addMeleeCritDamage", true);
			this.healthRegenAmp = par0.comment("Added on Intelligence").worldRestart().define("addHealthRegenAmp", true);
			this.rangedCritDamage = par0.comment("Added on Intelligence").worldRestart().define("addRangedCritDamage", true);
			this.lifesteal = par0.comment("Added on Intelligence").worldRestart().define("addLifesteal", true);
			this.luck = par0.comment("Added on Luckiness").worldRestart().define("addLuck", true);
			this.meleeCritChance = par0.comment("Added on Luckiness").worldRestart().define("addMeleeCritChance", true);
			this.rangedCritChance = par0.comment("Added on Luckiness").worldRestart().define("addRangedCritChance", true);
			this.evasion = par0.comment("Added on Luckiness").worldRestart().define("addEvasion", true);
			
			par0.pop();
			par0.push("misc");
			
			this.resetOnDeath = par0
					.comment("If true, resets all attributes to their defaults on death.")
					.worldRestart()
					.define("resetOnDeath", false);
			
			par0.pop();
			par0.push("expcoeff");
			
			this.offset = par0
					.comment("ExpCoeff offset - cannot and should not be set to 0 or less than 0.")
					.translation(ExAPI.MODID + ".config.common.offset")
					.worldRestart()
					.defineInRange("offset", 3D, 1D, 20D);
			this.scale = par0
					.comment("ExpCoeff scaling - cannot and should not be set to 0 or less than 0.")
					.translation(ExAPI.MODID + ".config.common.scale")
					.worldRestart()
					.defineInRange("scale", 1D, 1D, 20D);
			par0.pop();
		}
	}
}
