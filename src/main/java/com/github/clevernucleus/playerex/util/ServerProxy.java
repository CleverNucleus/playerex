package com.github.clevernucleus.playerex.util;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;

public class ServerProxy implements IProxy {
	
	@Override
	public Optional<PlayerEntity> player() {
		return Optional.empty();
	}
}
