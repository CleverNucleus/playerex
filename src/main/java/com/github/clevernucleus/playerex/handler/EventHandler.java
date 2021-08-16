package com.github.clevernucleus.playerex.handler;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.impl.ModifierDataManager;

import net.minecraft.server.network.ServerPlayerEntity;

public final class EventHandler {
	public static void respawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		ModifierDataManager newData = (ModifierDataManager)ExAPI.DATA.get(newPlayer);
		ModifierDataManager oldData = (ModifierDataManager)ExAPI.DATA.get(oldPlayer);
		newData.refresh(oldData);
	}
}
