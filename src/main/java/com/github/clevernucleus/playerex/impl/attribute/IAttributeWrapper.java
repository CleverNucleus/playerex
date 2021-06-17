package com.github.clevernucleus.playerex.impl.attribute;

import java.util.Set;

import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;

import net.minecraft.entity.attribute.EntityAttribute;

public interface IAttributeWrapper {
	EntityAttribute get();
	
	Set<IAttributeFunction> functions();
}
