package com.github.clevernucleus.playerex.util;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;

public final class StoredResistance {
	private final String store;
	private final Supplier<EntityAttribute> key;
	private final Predicate<DamageSource> value;
	
	public StoredResistance(final String store, final Supplier<EntityAttribute> key, final Predicate<DamageSource> value) {
		this.store = store;
		this.key = key;
		this.value = value;
	}
	
	public float result(AttributeContainer container) {
		EntityAttribute attribute = this.key.get();
		return (float)container.getValue(attribute);
	}
	
	public boolean isValid(AttributeContainer container, DamageSource source) {
		if(!this.value.test(source)) return false;
		
		EntityAttribute attribute = this.key.get();
		
		if(attribute == null) return false;
		
		return container.hasAttribute(attribute);
	}
	
	@Override
	public String toString() {
		return this.store;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof StoredResistance)) return false;
		
		StoredResistance storedRef = (StoredResistance)obj;
		return this.store.equals(storedRef.store);
	}
	
	@Override
	public int hashCode() {
		return 31 * this.store.hashCode();
	}
}
