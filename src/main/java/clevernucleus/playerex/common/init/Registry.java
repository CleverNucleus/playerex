package clevernucleus.playerex.common.init;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.capability.IPlayerElements;
import clevernucleus.playerex.common.init.capability.PlayerElements;
import clevernucleus.playerex.common.init.container.PlayerElementsContainer;
import clevernucleus.playerex.common.init.element.*;
import clevernucleus.playerex.common.init.item.*;
import clevernucleus.playerex.common.network.*;
import clevernucleus.playerex.common.util.Util;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

/**
 * Mod registry. Holds all registry objects added by PlayerEx.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {
	
	/** List storing instances of every registered item. */
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	/** Holder for iterating over all nbt read/write elements. */
	public static final Set<IDataElement> DATA_ELEMENTS = new HashSet<IDataElement>();
	
	/** Holder for iterating over all game elements. */
	public static final Set<IElement> GAME_ELEMENTS = new HashSet<IElement>();
	
	/** Network instance. */
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(PlayerEx.MODID, "path"), () -> "1", "1"::equals, "1"::equals);
	
	/** Capability access. */
	@CapabilityInject(IPlayerElements.class)
	public static final Capability<IPlayerElements> CAPABILITY = null;
	
	/** Capability pass-through function. */
	public static final Function<PlayerEntity, LazyOptional<IPlayerElements>> ELEMENTS = var -> var.getCapability(CAPABILITY, null);
	
	/** Gets the IElement instance in a safe format from its ID. */
	public static final Function<String, Optional<IElement>> ELEMENT_FROM_ID = var0 -> {
		Set<IElement> var1 = new HashSet<IElement>();
		var1.addAll(DATA_ELEMENTS);
		var1.addAll(GAME_ELEMENTS);
		
		return var1.stream().filter(var -> var0.equals(var.toString())).findFirst();
	};
	
	public static final Item RELIC_AMULET = register("relic_amulet", new RelicItem());
	public static final Item RELIC_BODY = register("relic_body", new RelicItem());
	public static final Item RELIC_HEAD = register("relic_head", new RelicItem());
	public static final Item RELIC_RING = register("relic_ring", new RelicItem());
	public static final Item SMALL_HEALTH_POTION = register("small_health_potion", new HealthPotionItem(1));
	public static final Item MEDIUM_HEALTH_POTION = register("medium_health_potion", new HealthPotionItem(2));
	public static final Item LARGE_HEALTH_POTION = register("large_health_potion", new HealthPotionItem(3));
	
	/** Static identifier for the player elements container type. */
	public static final ContainerType<PlayerElementsContainer> ELEMENTS_CONTAINER = register("elements", IForgeContainerType.create((var0, var1, var2) -> new PlayerElementsContainer(var0, var1)));
	
	public static final IDataElement EXPERIENCE = new WritableElement("Experience", 0F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
	}, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, par2);
	});
	
	public static final IDataElement LEVEL = new WritableElement("Level", 0F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.SKILLPOINTS, par2);
	}, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, par2);
		var0.set(par0, Registry.SKILLPOINTS, par2);
	});
	
	public static final IDataElement SKILLPOINTS = new WritableElement("SkillPoints", 0F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
	}, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, par2);
	});
	
	public static final IDataElement CONSTITUTION = new PropertyElement("Constitution", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.HEALTH, par2);
		var0.add(par0, Registry.ARMOUR_TOUGHNESS, par2 * 0.2D);
		var0.add(par0, Registry.KNOCKBACK_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.EXPLOSION_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.DROWNING_RESISTANCE, par2 * 0.01D);
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	public static final IDataElement STRENGTH = new PropertyElement("Strength", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.HEALTH_REGEN, par2 * 0.0005D);
		var0.add(par0, Registry.DAMAGE_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.FALL_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.MELEE_DAMAGE, par2 * 0.25D);
		var0.add(par0, Registry.EXPLOSION_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.LAVA_RESISTANCE, par2 * 0.01D);
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	public static final IDataElement DEXTERITY = new PropertyElement("Dexterity", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.ARMOUR, par2 * 0.5D);
		var0.add(par0, Registry.FALL_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.FIRE_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.MOVEMENT_SPEED_AMP, par2 * 0.02D);
		var0.add(par0, Registry.MELEE_CRIT_DAMAGE, par2 * 0.05D);
		var0.add(par0, Registry.ATTACK_SPEED, par2 * 0.25D);
		var0.add(par0, Registry.RANGED_DAMAGE, par2 * 0.25D);
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	public static final IDataElement INTELLIGENCE = new PropertyElement("Intelligence", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.HEALTH_REGEN_AMP, par2 * 0.02D);
		var0.add(par0, Registry.FIRE_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.LAVA_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.POISON_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.WITHER_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.DROWNING_RESISTANCE, par2 * 0.01D);
		var0.add(par0, Registry.RANGED_CRIT_DAMAGE, par2 * 0.05D);
		var0.add(par0, Registry.LIFESTEAL, par2 * 0.02D);
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	public static final IDataElement LUCK = new PropertyElement("Luck", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.LUCKINESS, par2);
		var0.add(par0, Registry.MELEE_CRIT_CHANCE, par2 * 0.02D);
		var0.add(par0, Registry.EVASION_CHANCE, par2 * 0.02D);
		var0.add(par0, Registry.RANGED_CRIT_CHANCE, par2 * 0.02D);
		var0.add(par0, Registry.POISON_RESISTANCE, par2 * 0.01D);
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	public static final IElement HEALTH = new BasicElement("Health", 1F, 10F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(par0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + par2);
		
		if(par2 < 0D) {
			par0.setHealth((par0.getHealth() + par2) < par0.getMaxHealth() ? par0.getMaxHealth() : ((par0.getHealth() + par2) < 1F) ? 1F : par0.getHealth());
		}
	}, (par0, par1) -> (double)par0.getMaxHealth(), (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	public static final IDataElement HEALTH_REGEN = new PropertyElement("HealthRegen", 0F, 0.0001F, 0.001F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("#.####").format(par1 * 20F)));
	
	public static final IDataElement HEALTH_REGEN_AMP = new PropertyElement("HealthRegenAmp", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 10D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IElement ARMOUR = new BasicElement("Armour", 1F, 10F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(Util.dim(par0.getAttribute(SharedMonsterAttributes.ARMOR).getBaseValue(), par2, 50D));
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ARMOR).getBaseValue(), (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	public static final IElement ARMOUR_TOUGHNESS = new BasicElement("ArmourToughness", 0.25F, 4F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(Util.dim(par0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue(), par2, 50D));
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue(), (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("#.##").format(par1)));
	
	public static final IElement KNOCKBACK_RESISTANCE = new BasicElement("KnockbackResistance", 0.01F, 0.2F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(Util.dim(par0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue(), par2, 1D));
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue(), (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement DAMAGE_RESISTANCE = new PropertyElement("DamageResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement FIRE_RESISTANCE = new PropertyElement("FireResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement LAVA_RESISTANCE = new PropertyElement("LavaResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement EXPLOSION_RESISTANCE = new PropertyElement("ExplosionResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement FALL_RESISTANCE = new PropertyElement("FallResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement POISON_RESISTANCE = new PropertyElement("PoisonResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement WITHER_RESISTANCE = new PropertyElement("WitherResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement DROWNING_RESISTANCE = new PropertyElement("DrowningResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement MOVEMENT_SPEED_AMP = new PropertyElement("MovementSpeedAmp", 0F, 0.01F, 0.4F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IElement MELEE_DAMAGE = new BasicElement("MeleeDamage", 0.25F, 4F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(par0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + par2);
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue(), (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("#.##").format(par1)));
	
	public static final IDataElement MELEE_CRIT_DAMAGE = new PropertyElement("MeleeCritDamage", 0F, 0.05F, 0.5F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 10D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement MELEE_CRIT_CHANCE = new PropertyElement("MeleeCritChance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IElement ATTACK_SPEED = new BasicElement("AttackSpeed", 0.25F, 4F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(par0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue() + par2);
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue(), (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("#.##").format(par1)));
	
	public static final IDataElement EVASION_CHANCE = new PropertyElement("EvasionChance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement RANGED_DAMAGE = new PropertyElement("RangedDamage", 0F, 0.25F, 4F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("#.##").format(par1)));
	
	public static final IDataElement RANGED_CRIT_DAMAGE = new PropertyElement("RangedCritDamage", 0F, 0.05F, 0.5F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 10D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement RANGED_CRIT_CHANCE = new PropertyElement("RangedCritChance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 1D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IDataElement LIFESTEAL = new PropertyElement("Lifesteal", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Util.dim(var0.get(par0, var1), par2, 10D));
	}, (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), Util.FORMAT.apply("##.##").format(par1 * 100F), "%"));
	
	public static final IElement LUCKINESS = new BasicElement("Luckiness", 1F, 10F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.LUCK).setBaseValue(par0.getAttribute(SharedMonsterAttributes.LUCK).getBaseValue() + par2);
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.LUCK).getBaseValue(), (par0, par1) -> new TranslationTextComponent("tooltip." + par0.toString(), par1.intValue()));
	
	/**
	 * Used to pass an item object and its registry name through to a list and returned again.
	 * @param par0 The registry name.
	 * @param par1 The item object.
	 * @return The item object, with its registry name set.
	 */
	private static Item register(final @Nonnull String par0, @Nonnull Item par1) {
		par1.setRegistryName(new ResourceLocation(PlayerEx.MODID, par0));
		
		ITEMS.add(par1);
		
		return par1;
	}
	
	/**
	 * Used to pass a container type and its registry name through to a list and returned again.
	 * @param par0 The registry name.
	 * @param par1 The container type object.
	 * @return The container type object, with its registry name set.
	 */
	private static <T extends Container> ContainerType<T> register(final @Nonnull String par0, ContainerType<T> par1) {
		par1.setRegistryName(new ResourceLocation(PlayerEx.MODID, par0));
		
		return par1;
	}
	
	/**
	 * Mod initialisation event.
	 * @param par0
	 */
	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent par0) {
		CapabilityManager.INSTANCE.register(IPlayerElements.class, new Capability.IStorage<IPlayerElements>() {
			
			@Override
			public INBT writeNBT(Capability<IPlayerElements> par0, IPlayerElements par1, Direction par2) {
				return par1.write();
			}
			
			@Override
			public void readNBT(Capability<IPlayerElements> par0, IPlayerElements par1, Direction par2, INBT par3) {
				par1.read((CompoundNBT)par3);
			}
		}, PlayerElements::new);
		
		NETWORK.registerMessage(0, SyncPlayerElements.class, SyncPlayerElements::encode, SyncPlayerElements::decode, SyncPlayerElements::handle);
		NETWORK.registerMessage(1, SwitchScreens.class, SwitchScreens::encode, SwitchScreens::decode, SwitchScreens::handle);
		NETWORK.registerMessage(2, AddPlayerElement.class, AddPlayerElement::encode, AddPlayerElement::decode, AddPlayerElement::handle);
	}
	
	/**
	 * Event handling the registration of container types.
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> par0) {
		par0.getRegistry().register(ELEMENTS_CONTAINER);
	}
	
	/**
	 * Event handling the registration of items to the game.
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> par0) {
		for(Item var : ITEMS) {
			par0.getRegistry().register(var);
		}
	}
	
	/**
	 * Event registering curios slots
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerCurios(final InterModEnqueueEvent par0) {
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("body"));
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("head"));
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring"));
	}
}
