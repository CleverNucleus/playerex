package com.github.clevernucleus.playerex.util;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

public final class NameLevelPair extends Pair<String, Integer> {
	public NameLevelPair(String name, int level) {
		super(name, level);
	}
	
	private int cast() {
		return this.getRight();
	}
	
	public String name() {
		return this.getLeft();
	}
	
	public String level() {
		return String.valueOf(this.cast());
	}
	
	public static NameLevelPair sort(NameLevelPair[] pairs, final int index) {
		Arrays.sort(pairs, Comparator.comparing(NameLevelPair::cast));
		
		int i = MathHelper.clamp(index, 1, pairs.length);
		
		return pairs[pairs.length - i];
	}
}
