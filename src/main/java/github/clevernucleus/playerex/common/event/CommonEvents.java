package github.clevernucleus.playerex.common.event;

import java.util.Random;

import javax.annotation.Nonnull;

import github.clevernucleus.playerex.api.ElementRegistry;
import github.clevernucleus.playerex.api.RandDistribution;
import github.clevernucleus.playerex.api.Util;
import github.clevernucleus.playerex.api.element.CapabilityProvider;
import github.clevernucleus.playerex.api.element.IPlayerElements;
import github.clevernucleus.playerex.common.PlayerEx;
import github.clevernucleus.playerex.common.init.Registry;
import github.clevernucleus.playerex.common.init.item.HealthPotionItem;
import github.clevernucleus.playerex.common.init.item.ILoot;
import github.clevernucleus.playerex.common.init.item.RelicItem;
import github.clevernucleus.playerex.common.network.SyncPlayerElements;
import github.clevernucleus.playerex.common.util.ConfigSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

/**
 * Repository for common events on the FORGE bus.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {
	
	/**
	 * Initialises the player capabilities with the config.
	 * @param par0
	 * @param par1
	 */
	private static void initTag(final @Nonnull PlayerEntity par0, IPlayerElements par1) {
		CompoundNBT var0 = par1.write();
		
		if(var0.getBoolean("Initialised")) return;
		
		ElementRegistry.CONSTITUTION.add(par0, par1, ConfigSetting.COMMON.constitution.get());
		ElementRegistry.STRENGTH.add(par0, par1, ConfigSetting.COMMON.strength.get());
		ElementRegistry.DEXTERITY.add(par0, par1, ConfigSetting.COMMON.dexterity.get());
		ElementRegistry.INTELLIGENCE.add(par0, par1, ConfigSetting.COMMON.intelligence.get());
		ElementRegistry.LUCKINESS.add(par0, par1, ConfigSetting.COMMON.luck.get());
		ElementRegistry.HEALTH.add(par0, par1, -20D);
		
		var0.putBoolean("Initialised", true);
	}
	
	/**
	 * Sync event pass-through with safety functions.
	 * @param par0
	 */
	public static void syncTag(final @Nonnull PlayerEntity par0) {
		if(par0 == null) return;
		if(par0.world.isRemote) return;
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(par0).ifPresent(var -> {
			initTag(par0, var);
			
			CompoundNBT var0 = new CompoundNBT();
			
			var0.put("Elements", var.write());
			var0.putDouble("generic.knockbackResistance", par0.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue());
			var0.putDouble("generic.attackDamage", par0.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue());
			
			Registry.NETWORK.sendTo(new SyncPlayerElements(var0), ((ServerPlayerEntity)par0).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
		});
	}
	
	/**
	 * Event for attaching capabilities.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onCapabilityAttachEntity(final net.minecraftforge.event.AttachCapabilitiesEvent<Entity> par0) {
		if(par0.getObject() instanceof PlayerEntity) {
			par0.addCapability(new ResourceLocation(PlayerEx.MODID, "playerelements"), new CapabilityProvider());
		}
	}
	
	/**
	 * Event firing when the player gets cloned.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onPlayerEntityCloned(final net.minecraftforge.event.entity.player.PlayerEvent.Clone par0) {
		PlayerEntity var0 = par0.getPlayer();
		PlayerEntity var1 = par0.getOriginal();
		
		if(var0.world.isRemote) return;
		
		try {
			ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(par1 -> {
				ElementRegistry.GET_PLAYER_ELEMENTS.apply(var1).ifPresent(par2 -> {
					var0.getAttribute(Attributes.MAX_HEALTH).setBaseValue(var1.getAttribute(Attributes.MAX_HEALTH).getBaseValue());
					var0.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(var1.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue());
					var0.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(var1.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue());
					var0.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(var1.getAttribute(Attributes.ATTACK_SPEED).getBaseValue());
					var0.getAttribute(Attributes.ARMOR).setBaseValue(var1.getAttribute(Attributes.ARMOR).getBaseValue());
					var0.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(var1.getAttribute(Attributes.ARMOR_TOUGHNESS).getBaseValue());
					var0.getAttribute(Attributes.LUCK).setBaseValue(var1.getAttribute(Attributes.LUCK).getBaseValue());
					
					par1.read(par2.write());
				});
			});
		} catch(Exception parE) {}
		
		syncTag(var0);
		
		if(par0.isWasDeath()) {
			var0.heal(var0.getMaxHealth());
		}
	}
	
	/**
	 * Event firing when a player changes dimensions.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onPlayerChangedDimension(final net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent par0) {
		syncTag(par0.getPlayer());
	}
	
	/**
	 * Event firing when the player respawns.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onPlayerRespawn(final net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent par0) {
		syncTag(par0.getPlayer());
	}
	
	/**
	 * Event firing when a player logs in.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onPlayerLoggedIn(final net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent par0) {
		syncTag(par0.getPlayer());
	}
	
	/**
	 * Event fired when xp is picked up.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onExperiencePickup(final net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp par0) {
		PlayerEntity var0 = par0.getPlayer();
		
		if(var0.world.isRemote) return;
		
		int var1 = par0.getOrb().getXpValue();
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
			ElementRegistry.EXPERIENCE.add(var0, var, var1);
			
			float var2 = Util.expCoeff((float)ElementRegistry.LEVEL.get(var0, var), (float)ElementRegistry.EXPERIENCE.get(var0, var));
			
			if(var2 > 0.95F) {
				ElementRegistry.LEVEL.add(var0, var, 1);
				ElementRegistry.EXPERIENCE.set(var0, var, 0D);
			}
		});
		
		syncTag(var0);
	}
	
	/**
	 * Event fired every tick.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onTick(final net.minecraftforge.event.TickEvent.PlayerTickEvent par0) {
		PlayerEntity var0 = par0.player;
		
		if(var0.world.isRemote) return;
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
			var0.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1D + (0.1D * ElementRegistry.MOVEMENT_SPEED_AMP.get(var0, var)));
			
			var0.heal((float)ElementRegistry.HEALTH_REGEN.get(var0, var));
		});
	}
	
	/**
	 * Event fired when an entity is healed.
	 * @param par0
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onHeal(final net.minecraftforge.event.entity.living.LivingHealEvent par0) {
		if(par0.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getEntityLiving();
			
			if(var0.world.isRemote) return;
			
			ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
				par0.setAmount(par0.getAmount() * (1F + (float)ElementRegistry.HEALTH_REGEN_AMP.get(var0, var)));
			});
		}
	}
	
	/**
	 * Event fired when a crit may or may not happen.
	 * @param par0
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onCrit(final net.minecraftforge.event.entity.player.CriticalHitEvent par0) {
		PlayerEntity var0 = par0.getPlayer();
		Random var1 = new Random();
		
		if(var0.world.isRemote) return;
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
			par0.setDamageModifier(1F + (float)ElementRegistry.MELEE_CRIT_DAMAGE.get(var0, var));
			
			if(var1.nextInt(100) < (int)(100D * ElementRegistry.MELEE_CRIT_CHANCE.get(var0, var))) {
				par0.setResult(Result.ALLOW);
			} else {
				par0.setResult(Result.DENY);
			}
		});
	}
	
	/**
	 * Event fired when a living entity is hurt.
	 * @param par0
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingHurt(final net.minecraftforge.event.entity.living.LivingHurtEvent par0) {
		if(par0.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getEntityLiving();
			Random var1 = new Random();
			
			if(var0.world.isRemote) return;
			
			ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
				if(par0.getSource().equals(DamageSource.IN_FIRE) || par0.getSource().equals(DamageSource.ON_FIRE) || par0.getSource().equals(DamageSource.HOT_FLOOR)) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.FIRE_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().equals(DamageSource.LAVA)) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.LAVA_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().isExplosion()) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.EXPLOSION_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().isMagicDamage()) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.POISON_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().equals(DamageSource.WITHER)) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.WITHER_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().equals(DamageSource.DROWN)) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.DROWNING_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().equals(DamageSource.FALL)) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.FALLING_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().isUnblockable()) {
					par0.setAmount(par0.getAmount() * (1F - (float)ElementRegistry.DAMAGE_RESISTANCE.get(var0, var)));
				}
				
				if(par0.getSource().isProjectile()) {
					if(var1.nextInt(100) < (int)(100D * ElementRegistry.EVASION_CHANCE.get(var0, var))) {
						par0.setCanceled(true);
					}
				}
			});
		}
		
		if(par0.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getSource().getTrueSource();
			
			ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
				var0.heal(par0.getAmount() * (float)ElementRegistry.LIFESTEAL.get(var0, var));
			});
		}
	}
	
	/**
	 * Event fired when a living entity takes an arrow up the ***.
	 * @param par0
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onProjectileImpact(final net.minecraftforge.event.entity.ProjectileImpactEvent.Arrow par0) {
		if(par0.getArrow().func_234616_v_() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getArrow().func_234616_v_();
			Random var1 = new Random();
			
			if(var0.world.isRemote) return;
			
			ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
				boolean var2 = var1.nextInt(100) > (int)(100F * ElementRegistry.RANGED_CRIT_CHANCE.get(var0, var));
				double var3 = par0.getArrow().getDamage() + ElementRegistry.RANGED_DAMAGE.get(var0, var);
				
				par0.getArrow().setIsCritical(var2);
				par0.getArrow().setDamage(var2 ? (var3 * (1D + ElementRegistry.RANGED_CRIT_DAMAGE.get(var0, var))) : var3);
			});
		}
	}
	
	/**
	 * Event fired on looting.
	 * @param par0
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingLoot(final net.minecraftforge.event.entity.living.LootingLevelEvent par0) {
		if(par0.getDamageSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getDamageSource().getTrueSource();
			
			if(var0.world.isRemote) return;
			
			ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
				par0.setLootingLevel(par0.getLootingLevel() + (int)(ElementRegistry.LUCKINESS.get(var0, var) / 5D));
			});
		}
	}
	
	/**
	 * Event to add random drops to instances of IMobs.
	 * @param par0
	 */
	@SubscribeEvent
    public static void dropLoot(final net.minecraftforge.event.entity.living.LivingDropsEvent par0) {
		if(ConfigSetting.COMMON.dropChance.get() == 0) return;
		if(!ConfigSetting.COMMON.enableCurioDrops.get() && !ConfigSetting.COMMON.enableDivineDrops.get() && !ConfigSetting.COMMON.enablePotionDrops.get()) return;
		
		LivingEntity var0 = par0.getEntityLiving();
		
		if(var0 instanceof IMob) {
			Random var1 = new Random();
			
			if(var1.nextInt(100) > ConfigSetting.COMMON.dropChance.get()) return;
			
			RandDistribution<Item> var2 = new RandDistribution<Item>(Items.AIR);
			
			for(Item var : Registry.ITEMS) {
				if(var instanceof ILoot) {
					var2.add(var, ((ILoot)var).getWeight());
				}
			}
			
			Item var3 = var2.getDistributedRandom();
			ItemStack var4 = new ItemStack(var3);
			
			if(var3 instanceof RelicItem) {
				if(!ConfigSetting.COMMON.enableCurioDrops.get()) return;
				
				Util.createRandomRelic(var4);
			} else if(var3 instanceof HealthPotionItem) {
				if(!ConfigSetting.COMMON.enablePotionDrops.get()) return;
			} else {
				if(!ConfigSetting.COMMON.enableDivineDrops.get()) return;
			}
			
			par0.getDrops().add(new ItemEntity(var0.world, var0.getPosX(), var0.getPosY(), var0.getPosZ(), var4));
		}
	}
}
