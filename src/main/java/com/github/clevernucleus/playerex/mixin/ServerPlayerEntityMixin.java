package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.PlayerDataManager;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin {
	
	@Inject(method = "addExperienceLevels", at = @At("TAIL"))
	private void onAddExperienceLevels(int levels, CallbackInfo info) {
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		PlayerDataManager playerDataManager = (PlayerDataManager)ExAPI.INSTANCE.get(player);
		int currentXp = player.experienceLevel;
		int requireXp = ExAPI.getConfig().requiredXp(player);
		
		if(currentXp >= requireXp) {
			if(!playerDataManager.hasNotifiedLevelUp) {
				NetworkHandler.notifyLevelUp(player);
				playerDataManager.hasNotifiedLevelUp = true;
			}
		} else {
			playerDataManager.hasNotifiedLevelUp = false;
		}
	}
}
