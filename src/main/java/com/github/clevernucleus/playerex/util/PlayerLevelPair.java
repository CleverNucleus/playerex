package com.github.clevernucleus.playerex.util;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

public final class PlayerLevelPair extends Pair<PlayerEntity, Integer> {
	public PlayerLevelPair(PlayerEntity player, int level) {
		super(player, level);
	}
	
	public int cast() {
		return this.getRight();
	}
	
	public static PlayerLevelPair sort(PlayerLevelPair[] pairs, final int place) {
		Arrays.sort(pairs, Comparator.comparing(PlayerLevelPair::cast));
		
		int index = MathHelper.clamp(place, 1, pairs.length);
		
		return pairs[pairs.length - index];
	}
}
