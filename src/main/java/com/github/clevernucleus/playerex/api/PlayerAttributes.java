package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.api.attribute.IAttribute;

import net.minecraft.util.Identifier;

/**
 * A repository of all attributes that the player has access to in the game by default. Developers should use these to access 
 * attributes preferentially to the repository EntityAttributes.
 * 
 * If any of these attributes (e.g. PlayerAttributes{@link #EVASION}) are removed from the game by a datapack, these entry objects
 * won't be affected as they are suppliers.
 * 
 * @author CleverNucleus
 * 
 */
public final class PlayerAttributes {
	/** Vanilla max health */
	public static final IAttribute MAX_HEALTH = find(new Identifier("generic.max_health"));
	/** Vanilla knockback resistance */
	public static final IAttribute KNOCKBACK_RESISTANCE = find(new Identifier("generic.knockback_resistance"));
	/** Vanilla movement speed */
	public static final IAttribute MOVEMENT_SPEED = find(new Identifier("generic.movement_speed"));
	/** Vanilla attack damage */
	public static final IAttribute MELEE_ATTACK_DAMAGE = find(new Identifier("generic.attack_damage"));
	/** Vanilla attack speed */
	public static final IAttribute ATTACK_SPEED = find(new Identifier("generic.attack_speed"));
	/** Jar-in-jar reach-entity-attributes reach distance */
	public static final IAttribute REACH_DISTANCE = find(new Identifier("reach-entity-attributes", "reach"));
	/** Jar-in-jar reach-entity-attributes attack range */
	public static final IAttribute ATTACK_RANGE = find(new Identifier("reach-entity-attributes", "attack_range"));
	/** Vanilla armor */
	public static final IAttribute ARMOR = find(new Identifier("generic.armor"));
	/** Vanilla armour toughness */
	public static final IAttribute ARMOR_TOUGHNESS = find(new Identifier("generic.armor_toughness"));
	/** Vanilla luck */
	public static final IAttribute LUCK = find(new Identifier("generic.luck"));
	/** PlayerEx player level */
	public static final IAttribute LEVEL = find(new Identifier(ExAPI.MODID, "level"));
	/** PlayerEx skill points */
	public static final IAttribute SKILLPOINTS = find(new Identifier(ExAPI.MODID, "skillpoints"));
	/** PlayerEx constitution */
	public static final IAttribute CONSTITUTION = find(new Identifier(ExAPI.MODID, "constitution"));
	/** PlayerEx strength */
	public static final IAttribute STRENGTH = find(new Identifier(ExAPI.MODID, "strength"));
	/** PlayerEx dexterity */
	public static final IAttribute DEXTERITY = find(new Identifier(ExAPI.MODID, "dexterity"));
	/** PlayerEx intelligence */
	public static final IAttribute INTELLIGENCE = find(new Identifier(ExAPI.MODID, "intelligence"));
	/** PlayerEx luckiness (different from vanilla luck) */
	public static final IAttribute LUCKINESS = find(new Identifier(ExAPI.MODID, "luckiness"));
	/** PlayerEx health regeneration */
	public static final IAttribute HEALTH_REGEN = find(new Identifier(ExAPI.MODID, "health_regeneration"));
	/** PlayerEx heal amplification */
	public static final IAttribute HEAL_AMPLIFICATION = find(new Identifier(ExAPI.MODID, "heal_amplification"));
	/** PlayerEx fire damage resistance */
	public static final IAttribute FIRE_RESISTANCE = find(new Identifier(ExAPI.MODID, "fire_resistance"));
	/** PlayerEx falling damage resistance */
	public static final IAttribute FALLING_RESISTANCE = find(new Identifier(ExAPI.MODID, "falling_resistance"));
	/** PlayerEx drowning damage resistance */
	public static final IAttribute DROWNING_RESISTANCE = find(new Identifier(ExAPI.MODID, "drowning_resistance"));
	/** PlayerEx wither damage resistance */
	public static final IAttribute WITHER_RESISTANCE = find(new Identifier(ExAPI.MODID, "wither_resistance"));
	/** PlayerEx magic damage (including poison) resistance */
	public static final IAttribute MAGIC_RESISTANCE = find(new Identifier(ExAPI.MODID, "magic_resistance"));
	/** PlayerEx melee crit damage */
	public static final IAttribute MELEE_CRIT_DAMAGE = find(new Identifier(ExAPI.MODID, "melee_crit_damage"));
	/** PlayerEx melee crit chance */
	public static final IAttribute MELEE_CRIT_CHANCE = find(new Identifier(ExAPI.MODID, "melee_crit_chance"));
	/** PlayerEx projectile evasion chance */
	public static final IAttribute EVASION = find(new Identifier(ExAPI.MODID, "evasion"));
	/** PlayerEx melee attack damage lifesteal */
	public static final IAttribute LIFESTEAL = find(new Identifier(ExAPI.MODID, "lifesteal"));
	/** PlayerEx ranged bonus hit damage */
	public static final IAttribute RANGED_DAMAGE = find(new Identifier(ExAPI.MODID, "ranged_damage"));
	/** PlayerEx ranged crit damage */
	public static final IAttribute RANGED_CRIT_DAMAGE = find(new Identifier(ExAPI.MODID, "ranged_crit_damage"));
	/** PlayerEx ranged crit chance */
	public static final IAttribute RANGED_CRIT_CHANCE = find(new Identifier(ExAPI.MODID, "ranged_crit_chance"));
	/** PlayerEx magic damage amplification */
	public static final IAttribute MAGIC_AMPLIFICATION = find(new Identifier(ExAPI.MODID, "magic_amplification"));
	/** Jar-in-jar step-height-entity-attribute step height */
	public static final IAttribute STEP_HEIGHT = find(new Identifier("stepheightentityattribute", "stepheight"));
	
	/**
	 * @param keyIn Player Attribute key
	 * @return A supplier instance providing access to the attribute if it exists.
	 */
	public static IAttribute find(final Identifier keyIn) {
		return () -> ExAPI.REGISTRY.get().getAttribute(keyIn);
	}
}
