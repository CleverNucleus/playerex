package com.github.clevernucleus.playerex.handler;

import java.util.Map;
import java.util.Random;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.resource.ServerResourceManager;
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
	
	public static void dataPackReload(MinecraftServer server, ServerResourceManager manager, boolean success) {
		if(canSyncAttributes()) {
			for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				NetworkHandler.syncAttributes(player);
			}
		}
	}
	
	public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		NetworkHandler.syncConfig(handler.player);
		
		if(canSyncAttributes()) {
			NetworkHandler.syncAttributes(handler.player);
		}
	}
	
	public static void respawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		AttributeDataManager oldData = (AttributeDataManager)ExAPI.DATA.get(oldPlayer);
		AttributeDataManager newData = (AttributeDataManager)ExAPI.DATA.get(newPlayer);
		
		if(ExAPI.CONFIG.get().resetOnDeath()) {
			oldData.reset();
		}
		
		newData.respawn(oldData);
		
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
		if(attribute.equals(ExAPI.ATTRIBUTES.get().maxHealth.get())) {
			if(player.getHealth() > player.getMaxHealth()) {
				player.setHealth(player.getMaxHealth());
			}
		}
	}
	
	public static void onHeal(LivingEntity entity, float amount) {
		if(!(entity instanceof PlayerEntity)) return;
		
		AttributeData data = ExAPI.DATA.get((PlayerEntity)entity);
		float mult = (float)data.get(ExAPI.ATTRIBUTES.get().healAmplification.get());
		
		amount = amount * (1.0F + mult);
	}
	
	public static void onTick(PlayerEntity player) {
		if(player.world.isClient) return;
		
		AttributeData data = ExAPI.DATA.get(player);
		float amount = (float)data.get(ExAPI.ATTRIBUTES.get().healthRegeneration.get());
		
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
				mult = (float)data.get(ExAPI.ATTRIBUTES.get().fallingResistance.get());
			} else if(source.equals(DamageSource.DROWN)) {
				mult = (float)data.get(ExAPI.ATTRIBUTES.get().drowningResistance.get());
			} else if(source.equals(DamageSource.WITHER)) {
				mult = (float)data.get(ExAPI.ATTRIBUTES.get().witherResistance.get());
			} else if(source.isFire()) {
				mult = (float)data.get(ExAPI.ATTRIBUTES.get().fireResistance.get());
			} else if(source.getMagic()) {
				mult = (float)data.get(ExAPI.ATTRIBUTES.get().magicResistance.get());
			} else if(source.isProjectile()) {
				float evasion = (float)data.get(ExAPI.ATTRIBUTES.get().evasion.get());
				
				end = rand.nextInt(100) < (int)(100.0F * evasion);
			}
			
			amount = amount * (1.0F + mult);
			
			return end;
		} else {
			if(source.getSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)source.getSource();
				AttributeData data = ExAPI.DATA.get(player);
				float lifesteal = (float)data.get(ExAPI.ATTRIBUTES.get().lifesteal.get());
				
				player.heal(amount * lifesteal);
			}
		}
		
		return false;
	}
	
	public static boolean onCritChance(PlayerEntity player, Random rand, boolean bl3) {
		AttributeData data = ExAPI.DATA.get(player);
		float chance = (float)data.get(ExAPI.ATTRIBUTES.get().meleeCritChance.get());
		
		return rand.nextInt(100) < (int)(100.0F * chance);
	}
	
	public static float onCritDamage(PlayerEntity player, Random rand, float amount) {
		AttributeData data = ExAPI.DATA.get(player);
		float damage = (float)data.get(ExAPI.ATTRIBUTES.get().meleeCritDamage.get());
		
		return amount * (1.0F + damage);
	}
	
	public static void onArrowHit(ProjectileEntity projectile, Random rand) {
		if(!(projectile instanceof PersistentProjectileEntity)) return;
		
		PersistentProjectileEntity arrow = (PersistentProjectileEntity)projectile;
		
		if(arrow.getOwner() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)arrow.getOwner();
			AttributeData data = ExAPI.DATA.get(player);
			float critChance = (float)data.get(ExAPI.ATTRIBUTES.get().rangedCritChance.get());
			float critDamage = (float)data.get(ExAPI.ATTRIBUTES.get().rangedCritDamage.get());
			float nextDamage = (float)data.get(ExAPI.ATTRIBUTES.get().rangedDamage.get());
			float prevDamage = (float)arrow.getDamage();
			boolean isCrit = rand.nextInt(100) < (int)(100.0F * critChance);
			double result = (prevDamage + nextDamage) * (isCrit ? (1.0F + critDamage) : 1.0F);
			
			arrow.setCritical(isCrit);
			arrow.setDamage(result);
		}
	}
}
