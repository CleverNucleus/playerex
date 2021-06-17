package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.util.ExRegistry;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import net.minecraft.util.Identifier;

/**
 * A repository of all attributes that the player has access to in the game by default. Developers should use these to access 
 * attributes preferentially to the repository EntityAttributes.
 * 
 * @author CleverNucleus
 * 
 */
public final class PlayerAttributesProvider {
	private PlayerAttributes attributes;
	private boolean initialised;
	
	protected PlayerAttributesProvider() {
		this.initialised = false;
	}
	
	/**
	 * @return Gets the repository.
	 */
	public PlayerAttributes get() {
		return this.attributes;
	}
	
	/**
	 * For internal use only; avoid using.
	 */
	@Deprecated
	public void build() {
		if(this.initialised) return;
		this.attributes = new PlayerAttributes();
		this.initialised = true;
	}
	
	/**
	 * A repository of all attributes that the player has access to in the game by default. Developers should use these to access 
	 * attributes preferentially to the repository EntityAttributes.
	 * 
	 * @author CleverNucleus
	 * 
	 */
	public final class PlayerAttributes {
		/** Vanilla max health */
		public final IAttribute maxHealth;
		/** Vanilla knockback resistance */
		public final IAttribute knockbackResistance;
		/** Vanilla movement speed */
		public final IAttribute movementSpeed;
		/** Vanilla attack damage */
		public final IAttribute meleeAttackDamage;
		/** Vanilla attack speed */
		public final IAttribute attackSpeed;
		/** Jar-in-jar reach-entity-attributes reach distance */
		public final IAttribute reachDistance;
		/** Jar-in-jar reach-entity-attributes attack range */
		public final IAttribute attackRange;
		/** Vanilla armor */
		public final IAttribute armor;
		/** Vanilla armour toughness */
		public final IAttribute armorToughness;
		/** Vanilla luck */
		public final IAttribute luck;
		/** PlayerEx player level */
		public final IAttribute level;
		/** PlayerEx skill points */
		public final IAttribute skillPoints;
		/** PlayerEx constitution */
		public final IAttribute constitution;
		/** PlayerEx strength */
		public final IAttribute strength;
		/** PlayerEx dexterity */
		public final IAttribute dexterity;
		/** PlayerEx intelligence */
		public final IAttribute intelligence;
		/** PlayerEx luckiness (different from vanilla luck) */
		public final IAttribute luckiness;
		/** PlayerEx health regeneration */
		public final IAttribute healthRegeneration;
		/** PlayerEx heal amplification */
		public final IAttribute healAmplification;
		/** PlayerEx fire damage resistance*/
		public final IAttribute fireResistance;
		/** PlayerEx falling damage resistance */
		public final IAttribute fallingResistance;
		/** PlayerEx drowning damage resistance */
		public final IAttribute drowningResistance;
		/** PlayerEx wither damage resistance */
		public final IAttribute witherResistance;
		/** PlayerEx magic damage (including poison) resistance */
		public final IAttribute magicResistance;
		/** PlayerEx melee crit damage */
		public final IAttribute meleeCritDamage;
		/** PlayerEx melee crit chance */
		public final IAttribute meleeCritChance;
		/** PlayerEx projectile evasion chance */
		public final IAttribute evasion;
		/** PlayerEx melee attack damage lifesteal */
		public final IAttribute lifesteal;
		/** PlayerEx ranged bonus hit damage */
		public final IAttribute rangedDamage;
		/** PlayerEx ranged crit damage */
		public final IAttribute rangedCritDamage;
		/** PlayerEx ranged crit chance */
		public final IAttribute rangedCritChance;
		
		private PlayerAttributes() {
			ExRegistry registry = ExAPI.REGISTRY.get();
			
			this.maxHealth = registry.getAttribute(new Identifier("generic.max_health"));
			this.knockbackResistance = registry.getAttribute(new Identifier("generic.knockback_resistance"));
			this.movementSpeed = registry.getAttribute(new Identifier("generic.movement_speed"));
			this.meleeAttackDamage = registry.getAttribute(new Identifier("generic.attack_damage"));
			this.attackSpeed = registry.getAttribute(new Identifier("generic.attack_speed"));
			this.reachDistance = registry.getAttribute(new Identifier(ReachEntityAttributes.MOD_ID, "reach"));
			this.attackRange = registry.getAttribute(new Identifier(ReachEntityAttributes.MOD_ID, "attack_range"));
			this.armor = registry.getAttribute(new Identifier("generic.armor"));
			this.armorToughness = registry.getAttribute(new Identifier("generic.armor_toughness"));
			this.luck = registry.getAttribute(new Identifier("generic.luck"));
			this.level = registry.getAttribute(new Identifier(ExAPI.MODID, "level"));
			this.skillPoints = registry.getAttribute(new Identifier(ExAPI.MODID, "skillpoints"));
			this.constitution = registry.getAttribute(new Identifier(ExAPI.MODID, "constitution"));
			this.strength = registry.getAttribute(new Identifier(ExAPI.MODID, "strength"));
			this.dexterity = registry.getAttribute(new Identifier(ExAPI.MODID, "dexterity"));
			this.intelligence = registry.getAttribute(new Identifier(ExAPI.MODID, "intelligence"));
			this.luckiness = registry.getAttribute(new Identifier(ExAPI.MODID, "luckiness"));
			this.healthRegeneration = registry.getAttribute(new Identifier(ExAPI.MODID, "health_regeneration"));
			this.healAmplification = registry.getAttribute(new Identifier(ExAPI.MODID, "heal_amplification"));
			this.fireResistance = registry.getAttribute(new Identifier(ExAPI.MODID, "fire_resistance"));
			this.fallingResistance = registry.getAttribute(new Identifier(ExAPI.MODID, "falling_resistance"));
			this.drowningResistance = registry.getAttribute(new Identifier(ExAPI.MODID, "drowning_resistance"));
			this.witherResistance = registry.getAttribute(new Identifier(ExAPI.MODID, "wither_resistance"));
			this.magicResistance = registry.getAttribute(new Identifier(ExAPI.MODID, "magic_resistance"));
			this.meleeCritDamage = registry.getAttribute(new Identifier(ExAPI.MODID, "melee_crit_damage"));
			this.meleeCritChance = registry.getAttribute(new Identifier(ExAPI.MODID, "melee_crit_chance"));
			this.evasion = registry.getAttribute(new Identifier(ExAPI.MODID, "evasion"));
			this.lifesteal = registry.getAttribute(new Identifier(ExAPI.MODID, "lifesteal"));
			this.rangedDamage = registry.getAttribute(new Identifier(ExAPI.MODID, "ranged_damage"));
			this.rangedCritDamage = registry.getAttribute(new Identifier(ExAPI.MODID, "ranged_crit_damage"));
			this.rangedCritChance = registry.getAttribute(new Identifier(ExAPI.MODID, "ranged_crit_chance"));
		}
	}
}
