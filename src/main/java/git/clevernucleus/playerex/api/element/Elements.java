package git.clevernucleus.playerex.api.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.util.TriConsumer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import git.clevernucleus.playerex.api.ExAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Main API for modders. Allows the dev to register/edit/add/get elements.
 */
public class Elements {
	protected static final BiMap<ResourceLocation, Element> ELEMENT_REGISTRY = HashBiMap.create();
	protected static final Map<ResourceLocation, BiFunction<PlayerEntity, IPlayerElements, Float>> GET_REGISTRY = Maps.newHashMap();
	protected static final Map<ResourceLocation, List<TriConsumer<PlayerEntity, IPlayerElements, Float>>> ADD_REGISTRY = Maps.newHashMap();
	protected static final Map<ResourceLocation, List<TriConsumer<PlayerEntity, IPlayerElements, Float>>> SET_REGISTRY = Maps.newHashMap();
	
	public static final Element EXPERIENCE = registerElement(new ResourceLocation(ExAPI.MODID, "experience"), new Element(0F, 0F, 0F, Element.Type.DATA));
	public static final Element LEVEL = registerElement(new ResourceLocation(ExAPI.MODID, "level"), new Element(0F, 0F, 0F, Element.Type.DATA));
	public static final Element SKILLPOINTS = registerElement(new ResourceLocation(ExAPI.MODID, "skillpoints"), new Element(0F, 0F, 0F, Element.Type.DATA));
	public static final Element CONSTITUTION = registerElement(new ResourceLocation(ExAPI.MODID, "constitution"), new Element(0F, 1F, 10F, Element.Type.ALL));
	public static final Element STRENGTH = registerElement(new ResourceLocation(ExAPI.MODID, "strength"), new Element(0F, 1F, 10F, Element.Type.ALL));
	public static final Element DEXTERITY = registerElement(new ResourceLocation(ExAPI.MODID, "dexterity"), new Element(0F, 1F, 10F, Element.Type.ALL));
	public static final Element INTELLIGENCE = registerElement(new ResourceLocation(ExAPI.MODID, "intelligence"), new Element(0F, 0F, 10F, Element.Type.ALL));
	public static final Element LUCKINESS = registerElement(new ResourceLocation(ExAPI.MODID, "luckiness"), new Element(0F, 1F, 10F, Element.Type.ALL));
	public static final Element HEALTH = registerElement(new ResourceLocation("health"), new Element(0F, 1F, 10F, Element.Type.GAME));
	public static final Element HEALTH_REGEN = registerElement(new ResourceLocation(ExAPI.MODID, "health_regen"), new Element(0F, 0.0001F, 0.001F, Element.Type.ALL));
	public static final Element HEALTH_REGEN_AMP = registerElement(new ResourceLocation(ExAPI.MODID, "health_regen_amp"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element ARMOR = registerElement(new ResourceLocation("armor"), new Element(0F, 1F, 10F, Element.Type.GAME));
	public static final Element ARMOR_TOUGHNESS = registerElement(new ResourceLocation("armor_toughness"), new Element(0F, 0.25F, 4F, Element.Type.GAME));
	public static final Element KNOCKBACK_RESISTANCE = registerElement(new ResourceLocation("knockback_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.GAME));
	public static final Element DAMAGE_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "damage_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element FIRE_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "fire_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element LAVA_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "lava_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element EXPLOSION_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "explosion_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element FALLING_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "falling_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element POISON_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "poison_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element WITHER_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "wither_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element DROWNING_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "drowning_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element MOVEMENT_SPEED_AMP = registerElement(new ResourceLocation(ExAPI.MODID, "movement_speed_amp"), new Element(0F, 0.01F, 0.4F, Element.Type.ALL));
	public static final Element MELEE_DAMAGE = registerElement(new ResourceLocation("melee_damage"), new Element(0F, 0.01F, 0.2F, Element.Type.GAME));
	public static final Element MELEE_CRIT_DAMAGE = registerElement(new ResourceLocation(ExAPI.MODID, "melee_crit_damage"), new Element(0F, 0.05F, 0.5F, Element.Type.ALL));
	public static final Element MELEE_CRIT_CHANCE = registerElement(new ResourceLocation(ExAPI.MODID, "melee_crit_chance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element ATTACK_SPEED = registerElement(new ResourceLocation("attack_speed"), new Element(0F, 0.25F, 4F, Element.Type.GAME));
	public static final Element EVASION_CHANCE = registerElement(new ResourceLocation(ExAPI.MODID, "evasion_chance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element RANGED_DAMAGE = registerElement(new ResourceLocation(ExAPI.MODID, "ranged_damage"), new Element(0F, 0.25F, 4F, Element.Type.ALL));
	public static final Element RANGED_CRIT_DAMAGE = registerElement(new ResourceLocation(ExAPI.MODID, "ranged_crit_damage"), new Element(0F, 0.05F, 0.5F, Element.Type.ALL));
	public static final Element RANGED_CRIT_CHANCE = registerElement(new ResourceLocation(ExAPI.MODID, "ranged_crit_chance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element LIFESTEAL = registerElement(new ResourceLocation(ExAPI.MODID, "lifesteal"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL));
	public static final Element LUCK = registerElement(new ResourceLocation("luck"), new Element(0F, 1F, 10F, Element.Type.GAME, (par0, par1) -> 0F));
	
	/**
	 * Registers an element to the API.
	 * @param par0 unique id to represent the element in the registry. Modders should use 'ResourceLocation(MODID, elementName")'.
	 * @param par1 The element object to register.
	 * @return The input element object, allows for inline static registering.
	 */
	public static Element registerElement(final ResourceLocation par0, final @Nonnull Element par1) {
		ELEMENT_REGISTRY.putIfAbsent(par0, par1);
		
		return par1;
	}
	
	/**
	 * Registers the getter function for the element. Cannot override elements that already have a getter function.
	 * Elements can only have one getter function.
	 * @param par0 The element's registry id.
	 * @param par1 The result to calling get on the element. Means that the element does not necessarily have to call their data store.
	 */
	public static void registerGetFunction(final ResourceLocation par0, final @Nonnull BiFunction<PlayerEntity, IPlayerElements, Float> par1) {
		GET_REGISTRY.putIfAbsent(par0, par1);
	}
	
	/**
	 * Registers an adder function for the element. Elements can have multiple adder functions from multiple sources.
	 * @param par0 The element's registry id.
	 * @param par1 The code performed when calling add. Allows modders to add their own hooks into elements.
	 */
	public static void registerAddFunction(final ResourceLocation par0, @Nonnull TriConsumer<PlayerEntity, IPlayerElements, Float> par1) {
		List<TriConsumer<PlayerEntity, IPlayerElements, Float>> var0 = ADD_REGISTRY.getOrDefault(par0, new ArrayList<TriConsumer<PlayerEntity, IPlayerElements, Float>>());
		
		var0.add(par1);
		
		ADD_REGISTRY.put(par0, var0);
	}
	
	/**
	 * Registers a setter function for the element. Elements can have multiple setter functions from multiple sources.
	 * This should resemble an adder function.
	 * @param par0 The element's registry id.
	 * @param par1 The code performed when calling set. Allows modders to add their own hooks into elements.
	 */
	public static void registerSetFunction(final ResourceLocation par0, @Nonnull TriConsumer<PlayerEntity, IPlayerElements, Float> par1) {
		List<TriConsumer<PlayerEntity, IPlayerElements, Float>> var0 = SET_REGISTRY.getOrDefault(par0, new ArrayList<TriConsumer<PlayerEntity, IPlayerElements, Float>>());
		
		var0.add(par1);
		
		ADD_REGISTRY.put(par0, var0);
	}
	
	/**
	 * @param par0 Input Registry id.
	 * @return Safely returns the element corresponding with the input registry id, if it exists.
	 */
	public static Optional<Element> fromResource(final ResourceLocation par0) {
		return Optional.ofNullable(ELEMENT_REGISTRY.get(par0));
	}
}
