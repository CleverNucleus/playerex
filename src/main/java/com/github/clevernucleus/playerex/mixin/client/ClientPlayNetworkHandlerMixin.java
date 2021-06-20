package com.github.clevernucleus.playerex.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	protected ClientPlayerEntity playerex$CachedPlayerOld;
	
	@ModifyVariable(method = "onPlayerRespawn", at = @At(value = "STORE", ordinal = 0), name = "clientPlayerEntity", ordinal = 0)
	private ClientPlayerEntity cacheOldPlayer(ClientPlayerEntity clientPlayerEntity, PlayerRespawnS2CPacket packet) {
		playerex$CachedPlayerOld = clientPlayerEntity;
		
		return clientPlayerEntity;
	}
	
	@ModifyVariable(method = "onPlayerRespawn", at = @At(value = "STORE", ordinal = 0), name = "clientPlayerEntity2", ordinal = 1)
	private ClientPlayerEntity cacheNewPlayer(ClientPlayerEntity clientPlayerEntity2, PlayerRespawnS2CPacket packet) {
		AttributeDataManager oldData = (AttributeDataManager)ExAPI.DATA.get(playerex$CachedPlayerOld);
		AttributeDataManager newData = (AttributeDataManager)ExAPI.DATA.get(clientPlayerEntity2);
		
		newData.setContainer(oldData.container());
		
		return clientPlayerEntity2;
	}
}
