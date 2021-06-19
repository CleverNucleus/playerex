package com.github.clevernucleus.playerex.api.attribute;

import java.util.Set;
import java.util.UUID;

import net.minecraft.util.Identifier;

public interface IPlayerAttribute {
	
	
	AttributeType type();
	
	
	UUID uuid();
	
	
	Identifier registryKey();
	
	
	Set<IAttributeFunction> functions();
	
	
	double valueFromType();
	
	
	double defaultValue();
	
	
	double minValue();
	
	
	double maxValue();
	
	
	String translationKey();
	
	
	boolean hasProperty(final String keyIn);
	
	
	float getProperty(final String keyIn);
}
