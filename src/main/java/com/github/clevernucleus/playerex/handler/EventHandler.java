package com.github.clevernucleus.playerex.handler;

import java.util.Map;
import java.util.Random;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class EventHandler {
	private static boolean canSyncAttributes() {
		Map<Identifier, IPlayerAttribute> attributes = ExAPI.REGISTRY.get().attributes();
		
		return attributes == null ? false : !attributes.isEmpty();
	}
	
	public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		ServerPlayerEntity player = handler.player;
		NetworkHandler.syncConfig(player);
		
		if(canSyncAttributes()) {
			NetworkHandler.syncAttributes(player);
		}
		
		AttributeDataManager data = (AttributeDataManager)ExAPI.DATA.get(player);
		data.refresh(data);
	}
	
	public static void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
		AttributeDataManager data = (AttributeDataManager)ExAPI.DATA.get(player);
		data.refresh(data);
	}
	
	public static void respawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		AttributeDataManager oldData = (AttributeDataManager)ExAPI.DATA.get(oldPlayer);
		AttributeDataManager newData = (AttributeDataManager)ExAPI.DATA.get(newPlayer);
		
		if(ExAPI.CONFIG.get().resetOnDeath()) {
			oldData.reset();
		}
		
		newData.setContainer(oldData.container());
		newData.refresh(oldData);
		
		if(!alive) {
			newPlayer.setHealth(oldPlayer.getMaxHealth());
		}
	}
	
	public static void levelUp(PlayerEntity player) {
		if(!ExAPI.CONFIG.get().announceLevelUp()) return;
		
		World world = player.world;
		
		if(!world.isClient) {
			ServerWorld server = (ServerWorld)world;
			
			for(int i = 0; i < 6; ++i) {
				server.spawnParticles(ParticleTypes.LARGE_SMOKE, player.getParticleX(0.5D), player.getRandomBodyY(), player.getParticleZ(0.5D), 5, 0, 0, 0, 0);
			}
			
			server.playSound((PlayerEntity)null, player.getBlockPos(), PlayerEx.LEVEL_UP, SoundCategory.NEUTRAL, 1.0F, 1.5F);
		}
	}
	
	public static void attributeModified(PlayerEntity player, IPlayerAttribute attribute, double oldValue, double newValue) {
		if(attribute.equals(PlayerAttributes.MAX_HEALTH.get())) {
			if(player.getHealth() > player.getMaxHealth()) {
				player.setHealth(player.getMaxHealth());
			}
		}
	}
	
	public static void onHeal(LivingEntity entity, float amount) {
		if(!(entity instanceof PlayerEntity)) return;
		
		AttributeData data = ExAPI.DATA.get((PlayerEntity)entity);
		float mult = (float)data.get(PlayerAttributes.HEAL_AMPLIFICATION.get());
		
		amount = amount * (1.0F + mult);
	}
	
	public static void onTick(PlayerEntity player) {
		if(player.world.isClient) return;
		
		AttributeData data = ExAPI.DATA.get(player);
		float amount = (float)data.get(PlayerAttributes.HEALTH_REGEN.get());
		
		if(amount > 0.0F) {
			player.heal(amount);
		} else if(amount < 0.0F) {
			player.damage(DamageSource.MAGIC, amount);
		}
	}
	
	public static boolean onDamage(LivingEntity entity, DamageSource source, float amount) {
		if(entity.isInvulnerableTo(source) || entity.world.isClient) return false;
		if(entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			AttributeData data = ExAPI.DATA.get(player);
			Random rand = new Random();
			float mult = 0.0F;
			boolean end = false;
			
			if(source.equals(DamageSource.FALL)) {
				mult = (float)data.get(PlayerAttributes.FALLING_RESISTANCE.get());
			} else if(source.equals(DamageSource.DROWN)) {
				mult = (float)data.get(PlayerAttributes.DROWNING_RESISTANCE.get());
			} else if(source.equals(DamageSource.WITHER)) {
				mult = (float)data.get(PlayerAttributes.WITHER_RESISTANCE.get());
			} else if(source.isFire()) {
				mult = (float)data.get(PlayerAttributes.FIRE_RESISTANCE.get());
			} else if(source.getMagic()) {
				System.out.println("Potion resist");
				mult = (float)data.get(PlayerAttributes.MAGIC_RESISTANCE.get());
			} else if(source.isProjectile()) {
				float evasion = (float)data.get(PlayerAttributes.EVASION.get());
				
				end = rand.nextInt(100) < (int)(100.0F * evasion);
			}
			
			amount = amount * (1.0F + mult);
			
			return end;
		} else {
			if(source.getSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)source.getSource();
				AttributeData data = ExAPI.DATA.get(player);
				
				if(source.getMagic()) {
					float magicAmp = (float)data.get(PlayerAttributes.MAGIC_AMPLIFICATION.get());
					
					amount = amount * (1.0F + magicAmp);
				} else {
					float lifesteal = (float)data.get(PlayerAttributes.LIFESTEAL.get());
					
					player.heal(amount * lifesteal);
				}
			} else if(source.getSource() instanceof PotionEntity) {
				PotionEntity potion = (PotionEntity)source.getSource();
				
				if(potion.getOwner() instanceof PlayerEntity) {
					PlayerEntity player = (PlayerEntity)potion.getOwner();
					AttributeData data = ExAPI.DATA.get(player);
					
					if(source.getMagic()) {
						float magicAmp = (float)data.get(PlayerAttributes.MAGIC_AMPLIFICATION.get());
						
						amount = amount * (1.0F + magicAmp);
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean onCritChance(PlayerEntity player, Random rand, boolean bl3) {
		AttributeData data = ExAPI.DATA.get(player);
		float chance = (float)data.get(PlayerAttributes.MELEE_CRIT_CHANCE.get());
		
		return rand.nextInt(100) < (int)(100.0F * chance);
	}
	
	public static float onCritDamage(PlayerEntity player, Random rand, float amount) {
		AttributeData data = ExAPI.DATA.get(player);
		float damage = (float)data.get(PlayerAttributes.MELEE_CRIT_DAMAGE.get());
		
		return amount * (1.0F + damage);
	}
	
	public static void onArrowHit(ProjectileEntity projectile, Random rand) {
		if(!(projectile instanceof PersistentProjectileEntity)) return;
		
		PersistentProjectileEntity arrow = (PersistentProjectileEntity)projectile;
		
		if(arrow.getOwner() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)arrow.getOwner();
			AttributeData data = ExAPI.DATA.get(player);
			float critChance = (float)data.get(PlayerAttributes.RANGED_CRIT_CHANCE.get());
			float critDamage = (float)data.get(PlayerAttributes.RANGED_CRIT_DAMAGE.get());
			float nextDamage = (float)data.get(PlayerAttributes.RANGED_DAMAGE.get());
			float prevDamage = (float)arrow.getDamage();
			boolean isCrit = rand.nextInt(100) < (int)(100.0F * critChance);
			double result = (prevDamage + nextDamage) * (isCrit ? (1.0F + critDamage) : 1.0F);
			
			arrow.setCritical(isCrit);
			arrow.setDamage(result);
		}
	}
}
