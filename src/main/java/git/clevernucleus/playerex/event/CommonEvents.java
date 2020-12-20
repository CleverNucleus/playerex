package git.clevernucleus.playerex.event;

import java.util.Random;

import javax.annotation.Nonnull;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.api.Util;
import git.clevernucleus.playerex.api.element.CapabilityProvider;
import git.clevernucleus.playerex.api.element.Elements;
import git.clevernucleus.playerex.api.element.IPlayerElements;
import git.clevernucleus.playerex.network.SyncPlayerElements;
import git.clevernucleus.playerex.util.ConfigSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {
	
	/**
	 * Initialises the player capabilities with the config.
	 * @param par0
	 * @param par1
	 */
	private static void initTag(final @Nonnull PlayerEntity par0, IPlayerElements par1) {
		CompoundNBT var0 = par1.write();
		
		if(var0.getBoolean("Initialised")) return;
		
		par1.add(Elements.CONSTITUTION, ConfigSetting.COMMON.constitution.get());
		par1.add(Elements.STRENGTH, ConfigSetting.COMMON.strength.get());
		par1.add(Elements.DEXTERITY, ConfigSetting.COMMON.dexterity.get());
		par1.add(Elements.INTELLIGENCE, ConfigSetting.COMMON.intelligence.get());
		par1.add(Elements.LUCKINESS, ConfigSetting.COMMON.luckiness.get());
		par1.add(Elements.HEALTH, -20F);
		
		var0.putBoolean("Initialised", true);
	}
	
	/**
	 * Sync event pass-through with safety functions.
	 * @param par0
	 */
	public static void syncTag(final @Nonnull PlayerEntity par0) {
		if(par0 == null) return;
		if(par0.world.isRemote) return;
		
		ExAPI.playerElements(par0).ifPresent(var -> {
			initTag(par0, var);
			
			CompoundNBT var0 = new CompoundNBT();
			
			var0.put("Elements", var.write());
			var0.putDouble("generic.knockbackResistance", par0.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue());
			var0.putDouble("generic.attackDamage", par0.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue());
			
			RegistryEvents.NETWORK.sendTo(new SyncPlayerElements(var0), ((ServerPlayerEntity)par0).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
		});
	}
	
	/**
	 * Event for attaching capabilities.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onCapabilityAttachEntity(final net.minecraftforge.event.AttachCapabilitiesEvent<Entity> par0) {
		if(par0.getObject() instanceof PlayerEntity) {
			par0.addCapability(new ResourceLocation(ExAPI.MODID, "playerelements"), new CapabilityProvider());
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
			ExAPI.playerElements(var0).ifPresent(par1 -> {
				ExAPI.playerElements(var1).ifPresent(par2 -> {
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
		
		ExAPI.playerElements(var0).ifPresent(var -> {
			var.add(Elements.EXPERIENCE, var1);
			
			float var2 = Util.expCoeff(var.get(Elements.LEVEL), var.get(Elements.EXPERIENCE));
			
			if(var2 > 0.95F) {
				var.add(Elements.LEVEL, 1);
				var.set(Elements.EXPERIENCE, 0F);
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
		
		ExAPI.playerElements(var0).ifPresent(var -> {
			var0.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1D + (0.1D * var.get(Elements.MOVEMENT_SPEED_AMP)));
			var0.heal(var.get(Elements.HEALTH_REGEN));
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
			
			ExAPI.playerElements(var0).ifPresent(var -> {
				par0.setAmount(par0.getAmount() * (1F + var.get(Elements.HEALTH_REGEN_AMP)));
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
		
		ExAPI.playerElements(var0).ifPresent(var -> {
			par0.setDamageModifier(1F + var.get(Elements.MELEE_CRIT_DAMAGE));
			
			if(var1.nextInt(100) < (int)(100F * var.get(Elements.MELEE_CRIT_CHANCE))) {
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
			
			ExAPI.playerElements(var0).ifPresent(var -> {
				if(par0.getSource().equals(DamageSource.IN_FIRE) || par0.getSource().equals(DamageSource.ON_FIRE) || par0.getSource().equals(DamageSource.HOT_FLOOR)) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.FIRE_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.LAVA)) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.LAVA_RESISTANCE)));
				}
				
				if(par0.getSource().isExplosion()) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.EXPLOSION_RESISTANCE)));
				}
				
				if(par0.getSource().isMagicDamage()) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.POISON_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.WITHER)) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.WITHER_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.DROWN)) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.DROWNING_RESISTANCE)));
				}
				
				if(par0.getSource().equals(DamageSource.FALL)) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.FALLING_RESISTANCE)));
				}
				
				if(par0.getSource().isUnblockable()) {
					par0.setAmount(par0.getAmount() * (1F - var.get(Elements.DAMAGE_RESISTANCE)));
				}
				
				if(par0.getSource().isProjectile()) {
					if(var1.nextInt(100) < (int)(100D * var.get(Elements.EVASION_CHANCE))) {
						par0.setCanceled(true);
					}
				}
			});
		}
		
		if(par0.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity var0 = (PlayerEntity)par0.getSource().getTrueSource();
			
			ExAPI.playerElements(var0).ifPresent(var -> {
				var0.heal(par0.getAmount() * var.get(Elements.LIFESTEAL));
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
			
			ExAPI.playerElements(var0).ifPresent(var -> {
				boolean var2 = var1.nextInt(100) > (int)(100F * var.get(Elements.RANGED_CRIT_CHANCE));
				double var3 = par0.getArrow().getDamage() + var.get(Elements.RANGED_DAMAGE);
				
				par0.getArrow().setIsCritical(var2);
				par0.getArrow().setDamage(var2 ? (var3 * (1D + var.get(Elements.RANGED_CRIT_DAMAGE))) : var3);
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
			
			ExAPI.playerElements(var0).ifPresent(var -> {
				par0.setLootingLevel(par0.getLootingLevel() + (int)(var.get(Elements.LUCKINESS) / 5F));
			});
		}
	}
}
