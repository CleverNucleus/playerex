package com.github.clevernucleus.playerex.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.AttributeType;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.google.gson.annotations.Expose;
import com.ibm.icu.impl.locale.XCldrStub.ImmutableSet;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

public final class PlayerAttribute implements IPlayerAttribute {
	@Expose private AttributeType type;
	@Expose private UUID uuid;
	@Expose private double defaultValue, minValue, maxValue;
	@Expose private String translationKey, displayFormat;
	@Expose private HashMap<String, Float> properties;
	@Expose private ArrayList<AttributeFunction> functions;
	private Set<IAttributeFunction> functionsAppended;
	private Identifier registryKey;
	private Lazy<EntityAttribute> attribute;
	
	private PlayerAttribute() {}
	
	private PlayerAttribute set(Supplier<EntityAttribute> attribute) {
		if(this.attribute == null) {
			this.attribute = new Lazy<EntityAttribute>(attribute);
		}
		
		return this;
	}
	
	private PlayerAttribute register(final Identifier identifier) {
		if(identifier == null) throw new IllegalStateException("Cannot register the attribute because the registry key is null!");
		
		this.functionsAppended = new HashSet<IAttributeFunction>();
		
		if(this.functions != null) {
			this.functions.forEach(this.functionsAppended::add);
		}
		
		ExAPI.REGISTRY.get().functions(identifier).forEach(this.functionsAppended::add);
		
		if(this.registryKey == null) {
			this.registryKey = identifier;
		}
		
		return this;
	}
	
	private EntityAttribute getOrDefault(Identifier identifier) {
		EntityAttribute value = Registry.ATTRIBUTE.get(identifier);
		
		if(value == null) {
			value = Registry.register(Registry.ATTRIBUTE, identifier, (new ClampedEntityAttribute(this.translationKey(), this.defaultValue(), this.minValue(), this.maxValue())).setTracked(true));
		} else {
			value.setTracked(true);
		}
		
		return ((IClampedEntityAttribute)value).withLimits(this.minValue(), this.maxValue());
	}
	
	public PlayerAttribute build(Identifier identifier) {
		EntityAttribute attribute = this.getOrDefault(identifier);
		
		return this.set(() -> attribute).register(identifier);
	}
	
	public EntityAttribute get() {
		return this.attribute.get();
	}
	
	public UUID uuid() {
		return this.uuid;
	}
	
	public Identifier registryKey() {
		return this.registryKey;
	}
	
	public double valueFromType() {
		return this.type.value(this);
	}
	
	public Set<IAttributeFunction> functions() {
		return ImmutableSet.copyOf(this.functionsAppended);
	}
	
	@Override
	public AttributeType type() {
		return this.type;
	}
	
	@Override
	public double defaultValue() {
		return this.defaultValue;
	}
	
	@Override
	public double minValue() {
		return this.minValue;
	}
	
	@Override
	public double maxValue() {
		return this.maxValue;
	}
	
	@Override
	public boolean hasProperty(final String key) {
		return this.properties == null ? false : this.properties.containsKey(key);
	}
	
	@Override
	public float getProperty(final String key) {
		return this.hasProperty(key) ? this.properties.getOrDefault(key, 0.0F) : 0.0F;
	}
	
	@Override
	public String translationKey() {
		return this.translationKey;
	}
	
	@Override
	public int hashCode() {
		return this.registryKey.hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == this) return true;
		if(!(object instanceof PlayerAttribute)) return false;
		
		PlayerAttribute attribute = (PlayerAttribute)object;
		return this.registryKey.equals(attribute.registryKey);
	}
	
	@Override
	public String toString() {
		return this.registryKey.toString();
	}
}
