package com.github.clevernucleus.playerex.api.attribute;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

/**
 * An enum for the type category of attribute: GAME and DATA.
 * 
 * @author CleverNucleus
 *
 */
public enum AttributeType {
	
	/** Attributes of this type are included as superversions of EntityAttributes and are available to the player and can use modifiers. */
	GAME("game", x -> 0D),
	
	/** Attributes of this type are purely data holders and are not registered as EntityAttributes and cannot use modifiers. */
	DATA("data", IPlayerAttribute::defaultValue);
	
	private static final Map<String, AttributeType> TYPES = Arrays.stream(AttributeType.values()).collect(Collectors.toMap(AttributeType::toString, type -> type));
	private String name;
	private Function<IPlayerAttribute, Double> value;
	
	private AttributeType(final String name, final Function<IPlayerAttribute, Double> value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param string the enum name (either 'game' or 'data')
	 * @return The type, GAME or DATA; else returns null.
	 */
	@Nullable
	public static AttributeType fromName(final String string) {
		return TYPES.get(string);
	}
	
	/**
	 * @param attribute 
	 * @return the default storage value that is initially set.
	 */
	public double value(IPlayerAttribute attribute) {
		return this.value.apply(attribute);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
