package git.clevernucleus.playerex.api.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import git.clevernucleus.playerex.api.ElementFunction;
import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.api.Util;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Allows the dev to register/edit/add/get elements.
 */
public class Elements {
	protected static final BiMap<ResourceLocation, Element> ELEMENT_REGISTRY = HashBiMap.create();
	protected static final Map<ResourceLocation, List<Consumer<ElementFunction>>> ADD_REGISTRY = Maps.newHashMap();
	
	public static final Element EXPERIENCE = registerElement(new ResourceLocation(ExAPI.MODID, "experience"), new Element(0F, 0F, 0F, Element.Type.DATA, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.EXPERIENCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element LEVEL = registerElement(new ResourceLocation(ExAPI.MODID, "level"), new Element(0F, 0F, 0F, Element.Type.DATA, (par0, par1) -> par1, var -> {
		PlayerEntity var0 = var.player();
		IPlayerElements var1 = var.elements();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var1.add(var0, Elements.SKILLPOINTS, var3);
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.LEVEL.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element SKILLPOINTS = registerElement(new ResourceLocation(ExAPI.MODID, "skillpoints"), new Element(0F, 0F, 0F, Element.Type.DATA, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.SKILLPOINTS.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element CONSTITUTION = registerElement(new ResourceLocation(ExAPI.MODID, "constitution"), new Element(0F, 1F, 10F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		PlayerEntity var0 = var.player();
		IPlayerElements var1 = var.elements();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var1.add(var0, Elements.HEALTH, var3);
		var1.add(var0, Elements.KNOCKBACK_RESISTANCE, (float)(var3 * 0.01D));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.CONSTITUTION.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element STRENGTH = registerElement(new ResourceLocation(ExAPI.MODID, "strength"), new Element(0F, 1F, 10F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		PlayerEntity var0 = var.player();
		IPlayerElements var1 = var.elements();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var1.add(var0, Elements.ARMOR, (float)(var3 * 0.5D));
		var1.add(var0, Elements.HEALTH_REGEN, (float)(var3 * 0.0005D));
		var1.add(var0, Elements.MELEE_DAMAGE, (float)(var3 * 0.25D));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.STRENGTH.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element DEXTERITY = registerElement(new ResourceLocation(ExAPI.MODID, "dexterity"), new Element(0F, 1F, 10F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		PlayerEntity var0 = var.player();
		IPlayerElements var1 = var.elements();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var1.add(var0, Elements.MOVEMENT_SPEED_AMP, (float)(var3 * 0.02D));
		var1.add(var0, Elements.MELEE_CRIT_DAMAGE, (float)(var3 * 0.05D));
		var1.add(var0, Elements.ATTACK_SPEED, (float)(var3 * 0.25D));
		var1.add(var0, Elements.RANGED_DAMAGE, (float)(var3 * 0.25D));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.DEXTERITY.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element INTELLIGENCE = registerElement(new ResourceLocation(ExAPI.MODID, "intelligence"), new Element(0F, 0F, 10F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		PlayerEntity var0 = var.player();
		IPlayerElements var1 = var.elements();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var1.add(var0, Elements.HEALTH_REGEN_AMP, (float)(var3 * 0.01D));
		var1.add(var0, Elements.RANGED_CRIT_DAMAGE, (float)(var3 * 0.05D));
		var1.add(var0, Elements.LIFESTEAL, (float)(var3 * 0.02D));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.INTELLIGENCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element LUCKINESS = registerElement(new ResourceLocation(ExAPI.MODID, "luckiness"), new Element(0F, 1F, 10F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		PlayerEntity var0 = var.player();
		IPlayerElements var1 = var.elements();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var1.add(var0, Elements.LUCK, var3);
		var1.add(var0, Elements.MELEE_CRIT_CHANCE, (float)(var3 * 0.02D));
		var1.add(var0, Elements.RANGED_CRIT_CHANCE, (float)(var3 * 0.02D));
		var1.add(var0, Elements.EVASION_CHANCE, (float)(var3 * 0.02D));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.LUCKINESS.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element HEALTH = registerElement(new ResourceLocation("health"), new Element(0F, 1F, 10F, Element.Type.GAME, (par0, par1) -> (float)par0.getAttribute(Attributes.MAX_HEALTH).getBaseValue(), var -> {
		PlayerEntity var0 = var.player();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var0.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(0D, var0.getAttribute(Attributes.MAX_HEALTH).getBaseValue() + var3));
		
		if(var3 < 0F) {
			var0.setHealth((var0.getHealth() + var3) < var0.getMaxHealth() ? var0.getMaxHealth() : ((var0.getHealth() + var3) < 1F) ? 1F : var0.getHealth());
		}
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.HEALTH.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return var2 + var3;
	}));
	public static final Element HEALTH_REGEN = registerElement(new ResourceLocation(ExAPI.MODID, "health_regen"), new Element(0F, 0.0001F, 0.001F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.HEALTH_REGEN.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, var2 + var3);
	}));
	public static final Element HEALTH_REGEN_AMP = registerElement(new ResourceLocation(ExAPI.MODID, "health_regen_amp"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.HEALTH_REGEN_AMP.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 10D));
	}));
	public static final Element ARMOR = registerElement(new ResourceLocation("armor"), new Element(0F, 1F, 10F, Element.Type.GAME, (par0, par1) -> (float)par0.getAttribute(Attributes.ARMOR).getBaseValue(), var -> {
		PlayerEntity var0 = var.player();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var0.getAttribute(Attributes.ARMOR).setBaseValue(Math.max(0D, Util.add(var0.getAttribute(Attributes.ARMOR).getBaseValue(), var3, 100D)));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.ARMOR.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return var2 + var3;
	}));
	public static final Element ARMOR_TOUGHNESS = registerElement(new ResourceLocation("armor_toughness"), new Element(0F, 0.25F, 4F, Element.Type.GAME, (par0, par1) -> (float)par0.getAttribute(Attributes.ARMOR_TOUGHNESS).getBaseValue(), var -> {
		PlayerEntity var0 = var.player();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var0.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(Math.max(0D, Util.add(var0.getAttribute(Attributes.ARMOR_TOUGHNESS).getBaseValue(), var3, 50D)));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.ARMOR_TOUGHNESS.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return var2 + var3;
	}));
	public static final Element KNOCKBACK_RESISTANCE = registerElement(new ResourceLocation("knockback_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.GAME, (par0, par1) -> (float)par0.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue(), var -> {
		PlayerEntity var0 = var.player();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var0.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(Math.max(0D, Util.add(var0.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue(), var3, 1D)));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.KNOCKBACK_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return var2 + var3;
	}));
	public static final Element DAMAGE_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "damage_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.DAMAGE_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element FIRE_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "fire_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.FIRE_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element LAVA_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "lava_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.LAVA_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element EXPLOSION_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "explosion_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.EXPLOSION_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element FALLING_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "falling_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.FALLING_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element POISON_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "poison_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.POISON_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element WITHER_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "wither_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.WITHER_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element DROWNING_RESISTANCE = registerElement(new ResourceLocation(ExAPI.MODID, "drowning_resistance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.DROWNING_RESISTANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element MOVEMENT_SPEED_AMP = registerElement(new ResourceLocation(ExAPI.MODID, "movement_speed_amp"), new Element(0F, 0.01F, 0.4F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.MOVEMENT_SPEED_AMP.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 2D));
	}));
	public static final Element MELEE_DAMAGE = registerElement(new ResourceLocation("melee_damage"), new Element(0F, 0.01F, 0.2F, Element.Type.GAME, (par0, par1) -> (float)par0.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue(), var -> {
		PlayerEntity var0 = var.player();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var0.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(0D, var0.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() + var3));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.MELEE_DAMAGE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return var2 + var3;
	}));
	public static final Element MELEE_CRIT_DAMAGE = registerElement(new ResourceLocation(ExAPI.MODID, "melee_crit_damage"), new Element(0F, 0.05F, 0.5F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.MELEE_CRIT_DAMAGE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 10D));
	}));
	public static final Element MELEE_CRIT_CHANCE = registerElement(new ResourceLocation(ExAPI.MODID, "melee_crit_chance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.MELEE_CRIT_CHANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element ATTACK_SPEED = registerElement(new ResourceLocation("attack_speed"), new Element(0F, 0.25F, 4F, Element.Type.GAME, (par0, par1) -> (float)par0.getAttribute(Attributes.ATTACK_SPEED).getBaseValue(), var -> {
		PlayerEntity var0 = var.player();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var0.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(Math.max(0D, var0.getAttribute(Attributes.ATTACK_SPEED).getBaseValue() + var3));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.ATTACK_SPEED.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return var2 + var3;
	}));
	public static final Element EVASION_CHANCE = registerElement(new ResourceLocation(ExAPI.MODID, "evasion_chance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.EVASION_CHANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element RANGED_DAMAGE = registerElement(new ResourceLocation(ExAPI.MODID, "ranged_damage"), new Element(0F, 0.25F, 4F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.RANGED_DAMAGE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return (float)Math.max(0F, var2 + var3);
	}));
	public static final Element RANGED_CRIT_DAMAGE = registerElement(new ResourceLocation(ExAPI.MODID, "ranged_crit_damage"), new Element(0F, 0.05F, 0.5F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.RANGED_CRIT_DAMAGE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element RANGED_CRIT_CHANCE = registerElement(new ResourceLocation(ExAPI.MODID, "ranged_crit_chance"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.RANGED_CRIT_CHANCE.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 1D));
	}));
	public static final Element LIFESTEAL = registerElement(new ResourceLocation(ExAPI.MODID, "lifesteal"), new Element(0F, 0.01F, 0.2F, Element.Type.ALL, (par0, par1) -> par1, var -> {
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.LIFESTEAL.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return Math.max(0F, (float)Util.add(var2, var3, 10D));
	}));
	public static final Element LUCK = registerElement(new ResourceLocation("luck"), new Element(0F, 1F, 10F, Element.Type.GAME, (par0, par1) -> (float)par0.getAttribute(Attributes.LUCK).getBaseValue(), var -> {
		PlayerEntity var0 = var.player();
		float var2 = var.currentValue();
		float var3 = var.addingValue();
		
		var0.getAttribute(Attributes.LUCK).setBaseValue(Math.max(0D, var0.getAttribute(Attributes.LUCK).getBaseValue() + var3));
		
		for(Consumer<ElementFunction> var4 : ADD_REGISTRY.getOrDefault(Elements.LUCK.registry(), new ArrayList<Consumer<ElementFunction>>())) {
			var4.accept(var);
		}
		
		return var2 + var3;
	}));
	
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
	 * Registers an adder function for the element. Elements can have multiple adder functions from multiple sources.
	 * @param par0 The element's registry id.
	 * @param par1 The code performed when calling add. Allows modders to add their own hooks into elements.
	 */
	public static void registerAddFunction(final ResourceLocation par0, @Nonnull Consumer<ElementFunction> par1) {
		List<Consumer<ElementFunction>> var0 = ADD_REGISTRY.getOrDefault(par0, new ArrayList<Consumer<ElementFunction>>());
		
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
