package clevernucleus.playerex.api;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Maps;

import clevernucleus.playerex.api.element.Element;
import clevernucleus.playerex.api.element.IElement;
import clevernucleus.playerex.api.element.IPlayerElements;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

/**
 * For modders using the api, this will be your main access point for PlayerEx.
 * 
 * Secondary Registry for API.
 */
public class ElementRegistry {
	
	/** Holds all Elements. */
	private static final Map<ResourceLocation, IElement> ELEMENTS = Maps.newHashMap();
	
	/** Capability access. */
	@CapabilityInject(IPlayerElements.class)
	public static final Capability<IPlayerElements> PLAYER_ELEMENTS = null;
	
	/** Capability pass-through function. */
	public static final Function<PlayerEntity, LazyOptional<IPlayerElements>> GET_PLAYER_ELEMENTS = var -> var.getCapability(PLAYER_ELEMENTS, null);
	
	/** The modid used to identify playerex. */
	public static final String MODID = "playerex";
	
	/** Instead of null we always use this! */
	public static final IElement NONE = null;
	
	public static final IElement EXPERIENCE = registerElement(new ResourceLocation(MODID, "experience"), new Element(0D, 0D, 0D, IElement.Type.DATA) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
		}
	});
	public static final IElement LEVEL = registerElement(new ResourceLocation(MODID, "level"), new Element(0D, 0D, 0D, IElement.Type.DATA) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
			
			ElementRegistry.SKILLPOINTS.set(par0, par1, par2);
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
			
			ElementRegistry.SKILLPOINTS.add(par0, par1, par2);
		}
	});
	public static final IElement SKILLPOINTS = registerElement(new ResourceLocation(MODID, "skillpoints"), new Element(0D, 0D, 0D, IElement.Type.DATA) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
		}
	});
	public static final IElement CONSTITUTION = registerElement(new ResourceLocation(MODID, "constitution"), new Element(0D, 1D, 10D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
			
			ElementRegistry.HEALTH.set(par0, par1, par2);
			ElementRegistry.HEALTH_REGEN_AMP.set(par0, par1, par2 * 0.01D);
			ElementRegistry.KNOCKBACK_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.EXPLOSION_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.DROWNING_RESISTANCE.set(par0, par1, par2 * 0.01D);
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
			
			ElementRegistry.HEALTH.add(par0, par1, par2);
			ElementRegistry.HEALTH_REGEN_AMP.add(par0, par1, par2 * 0.01D);
			ElementRegistry.KNOCKBACK_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.EXPLOSION_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.DROWNING_RESISTANCE.add(par0, par1, par2 * 0.01D);
		}
	});
	public static final IElement STRENGTH = registerElement(new ResourceLocation(MODID, "strength"), new Element(0D, 1D, 10D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
			
			ElementRegistry.HEALTH_REGEN.set(par0, par1, par2 * 0.0005D);
			ElementRegistry.DAMAGE_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.FALLING_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.MELEE_DAMAGE.set(par0, par1, par2 * 0.25D);
			ElementRegistry.EXPLOSION_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.LAVA_RESISTANCE.set(par0, par1, par2 * 0.01D);
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
			
			ElementRegistry.HEALTH_REGEN.add(par0, par1, par2 * 0.0005D);
			ElementRegistry.DAMAGE_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.FALLING_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.MELEE_DAMAGE.add(par0, par1, par2 * 0.25D);
			ElementRegistry.EXPLOSION_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.LAVA_RESISTANCE.add(par0, par1, par2 * 0.01D);
		}
	});
	public static final IElement DEXTERITY = registerElement(new ResourceLocation(MODID, "dexterity"), new Element(0D, 1D, 10D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
			
			ElementRegistry.ARMOR.set(par0, par1, par2 * 0.5D);
			ElementRegistry.FALLING_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.FIRE_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.MOVEMENT_SPEED_AMP.set(par0, par1, par2 * 0.02D);
			ElementRegistry.MELEE_CRIT_DAMAGE.set(par0, par1, par2 * 0.05D);
			ElementRegistry.ATTACK_SPEED.set(par0, par1, par2 * 0.25D);
			ElementRegistry.RANGED_DAMAGE.set(par0, par1, par2 * 0.25D);
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
			
			ElementRegistry.ARMOR.add(par0, par1, par2 * 0.5D);
			ElementRegistry.FALLING_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.FIRE_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.MOVEMENT_SPEED_AMP.add(par0, par1, par2 * 0.02D);
			ElementRegistry.MELEE_CRIT_DAMAGE.add(par0, par1, par2 * 0.05D);
			ElementRegistry.ATTACK_SPEED.add(par0, par1, par2 * 0.25D);
			ElementRegistry.RANGED_DAMAGE.add(par0, par1, par2 * 0.25D);
		}
	});
	public static final IElement INTELLIGENCE = registerElement(new ResourceLocation(MODID, "intelligence"), new Element(0D, 1D, 10D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
			
			ElementRegistry.FIRE_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.LAVA_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.POISON_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.WITHER_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.DROWNING_RESISTANCE.set(par0, par1, par2 * 0.01D);
			ElementRegistry.RANGED_CRIT_DAMAGE.set(par0, par1, par2 * 0.05D);
			ElementRegistry.LIFESTEAL.set(par0, par1, par2 * 0.02D);
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
			
			ElementRegistry.FIRE_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.LAVA_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.POISON_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.WITHER_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.DROWNING_RESISTANCE.add(par0, par1, par2 * 0.01D);
			ElementRegistry.RANGED_CRIT_DAMAGE.add(par0, par1, par2 * 0.05D);
			ElementRegistry.LIFESTEAL.add(par0, par1, par2 * 0.02D);
		}
	});
	public static final IElement LUCKINESS = registerElement(new ResourceLocation(MODID, "luckiness"), new Element(0D, 1D, 10D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
			
			ElementRegistry.LUCK.set(par0, par1, par2);
			ElementRegistry.MELEE_CRIT_CHANCE.set(par0, par1, par2 * 0.02D);
			ElementRegistry.RANGED_CRIT_CHANCE.set(par0, par1, par2 * 0.02D);
			ElementRegistry.EVASION_CHANCE.set(par0, par1, par2 * 0.02D);
			ElementRegistry.POISON_RESISTANCE.set(par0, par1, par2 * 0.01D);
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
			
			ElementRegistry.LUCK.add(par0, par1, par2);
			ElementRegistry.MELEE_CRIT_CHANCE.add(par0, par1, par2 * 0.02D);
			ElementRegistry.RANGED_CRIT_CHANCE.add(par0, par1, par2 * 0.02D);
			ElementRegistry.EVASION_CHANCE.add(par0, par1, par2 * 0.02D);
			ElementRegistry.POISON_RESISTANCE.add(par0, par1, par2 * 0.01D);
		}
	});
	public static final IElement HEALTH = registerElement(new ResourceLocation("health"), new Element(0D, 1D, 10D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getMaxHealth();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233818_a_).setBaseValue(Math.max(1.0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233818_a_).setBaseValue(Math.max(0D, par0.getAttribute(Attributes.field_233818_a_).getBaseValue() + par2));
			
			if(par2 < 0D) {
				par0.setHealth((par0.getHealth() + par2) < par0.getMaxHealth() ? par0.getMaxHealth() : ((par0.getHealth() + par2) < 1F) ? 1F : par0.getHealth());
			}
		}
	});
	public static final IElement HEALTH_REGEN = registerElement(new ResourceLocation(MODID, "health_regen"), new Element(0D, 0.0001D, 0.001D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
		}
	});
	public static final IElement HEALTH_REGEN_AMP = registerElement(new ResourceLocation(MODID, "health_regen_amp"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 10D)));
		}
	});
	public static final IElement ARMOR = registerElement(new ResourceLocation("armor"), new Element(0D, 1D, 10D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getAttribute(Attributes.field_233826_i_).getBaseValue();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233826_i_).setBaseValue(Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233826_i_).setBaseValue(Math.max(0D, Util.dim(par0.getAttribute(Attributes.field_233826_i_).getBaseValue(), par2, 100D)));
		}
	});
	public static final IElement ARMOR_TOUGHNESS = registerElement(new ResourceLocation("armor_toughness"), new Element(0D, 0.25D, 4D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getAttribute(Attributes.field_233827_j_).getBaseValue();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233827_j_).setBaseValue(Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233827_j_).setBaseValue(Math.max(0D, Util.dim(par0.getAttribute(Attributes.field_233827_j_).getBaseValue(), par2, 100D)));
		}
	});
	public static final IElement KNOCKBACK_RESISTANCE = registerElement(new ResourceLocation("knockback_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getAttribute(Attributes.field_233820_c_).getBaseValue();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233820_c_).setBaseValue(Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233820_c_).setBaseValue(Math.max(0D, Util.dim(par0.getAttribute(Attributes.field_233820_c_).getBaseValue(), par2, 1D)));
		}
	});
	public static final IElement DAMAGE_RESISTANCE = registerElement(new ResourceLocation(MODID, "damage_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement FIRE_RESISTANCE = registerElement(new ResourceLocation(MODID, "fire_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement LAVA_RESISTANCE = registerElement(new ResourceLocation(MODID, "lava_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement EXPLOSION_RESISTANCE = registerElement(new ResourceLocation(MODID, "explosion_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement FALLING_RESISTANCE = registerElement(new ResourceLocation(MODID, "falling_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement POISON_RESISTANCE = registerElement(new ResourceLocation(MODID, "poison_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement WITHER_RESISTANCE = registerElement(new ResourceLocation(MODID, "wither_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement DROWNING_RESISTANCE = registerElement(new ResourceLocation(MODID, "drowning_resistance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement MOVEMENT_SPEED_AMP = registerElement(new ResourceLocation(MODID, "movement_speed_amp"), new Element(0D, 0.01D, 0.4D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 2D)));
		}
	});
	public static final IElement MELEE_DAMAGE = registerElement(new ResourceLocation("melee_damage"), new Element(0D, 0.01D, 0.2D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getAttribute(Attributes.field_233823_f_).getBaseValue();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233823_f_).setBaseValue(Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233823_f_).setBaseValue(Math.max(0D, par0.getAttribute(Attributes.field_233823_f_).getBaseValue() + par2));
		}
	});
	public static final IElement MELEE_CRIT_DAMAGE = registerElement(new ResourceLocation(MODID, "melee_crit_damage"), new Element(0D, 0.05D, 0.5D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 10D)));
		}
	});
	public static final IElement MELEE_CRIT_CHANCE = registerElement(new ResourceLocation(MODID, "melee_crit_chance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement ATTACK_SPEED = registerElement(new ResourceLocation("attack_speed"), new Element(0D, 0.25D, 4D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getAttribute(Attributes.field_233825_h_).getBaseValue();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233825_h_).setBaseValue(Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233825_h_).setBaseValue(Math.max(0D, par0.getAttribute(Attributes.field_233825_h_).getBaseValue() + par2));
		}
	});
	public static final IElement EVASION_CHANCE = registerElement(new ResourceLocation(MODID, "evasion_chance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement RANGED_DAMAGE = registerElement(new ResourceLocation(MODID, "ranged_damage"), new Element(0D, 0.25D, 4D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, get(par0, par1) + par2));
		}
	});
	public static final IElement RANGED_CRIT_DAMAGE = registerElement(new ResourceLocation(MODID, "ranged_crit_damage"), new Element(0D, 0.05D, 0.5D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 10D)));
		}
	});
	public static final IElement RANGED_CRIT_CHANCE = registerElement(new ResourceLocation(MODID, "ranged_crit_chance"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 1D)));
		}
	});
	public static final IElement LIFESTEAL = registerElement(new ResourceLocation(MODID, "lifesteal"), new Element(0D, 0.01D, 0.2D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, Math.max(0D, Util.dim(get(par0, par1), par2, 10D)));
		}
	});
	public static final IElement LUCK = registerElement(new ResourceLocation("luck"), new Element(0D, 1D, 10D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getAttribute(Attributes.field_233828_k_).getBaseValue();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233828_k_).setBaseValue(Math.max(0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233828_k_).setBaseValue(Math.max(0D, par0.getAttribute(Attributes.field_233828_k_).getBaseValue() + par2));
		}
	});
	
	/**
	 * Use this to register an element.
	 * @param par0 This should include your modid.
	 * @param par1 The element object; this should be fully constructed with no nulls.
	 * @return The now registered input element. This is a pass-through, but can be used as a stand-alone too.
	 */
	public static IElement registerElement(final ResourceLocation par0, final IElement par1) {
		par1.setRegistryName(par0);
		
		ELEMENTS.putIfAbsent(par0, par1);
		
		return par1;
	}
	
	/**
	 * @param par0 The element's id (resource location) - should contain a modid.
	 * @return Getter. Returns the mapped element to the input id; if that is null returns {@link ElementRegistry#NONE}
	 */
	public static IElement getElement(final ResourceLocation par0) {
		return ELEMENTS.getOrDefault(par0, NONE);
	}
	
	/**
	 * @return Returns an set containing all the resource location id's for all elements. Should be used only for iteration.
	 */
	public static Set<ResourceLocation> getRegistry() {
		return ELEMENTS.keySet();
	}
}
