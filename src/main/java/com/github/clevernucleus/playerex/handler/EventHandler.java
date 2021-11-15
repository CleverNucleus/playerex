package com.github.clevernucleus.playerex.handler;

import org.apache.commons.lang3.mutable.MutableDouble;

import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;
import com.github.clevernucleus.playerex.impl.ModifierDataManager;
import com.github.clevernucleus.playerex.impl.PersistentPlayerCacheManager;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class EventHandler {
	public static void respawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		ModifierDataManager newData = (ModifierDataManager)ExAPI.DATA.get(newPlayer);
		ModifierDataManager oldData = (ModifierDataManager)ExAPI.DATA.get(oldPlayer);
		
		if(ExAPI.CONFIG.get().resetOnDeath()) {
			oldData.reset();
		}
		
		newData.refresh(oldData);
		
		if(!alive) {
			newPlayer.setHealth(oldPlayer.getMaxHealth());
		}
	}
	
	public static void join(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		ServerPlayerEntity player = handler.getPlayer();
		PersistentPlayerCacheManager cache = (PersistentPlayerCacheManager)ExAPI.persistentPlayerCache(server);
		cache.cachePlayer(player);
	}
	
	public static void healthUpdate(LivingEntity entity, EntityAttribute attribute, EntityAttributeModifier modifier) {
		if(attribute != EntityAttributes.GENERIC_MAX_HEALTH) return;
		if(entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity)entity;
			ModifierData data = ExAPI.DATA.get(player);
			Identifier key = new Identifier(ExAPI.MODID, "current_health");
			//float c = player.getHealth();
			//System.out.println("current: " + c + "max: " + player.getMaxHealth());
			
			//data.putInCache(key, c);
			
			
		}
	}
	
	public static void test(LivingEntity entity, EntityAttribute attribute, EntityAttributeModifier modifier) {
		if(attribute != EntityAttributes.GENERIC_MAX_HEALTH) return;
		if(entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity)entity;
			ModifierData data = ExAPI.DATA.get(player);
			Identifier key = new Identifier(ExAPI.MODID, "current_health");
			//double currentHealth = data.getFromCache(key);
			
			//System.out.println("cached: " + currentHealth + "; max: " + player.getMaxHealth());
			
			final float c = player.getHealth();
			final float k = player.getMaxHealth();
			if(data.cacheContains(key)) {
				
				double currentHealth = data.getFromCache(key);
				
				//System.out.println("POST - current: " + c + "; max: " + k + "; cached: " + currentHealth);
				
				player.setHealth((float)currentHealth);
				
				data.removeFromCache(key);
			}
			
			
			//System.out.println("PRE - current: " + c + "; max: " + k);
			data.putInCache(key, c);
			//player.setHealth((float)currentHealth);
			//data.removeFromCache(key);
		}
	}
	
	public static void levelUpdate(LivingEntity entity, EntityAttribute attribute, EntityAttributeModifier modifier) {
		EntityAttribute level = ExAPI.LEVEL.get();
		
		if(level == null) return;
		if(attribute != level) return;
		if(entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity)entity;
			MinecraftServer server = player.getServer();
			PersistentPlayerCacheManager cache = (PersistentPlayerCacheManager)ExAPI.persistentPlayerCache(server);
			cache.update(player);
		}
	}
	
	public static void roundClampedValue(final EntityAttribute attributeIn, final MutableDouble valueIn) {
		IAttribute attribute = (IAttribute)attributeIn;
		
		if(attribute.hasProperty(ExAPI.INTEGER_PROPERTY)) {
			final float value = valueIn.floatValue();
			final int rounded = Math.round(value);
			
			valueIn.setValue(rounded);
		}
	}
}
