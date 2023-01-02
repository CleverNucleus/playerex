package com.github.clevernucleus.playerex.api;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;

import com.github.clevernucleus.opc.api.CacheableValue;
import com.github.clevernucleus.opc.api.OfflinePlayerCache;
import com.github.clevernucleus.playerex.api.damage.DamageFunction;
import com.github.clevernucleus.playerex.api.damage.DamagePredicate;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.player.PlayerEntity;
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
	/** Round value to integer property attached to some attributes. */
	public static final String INTEGER_PROPERTY = "integer";
	/** Multiply value by property and append % symbol. */
	public static final String PERCENTAGE_PROPERTY = "percentage";
	/** Multiply value by property. */
	public static final String MULTIPLIER_PROPERTY = "multiplier";
	/** The UUID PlayerEx modifiers use. */
	public static final UUID PLAYEREX_MODIFIER_ID = UUID.fromString("0f320cdd-8b2e-47a6-917e-adca8f899495");
	/** The CacheableValue key to access player's level. */
	public static final CacheableValue<Integer> LEVEL_VALUE = OfflinePlayerCache.register(new com.github.clevernucleus.playerex.impl.LevelValue());
	/** The Cardinal Components Key for PlayerEx player data. */
	public static final ComponentKey<PlayerData> PLAYER_DATA = ComponentRegistry.getOrCreate(new Identifier(MODID, "player_data"), PlayerData.class);
	/** The Cardinal Components Key for PlayerEx experience data. */
	public static final ComponentKey<ExperienceData> EXPERIENCE_DATA = ComponentRegistry.getOrCreate(new Identifier(MODID, "experience_data"), ExperienceData.class);
	
	public static final EntityAttributeSupplier LEVEL = define("level");
	public static final EntityAttributeSupplier CONSTITUTION = define("constitution");
	public static final EntityAttributeSupplier STRENGTH = define("strength");
	public static final EntityAttributeSupplier DEXTERITY = define("dexterity");
	public static final EntityAttributeSupplier INTELLIGENCE = define("intelligence");
	public static final EntityAttributeSupplier LUCKINESS = define("luckiness");
	public static final EntityAttributeSupplier EVASION = define("evasion");
	public static final EntityAttributeSupplier LIFESTEAL = define("lifesteal");
	public static final EntityAttributeSupplier HEALTH_REGENERATION = define("health_regeneration");
	public static final EntityAttributeSupplier HEAL_AMPLIFICATION = define("heal_amplification");
	public static final EntityAttributeSupplier MELEE_CRIT_DAMAGE = define("melee_crit_damage");
	public static final EntityAttributeSupplier MELEE_CRIT_CHANCE = define("melee_crit_chance");
	public static final EntityAttributeSupplier RANGED_CRIT_DAMAGE = define("ranged_crit_damage");
	public static final EntityAttributeSupplier RANGED_CRIT_CHANCE = define("ranged_crit_chance");
	public static final EntityAttributeSupplier RANGED_DAMAGE = define("ranged_damage");
	public static final EntityAttributeSupplier FIRE_RESISTANCE = define("fire_resistance");
	public static final EntityAttributeSupplier FREEZE_RESISTANCE = define("freeze_resistance");
	public static final EntityAttributeSupplier LIGHTNING_RESISTANCE = define("lightning_resistance");
	public static final EntityAttributeSupplier POISON_RESISTANCE = define("poison_resistance");
	public static final EntityAttributeSupplier WITHER_RESISTANCE = define("wither_resistance");
	/** Implemented for the player only. */
	public static final EntityAttributeSupplier BREAKING_SPEED = define("breaking_speed");
	/** If JamieWhiteShirt reach-entity-attributes exist then this accesses the reach distance attribute. */
	public static final EntityAttributeSupplier REACH_DISTANCE = EntityAttributeSupplier.of(new Identifier("reach-entity-attributes:reach"));
	/** If JamieWhiteShirt reach-entity-attributes exist then this accesses the attack range attribute. */
	public static final EntityAttributeSupplier ATTACK_RANGE = EntityAttributeSupplier.of(new Identifier("reach-entity-attributes:attack_range"));
	
	/** PlayerEx config access. Yes I know, AutoConfig on the server too?? Ewww. It works it is fine. */
	public static ExConfig getConfig() {
		return AutoConfig.getConfigHolder(com.github.clevernucleus.playerex.config.ConfigImpl.class).get();
	}
	
	/**
	 * Registers a damage modification condition that is applied to living entities under specific circumstances.
	 * @param predicate Using the incoming damage conditions, determines whether the damage modification function 
	 * should be applied.
	 * @param function Using the incoming damage conditions, modifies the incoming damage before it actually damages.
	 */
	public static void registerDamageModification(final DamagePredicate predicate, final DamageFunction function) {
		com.github.clevernucleus.playerex.impl.DamageModificationImpl.add(predicate, function);
	}
	
	/**
	 * Registers a refund condition. Refund conditions tell the game what can be refunded and what the maximum number of 
	 * refund points are for a given circumstance.
	 * @param refundCondition
	 */
	public static void registerRefundCondition(final BiFunction<PlayerData, PlayerEntity, Double> refundCondition) {
		com.github.clevernucleus.playerex.impl.RefundConditionImpl.add(refundCondition);
	}
	
	/**
	 * @return Returns all the registered refund conditions. Note that while this is mutable and backed by the original registry, 
	 * you should avoid modification and treat as read-only!
	 * @since 3.5.0
	 */
	public static Collection<BiFunction<PlayerData, PlayerEntity, Double>> getRefundConditions() {
		return com.github.clevernucleus.playerex.impl.RefundConditionImpl.get();
	}
	
	private static EntityAttributeSupplier define(final String path) {
		return EntityAttributeSupplier.of(new Identifier(MODID, path));
	}
}
