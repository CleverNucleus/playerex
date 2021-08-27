package com.github.clevernucleus.playerex.api;

import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.API;
import com.github.clevernucleus.playerex.api.config.ExConfigProvider;
import com.github.clevernucleus.playerex.api.config.IConfig;

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
	/** Display formatting property attached to some attributes. */
	public static final String PERCENTAGE_PROPERTY = "percent";
	/** Display formatting property attached to some attributes. */
	public static final String MULTIPLIER_PROPERTY = "multiplier";
	/** The Cardinal Components Key for PlayerEx modifier data. */
	public static final ComponentKey<ModifierData> DATA = ComponentRegistry.getOrCreate(new Identifier(MODID, "data"), ModifierData.class);
	/** The config supplier object. */
	public static final IConfig.Provider CONFIG = new ExConfigProvider();
	
	public static final Supplier<EntityAttribute> LEVEL = API.getAttribute(new Identifier(MODID, "level"));
	public static final Supplier<EntityAttribute> SKILL_POINTS = API.getAttribute(new Identifier(MODID, "skill_points"));
	public static final Supplier<EntityAttribute> CONSTITUTION = API.getAttribute(new Identifier(MODID, "constitution"));
	public static final Supplier<EntityAttribute> STRENGTH = API.getAttribute(new Identifier(MODID, "strength"));
	public static final Supplier<EntityAttribute> DEXTERITY = API.getAttribute(new Identifier(MODID, "dexterity"));
	public static final Supplier<EntityAttribute> INTELLIGENCE = API.getAttribute(new Identifier(MODID, "intelligence"));
	public static final Supplier<EntityAttribute> LUCKINESS = API.getAttribute(new Identifier(MODID, "luckiness"));
	public static final Supplier<EntityAttribute> EVASION = API.getAttribute(new Identifier(MODID, "evasion"));
	public static final Supplier<EntityAttribute> LIFESTEAL = API.getAttribute(new Identifier(MODID, "lifesteal"));
	public static final Supplier<EntityAttribute> HEALTH_REGENERATION = API.getAttribute(new Identifier(MODID, "health_regeneration"));
	public static final Supplier<EntityAttribute> HEAL_AMPLIFICATION = API.getAttribute(new Identifier(MODID, "heal_amplification"));
	public static final Supplier<EntityAttribute> MAGIC_AMPLIFICATION = API.getAttribute(new Identifier(MODID, "magic_amplification"));
	public static final Supplier<EntityAttribute> MAGIC_RESISTANCE = API.getAttribute(new Identifier(MODID, "magic_resistance"));
	public static final Supplier<EntityAttribute> FIRE_RESISTANCE = API.getAttribute(new Identifier(MODID, "fire_resistance"));
	public static final Supplier<EntityAttribute> FREEZE_RESISTANCE = API.getAttribute(new Identifier(MODID, "freeze_resistance"));
	public static final Supplier<EntityAttribute> DROWNING_RESISTANCE = API.getAttribute(new Identifier(MODID, "drowning_resistance"));
	public static final Supplier<EntityAttribute> FALLING_RESISTANCE = API.getAttribute(new Identifier(MODID, "falling_resistance"));
	public static final Supplier<EntityAttribute> WITHER_RESISTANCE = API.getAttribute(new Identifier(MODID, "wither_resistance"));
	public static final Supplier<EntityAttribute> MELEE_CRIT_CHANCE = API.getAttribute(new Identifier(MODID, "melee_crit_chance"));
	public static final Supplier<EntityAttribute> MELEE_CRIT_DAMAGE = API.getAttribute(new Identifier(MODID, "melee_crit_damage"));
	public static final Supplier<EntityAttribute> RANGED_CRIT_CHANCE = API.getAttribute(new Identifier(MODID, "ranged_crit_chance"));
	public static final Supplier<EntityAttribute> RANGED_CRIT_DAMAGE = API.getAttribute(new Identifier(MODID, "ranged_crit_damage"));
	public static final Supplier<EntityAttribute> RANGED_DAMAGE = API.getAttribute(new Identifier(MODID, "ranged_damage"));
}
