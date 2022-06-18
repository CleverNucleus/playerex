package com.github.clevernucleus.playerex.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import com.github.clevernucleus.playerex.api.PlayerData;

import net.minecraft.entity.player.PlayerEntity;

public final class RefundConditionImpl {
	private static final List<BiFunction<PlayerData, PlayerEntity, Double>> REGISTRY = new ArrayList<>();
	
	public static void add(final BiFunction<PlayerData, PlayerEntity, Double> refundCondition) {
		REGISTRY.add(refundCondition);
	}
	
	public static Collection<BiFunction<PlayerData, PlayerEntity, Double>> get() {
		return REGISTRY;
	}
}
