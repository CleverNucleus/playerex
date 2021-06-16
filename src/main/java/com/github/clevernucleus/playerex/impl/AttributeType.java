package com.github.clevernucleus.playerex.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

public enum AttributeType {
	GAME("game", x -> 0D),
	DATA("data", PlayerAttribute::defaultValue);
	
	private static final Map<String, AttributeType> TYPES = Arrays.stream(AttributeType.values()).collect(Collectors.toMap(AttributeType::toString, type -> type));
	private String name;
	private Function<PlayerAttribute, Double> value;
	
	private AttributeType(final String name, final Function<PlayerAttribute, Double> value) {
		this.name = name;
		this.value = value;
	}
	
	@Nullable
	public static AttributeType fromName(final String string) {
		return TYPES.get(string);
	}
	
	public double value(PlayerAttribute attribute) {
		return this.value.apply(attribute);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
