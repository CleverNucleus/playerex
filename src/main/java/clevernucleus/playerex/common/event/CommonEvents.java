package clevernucleus.playerex.common.event;

import java.util.Random;

import javax.annotation.Nonnull;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.capability.CapabilityProvider;
import clevernucleus.playerex.common.init.item.RelicItem;
import clevernucleus.playerex.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Repository for common events on the FORGE bus.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {
	
	/**
	 * Sync event pass-through with safety functions.
	 * @param par0
	 */
	private static void syncTag(final @Nonnull PlayerEntity par0) {
		if(par0 == null) return;
		if(par0.world.isRemote) return;
		
		Registry.ELEMENTS.apply(par0).ifPresent(var -> {
			var.init(par0);
			var.sync(par0);
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
			Registry.ELEMENTS.apply(var0).ifPresent(par1 -> {
				Registry.ELEMENTS.apply(var1).ifPresent(par2 -> {
					var0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(var1.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(var1.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ARMOR).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.LUCK).setBaseValue(var1.getAttribute(SharedMonsterAttributes.LUCK).getBaseValue());
					
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
		
		Registry.ELEMENTS.apply(var0).ifPresent(var -> {
			var.add(var0, Registry.EXPERIENCE, var1);
			
			float var2 = Util.expCoeff((float)var.get(var0, Registry.LEVEL), (float)var.get(var0, Registry.EXPERIENCE));
			
			if(var2 > 0.95F) {
				var.add(var0, Registry.LEVEL, 1);
				var.set(var0, Registry.EXPERIENCE, 0);
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
		
		Registry.ELEMENTS.apply(var0).ifPresent(var -> {
			var0.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D + (0.1D * var.get(var0, Registry.MOVEMENT_SPEED_AMP)));
			
			var0.heal((float)var.get(var0, Registry.HEALTH_REGEN));
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
			
			Registry.ELEMENTS.apply(var0).ifPresent(var -> {
				par0.setAmount(par0.getAmount() * (1F + (float)var.get(var0, Registry.HEALTH_REGEN_AMP)));
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
		
		Registry.ELEMENTS.apply(var0).ifPresent(var -> {
			par0.setDamageModifier(1F + (float)var.get(var0, Registry.MELEE_CRIT_DAMAGE));
			
			if(var1.nextInt(100) < (int)(100D * var.get(var0, Registry.MELEE_CRIT_CHANCE))) {
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
			
			Registry.ELEMENTS.apply(var0).ifPresent(var -> {
				if(par0.getSource().equals(DamageSource.IN_FIRE) || par0.getSource().equals(DamageSource.ON_FIRE) || par0.getSource().equals(DamageSource.HOT_FLOOR)) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.FIRE_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.LAVA)) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.LAVA_RESISTANCE)));
				}
				
				if(par0.getSource().isExplosion()) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.EXPLOSION_RESISTANCE)));
				}
				
				if(par0.getSource().isMagicDamage()) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.POISON_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.WITHER)) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.WITHER_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.DROWN)) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.DROWNING_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.FALL)) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.FALL_RESISTANCE)));
				}
				
				if(par0.getSource().isUnblockable()) {
					par0.setAmount(par0.getAmount() * (1F - (float)var.get(var0, Registry.DAMAGE_RESISTANCE)));
				}
				
				if(par0.getSource().isProjectile()) {
					if(var1.nextInt(100) < (int)(100D * var.get(var0, Registry.EVASION_CHANCE))) {
						par0.setCanceled(true);
					}
				}
			});
		}
		
		if(par0.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getSource().getTrueSource();
			
			Registry.ELEMENTS.apply(var0).ifPresent(var -> {
				var0.heal(par0.getAmount() * (float)var.get(var0, Registry.LIFESTEAL));
			});
		}
	}
	
	/**
	 * Event fired when a living entity takes an arrow up the ***.
	 * @param par0
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onProjectileImpact(final net.minecraftforge.event.entity.ProjectileImpactEvent.Arrow par0) {
		if(par0.getArrow().getShooter() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getArrow().getShooter();
			Random var1 = new Random();
			
			if(var0.world.isRemote) return;
			
			Registry.ELEMENTS.apply(var0).ifPresent(var -> {
				boolean var2 = var1.nextInt(100) > (int)(100F * var.get(var0, Registry.RANGED_CRIT_CHANCE));
				double var3 = par0.getArrow().getDamage() + var.get(var0, Registry.RANGED_DAMAGE);
				
				par0.getArrow().setIsCritical(var2);
				par0.getArrow().setDamage(var2 ? (var3 * (1D + var.get(var0, Registry.RANGED_CRIT_DAMAGE))) : var3);
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
			
			Registry.ELEMENTS.apply(var0).ifPresent(var -> {
				par0.setLootingLevel(par0.getLootingLevel() + (int)(var.get(var0, Registry.LUCK) / 5D));
			});
		}
	}
	
	/**
	 * Event to add random drops to instances of IMobs.
	 * @param par0
	 */
	@SubscribeEvent
    public static void dropLoot(final net.minecraftforge.event.entity.living.LivingDropsEvent par0) {
		LivingEntity var0 = par0.getEntityLiving();
		
		if(var0 instanceof IMob) {
			Random var1 = new Random();
			Item var2 = Registry.ITEMS.get(var1.nextInt(Registry.ITEMS.size()));
			ItemStack var3 = new ItemStack(var2);
			
			if(var2 instanceof RelicItem) {
				Util.createRandomRelic(var3);
			}
			
			if(var1.nextInt(100) < 15) {
				par0.getDrops().add(new ItemEntity(var0.world, var0.getPosX(), var0.getPosY(), var0.getPosZ(), var3));
			}
		}
	}
}
