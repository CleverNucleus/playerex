package com.github.clevernucleus.playerex.mixin;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.github.clevernucleus.playerex.util.EntityAttributeInstanceAccess;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;

@Mixin(EntityAttributeInstance.class)
abstract class EntityAttributeInstanceMixin implements EntityAttributeInstanceAccess {
	
	@Final
	@Shadow
	private Map<EntityAttributeModifier.Operation, Set<EntityAttributeModifier>> operationToModifiers;
	
	@Override
	public Collection<EntityAttributeModifier> modifiersByOperation(Operation operationIn) {
		return (Collection<EntityAttributeModifier>)this.operationToModifiers.getOrDefault(operationIn, Collections.emptySet());
	}
}
