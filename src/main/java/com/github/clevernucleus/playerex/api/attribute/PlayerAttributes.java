package com.github.clevernucleus.playerex.api.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.logging.log4j.util.TriConsumer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.Limit;
import com.github.clevernucleus.playerex.api.TriFunction;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;

/**
 * Use this to refer to player attributes and to register new ones and to register adders.
 */
public class PlayerAttributes {
	private static final List<IPlayerAttribute> ATTRIBUTES = new ArrayList<IPlayerAttribute>();
	private static final Multimap<ResourceLocation, TriConsumer<PlayerEntity, IPlayerAttributes, Double>> ADD_CONSUMERS = ArrayListMultimap.create();
	private static final Multimap<ResourceLocation, TriConsumer<PlayerEntity, TriFunction<PlayerEntity, IPlayerAttribute, AttributeModifier, IPlayerAttributes>, AttributeModifier>> MOD_CONSUMERS = ArrayListMultimap.create();
	
	public static final IPlayerAttribute EXPERIENCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "experience"), UUID.fromString("b5ff159c-5968-46f7-8bce-b1536a57c1ff"), Limit.none(), IPlayerAttribute.Type.DATA, () -> (new RangedAttribute("attribute.name.playerex.experience", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute LEVEL = registerAttribute(new ResourceLocation(ExAPI.MODID, "level"), UUID.fromString("c76e5f55-a45a-435b-974e-c41fb357e32f"), Limit.none(), IPlayerAttribute.Type.DATA, () -> (new RangedAttribute("attribute.name.playerex.level", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute SKILLPOINTS = registerAttribute(new ResourceLocation(ExAPI.MODID, "skillpoints"), UUID.fromString("694d1ce6-e270-48ed-a38f-a6e561a56b7b"), Limit.none(), IPlayerAttribute.Type.DATA, () -> (new RangedAttribute("attribute.name.playerex.skillpoints", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute CONSTITUTION = registerAttribute(new ResourceLocation(ExAPI.MODID, "constitution"), UUID.fromString("7bf2cca9-ea8b-41d2-8aaf-3aa7b2808a0b"), Limit.hold(1D, 1D, 10D, 0.7D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.constitution", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute STRENGTH = registerAttribute(new ResourceLocation(ExAPI.MODID, "strength"), UUID.fromString("ebae169d-7c82-4170-bb81-cd5bff7e3e8d"), Limit.hold(1D, 1D, 10D, 0.8D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.strength", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute DEXTERITY = registerAttribute(new ResourceLocation(ExAPI.MODID, "dexterity"), UUID.fromString("71999133-e76e-4b48-a722-8a3d774ec8f9"), Limit.hold(1D, 1D, 10D, 0.8D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.dexterity", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute INTELLIGENCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "intelligence"), UUID.fromString("17d3dae1-c005-4560-866b-12ecf49007b9"), Limit.hold(1D, 1D, 10D, 0.7D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.intelligence", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute LUCKINESS = registerAttribute(new ResourceLocation(ExAPI.MODID, "luckiness"), UUID.fromString("f0cc3694-6da7-42aa-9b30-57f569489b9f"), Limit.hold(1D, 1D, 10D, 0.7D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.luckiness", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute MAX_HEALTH = registerAttribute(new ResourceLocation("max_health"), UUID.fromString("f62377b7-d349-457a-9fc6-16ea143e0915"), Limit.hold(1D, 1D, 10D, 0.65D), IPlayerAttribute.Type.GAME, () -> Attributes.MAX_HEALTH);
	public static final IPlayerAttribute HEALTH_REGEN = registerAttribute(new ResourceLocation(ExAPI.MODID, "health_regen"), UUID.fromString("b1a380bf-48d6-4057-9715-1da2d1b79577"), Limit.hold(0.0005D, 0.0005D, 0.005D, 0.5D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.health_regen", 0D, ((-1D) * Integer.MAX_VALUE), Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute HEALTH_REGEN_AMP = registerAttribute(new ResourceLocation(ExAPI.MODID, "health_regen_amp"), UUID.fromString("f702b7d7-b8b4-4fdf-955e-f7b0e9fe081d"), Limit.hold(0.01D, 0.01D, 0.25D, 0.4D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.health_regen_amp", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute ARMOR = registerAttribute(new ResourceLocation("armor"), UUID.fromString("4183dc71-af3f-43b2-92c8-e04aab003df8"), Limit.hold(1D, 1D, 10D, 0.6D), IPlayerAttribute.Type.GAME, () -> Attributes.ARMOR);
	public static final IPlayerAttribute ARMOR_TOUGHNESS = registerAttribute(new ResourceLocation("armor_toughness"), UUID.fromString("7ecab8f3-c381-48a8-b838-01166ce2a9fe"), Limit.hold(0.5D, 0.5D, 6D, 0.45D), IPlayerAttribute.Type.GAME, () -> Attributes.ARMOR_TOUGHNESS);
	public static final IPlayerAttribute KNOCKBACK_RESISTANCE = registerAttribute(new ResourceLocation("knockback_resistance"), UUID.fromString("840cfc88-09ec-470c-9cf1-632a5b006bda"), Limit.hold(0.01D, 0.01D, 0.25D, 0.5D), IPlayerAttribute.Type.GAME, () -> Attributes.KNOCKBACK_RESISTANCE);
	public static final IPlayerAttribute DAMAGE_REDUCTION = registerAttribute(new ResourceLocation(ExAPI.MODID, "damage_reduction"), UUID.fromString("c22f250c-4e40-4a3e-b231-c3f437be0103"), Limit.hold(0.01D, 0.01D, 0.25D, 0.3D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.damage_reduction", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute FIRE_RESISTANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "fire_resistance"), UUID.fromString("d90270ad-239b-41d8-9bbd-427babaa2daa"), Limit.hold(0.01D, 0.01D, 0.25D, 0.7D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.fire_resistance", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute LAVA_RESISTANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "lava_resistance"), UUID.fromString("06aa25a0-3e7e-4e96-8b1a-47ff35d89fe6"), Limit.hold(0.01D, 0.01D, 0.25D, 0.6D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.lava_resistance", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute EXPLOSION_RESISTANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "explosion_resistance"), UUID.fromString("839ea2e1-cc06-4513-896a-d5e6ed946e64"), Limit.hold(0.01D, 0.01D, 0.25D, 0.6D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.explosion_resistance", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute FALLING_RESISTANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "falling_resistance"), UUID.fromString("712ddd97-57e1-4816-b5bb-bcb063c778c9"), Limit.hold(0.01D, 0.01D, 0.25D, 0.4D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.falling_resistance", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute POISON_RESISTANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "poison_resistance"), UUID.fromString("e5435bd0-075e-4609-a147-6c18050f652c"), Limit.hold(0.01D, 0.01D, 0.25D, 0.5D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.poison_resistance", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute WITHER_RESISTANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "wither_resistance"), UUID.fromString("6d95c602-edef-4b2e-9c00-1ccdda1ab979"), Limit.hold(0.01D, 0.01D, 0.25D, 0.4D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.wither_resistance", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute DROWNING_RESISTANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "drowning_resistance"), UUID.fromString("ec3adb01-5bea-4f98-aeb0-848e71441c71"), Limit.hold(0.01D, 0.01D, 0.25D, 0.4D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.drowning_resistance", 0D, ((-1D) * Integer.MAX_VALUE), 1D)).setShouldWatch(true));
	public static final IPlayerAttribute MOVEMENT_SPEED = registerAttribute(new ResourceLocation("movement_speed"), UUID.fromString("70e02e84-e55b-4452-bed6-d7ec9141b95b"), Limit.hold(0.004D, 0.004D, 0.1D, 0.35D), IPlayerAttribute.Type.GAME, () -> Attributes.MOVEMENT_SPEED);
	public static final IPlayerAttribute MELEE_DAMAGE = registerAttribute(new ResourceLocation("melee_damage"), UUID.fromString("608ce27e-c0ad-41c0-af72-0e14bdef431b"), Limit.hold(0.25D, 0.25D, 4D, 0.5D), IPlayerAttribute.Type.GAME, () -> Attributes.ATTACK_DAMAGE);
	public static final IPlayerAttribute MELEE_CRIT_DAMAGE = registerAttribute(new ResourceLocation(ExAPI.MODID, "melee_crit_damage"), UUID.fromString("6ac065ba-47d0-4393-9053-4ebfa232ebbc"), Limit.hold(0.05D, 0.05D, 0.3D, 0.4D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.melee_crit_damage", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute MELEE_CRIT_CHANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "melee_crit_chance"), UUID.fromString("e0a8db40-8680-4b66-a81c-44dfa7520d8a"), Limit.hold(0.01D, 0.01D, 0.25D, 0.3D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.melee_crit_chance", 0D, 0D, 1D)).setShouldWatch(true));
	public static final IPlayerAttribute ATTACK_SPEED = registerAttribute(new ResourceLocation("attack_speed"), UUID.fromString("a27f0fc8-e975-4220-bd0a-4f045933fc13"), Limit.hold(0.25D, 0.25D, 2D, 0.6D), IPlayerAttribute.Type.GAME, () -> Attributes.ATTACK_SPEED);
	public static final IPlayerAttribute GRAVITY = registerAttribute(new ResourceLocation("forge", "gravity"), UUID.fromString("1c1e3b0c-665f-462a-a38d-205e61003ec1"), Limit.hold(0.002D, 0.002D, 0.06D, 0.05D), IPlayerAttribute.Type.GAME, ForgeMod.ENTITY_GRAVITY);
	public static final IPlayerAttribute EVASION = registerAttribute(new ResourceLocation(ExAPI.MODID, "evasion"), UUID.fromString("6618d0f3-9312-4399-8aa1-6829202685e9"), Limit.hold(0.01D, 0.01D, 0.5D, 0.3D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.evasion", 0D, 0D, 1D)).setShouldWatch(true));
	public static final IPlayerAttribute RANGED_DAMAGE = registerAttribute(new ResourceLocation(ExAPI.MODID, "ranged_damage"), UUID.fromString("a5e9b729-49c8-4f26-b37b-782e581edae3"), Limit.hold(0.25D, 0.25D, 4D, 0.5D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.ranged_damage", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute RANGED_CRIT_DAMAGE = registerAttribute(new ResourceLocation(ExAPI.MODID, "ranged_crit_damage"), UUID.fromString("2c05461a-48f5-4806-af14-4d3c1229eede"), Limit.hold(0.05D, 0.05D, 0.3D, 0.4D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.ranged_crit_damage", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute RANGED_CRIT_CHANCE = registerAttribute(new ResourceLocation(ExAPI.MODID, "ranged_crit_chance"), UUID.fromString("e1cc6b06-5667-4df7-8298-350f2a6c6f4b"), Limit.hold(0.01D, 0.01D, 0.25D, 0.3D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.ranged_crit_chance", 0D, 0D, 1D)).setShouldWatch(true));
	public static final IPlayerAttribute LIFESTEAL = registerAttribute(new ResourceLocation(ExAPI.MODID, "lifesteal"), UUID.fromString("749e2d1a-687d-45ca-a0f2-1a3bd9460b12"), Limit.hold(0.01D, 0.01D, 0.25D, 0.25D), IPlayerAttribute.Type.ALL, () -> (new RangedAttribute("attribute.name.playerex.lifesteal", 0D, 0D, Integer.MAX_VALUE)).setShouldWatch(true));
	public static final IPlayerAttribute LUCK = registerAttribute(new ResourceLocation("luck"), UUID.fromString("7b35348d-4266-4ec0-9980-30a51fcd4c67"), Limit.hold(1D, 1D, 10D, 0.5D), IPlayerAttribute.Type.GAME, () -> Attributes.LUCK);
	public static final IPlayerAttribute REACH_DISTANCE = registerAttribute(new ResourceLocation("forge", "reach_distance"), UUID.fromString("c4eab154-b99d-496d-8fe9-2fd47daa7cb0"), Limit.hold(0.5D, 0.5D, 2D, 0.2D), IPlayerAttribute.Type.GAME, ForgeMod.REACH_DISTANCE);
	public static final IPlayerAttribute SWIM_SPEED = registerAttribute(new ResourceLocation("forge", "swim_speed"), UUID.fromString("0f7b2e3f-fab9-4b97-9fb0-7d3ab53b0ef7"), Limit.hold(0.1D, 0.1D, 1D, 0.35D), IPlayerAttribute.Type.GAME, ForgeMod.SWIM_SPEED);
	
	/**
	 * Builds and registers a new player attribute using the input parameters. Can be statically initialised.
	 * @param par0 The attribute's registry name in the form (YourMod.MODID, "attribute_name").
	 * @param par1 The attribute's UUID.
	 * @param par2 The attribute limit object: this is purely for external use and does not affect the attribute's functions at all.
	 * @param par3 The attribute's data behaviour; if in doubt pick {@link IPlayerAttribute.Type#ALL}. Registers a new attribute with Forge if {@link IPlayerAttribute.Type#GAME} is NOT the input.
	 * @param par4 The Supplier to the attribute object.
	 * @return A fully registered player attribute. 
	 */
	public static IPlayerAttribute registerAttribute(final ResourceLocation par0, final UUID par1, final Limit par2, final IPlayerAttribute.Type par3, final Supplier<Attribute> par4) {
		IPlayerAttribute var0 = new PlayerAttribute(par0, par1, par2, par3, par4);
		
		if(par3 == IPlayerAttribute.Type.ALL || par3 == IPlayerAttribute.Type.DATA) {
			Attribute var1 = par4.get();
			
			var1.setRegistryName(par0);
			
			var0 = new PlayerAttribute(par0, par1, par2, par3, () -> var1);
		}
		
		ATTRIBUTES.add(var0);
		
		return var0;
	}
	
	/**
	 * Registers an adder method that will be run during(after) {@link IPlayerAttributes#add(PlayerEntity, IPlayerAttribute, double)}
	 * @param par0 The IPlayerAttribute's registry name, which can either be manually defined or from {@link IPlayerAttribute#registryName()}.
	 * @param par1 The TriConsumer that will take in the player instance, the attribute capability and the amount that was added to the attribute with the input registry name.
	 */
	public static void registerAdder(final ResourceLocation par0, final TriConsumer<PlayerEntity, IPlayerAttributes, Double> par1) {
		ADD_CONSUMERS.put(par0, par1);
	}
	
	/**
	 * Registers a modifier method that will be run during(after) {@link IPlayerAttributes#applyModifier(PlayerEntity, IPlayerAttribute, AttributeModifier)}
	 * @param par0 The IPlayerAttribute's registry name, which can either be manually defined or from {@link IPlayerAttribute#registryName()}.
	 * @param par1 The TriConsumer that will take in the player instance, the attribute capability and the modifier that was applied to the attribute with the input registry name.
	 */
	public static void registerModifier(final ResourceLocation par0, final TriConsumer<PlayerEntity, TriFunction<PlayerEntity, IPlayerAttribute, AttributeModifier, IPlayerAttributes>, AttributeModifier> par1) {
		MOD_CONSUMERS.put(par0, par1);
	}
	
	/**
	 * @param par0 RegistryName String : {@link ResourceLocation#toString()}
	 * @return Returns the IPlayerAttribute instance with the input registry name if it exists; if not, returns null.
	 */
	public static IPlayerAttribute fromRegistryName(String par0) {
		for(IPlayerAttribute var : ATTRIBUTES) {
			if(var.toString().equals(par0)) return var;
		}
		
		return null;
	}
	
	/**
	 * @return An immutable copy of the ATTRIBUTES list.
	 */
	public static Collection<IPlayerAttribute> attributes() {
		return ImmutableList.copyOf(ATTRIBUTES);
	}
	
	/**
	 * @return An immutable copy of the ADD_CONSUMERS Multimap.
	 */
	public static Multimap<ResourceLocation, TriConsumer<PlayerEntity, IPlayerAttributes, Double>> adders() {
		return ImmutableListMultimap.copyOf(ADD_CONSUMERS);
	}
	
	/**
	 * @return An immutable copy of the MOD_CONSUMERS Multimap.
	 */
	public static Multimap<ResourceLocation, TriConsumer<PlayerEntity, TriFunction<PlayerEntity, IPlayerAttribute, AttributeModifier, IPlayerAttributes>, AttributeModifier>> modifiers() {
		return ImmutableListMultimap.copyOf(MOD_CONSUMERS);
	}
}
