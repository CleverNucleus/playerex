package com.github.clevernucleus.playerex.handler;

import org.apache.commons.lang3.mutable.MutableDouble;

import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.impl.ModifierDataManager;
import com.github.clevernucleus.playerex.impl.PersistentPlayerCacheManager;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

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
