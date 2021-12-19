package com.github.clevernucleus.playerex.api;

import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;

/**
 * Main API access for the PlayerEx API package.
 * 
 * @author CleverNucleus
 *
 */
public final class ExAPI {
	
	/** The PlayerEx mod Id */
	public static final String MODID = "playerex";
	/** The Cardinal Components Key for PlayerEx modifier data. */
	public static final ComponentKey<PlayerData> INSTANCE = ComponentRegistry.getOrCreate(new Identifier(MODID, "data"), PlayerData.class);
	
	public static final Supplier<EntityAttribute> LEVEL = define("level");//
	public static final Supplier<EntityAttribute> SKILL_POINTS = define("skill_points");//
	public static final Supplier<EntityAttribute> CONSTITUTION = define("constitution");//
	public static final Supplier<EntityAttribute> STRENGTH = define("strength");//
	public static final Supplier<EntityAttribute> DEXTERITY = define("dexterity");//
	public static final Supplier<EntityAttribute> INTELLIGENCE = define("intelligence");//
	public static final Supplier<EntityAttribute> LUCKINESS = define("luckiness");//
	public static final Supplier<EntityAttribute> EVASION = define("evasion");//
	public static final Supplier<EntityAttribute> LIFESTEAL = define("lifesteal");//
	public static final Supplier<EntityAttribute> HEALTH_REGENERATION = define("health_regeneration");
	public static final Supplier<EntityAttribute> HEAL_AMPLIFICATION = define("heal_amplification");
	public static final Supplier<EntityAttribute> MELEE_CRIT_DAMAGE = define("melee_crit_damage");
	public static final Supplier<EntityAttribute> MELEE_CRIT_CHANCE = define("melee_crit_chance");
	public static final Supplier<EntityAttribute> RANGED_CRIT_DAMAGE = define("ranged_crit_damage");//
	public static final Supplier<EntityAttribute> RANGED_CRIT_CHANCE = define("ranged_crit_chance");//
	public static final Supplier<EntityAttribute> RANGED_DAMAGE = define("ranged_damage");//
	
	private static Supplier<EntityAttribute> define(final String path) {
		return DataAttributesAPI.getAttribute(new Identifier(MODID, path));
	}
}
