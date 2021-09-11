package com.github.clevernucleus.playerex.util;

import java.util.Collection;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface EntityAttributeInstanceAccess {
	
	Collection<EntityAttributeModifier> modifiersByOperation(EntityAttributeModifier.Operation operationIn);
}
