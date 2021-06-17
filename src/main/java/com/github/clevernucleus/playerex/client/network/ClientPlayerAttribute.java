package com.github.clevernucleus.playerex.client.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.github.clevernucleus.playerex.api.attribute.AttributeType;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.impl.attribute.IAttributeWrapper;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ClientPlayerAttribute implements IPlayerAttribute, IAttributeWrapper {
	private AttributeType type;
	private UUID uuid;
	private double defaultValue, minValue, maxValue;
	private String translationKey;
	private Map<String, Float> properties;
	private Identifier registryKey;
	
	private ClientPlayerAttribute(final AttributeType type, final UUID uuid, final Identifier keyIn, final Map<String, Float> properties, final String translationKey, final double defaultValue, final double minValue, double maxValue) {
		this.type = type;
		this.uuid = uuid;
		this.registryKey = keyIn;
		this.properties = properties;
		this.translationKey = translationKey;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public static ClientPlayerAttribute read(CompoundTag tag) {
		AttributeType type = AttributeType.fromName(tag.getString("Type"));
		UUID uuid = tag.getUuid("UUID");
		Identifier registryKey = new Identifier(tag.getString("RegistryKey"));
		double defaultValue = tag.getDouble("DefaultValue");
		double minValue = tag.getDouble("MinValue");
		double maxValue = tag.getDouble("MaxValue");
		String translationKey = tag.getString("TranslationKey");
		Map<String, Float> properties = new HashMap<String, Float>();
		ListTag propertiesTag = tag.getList("Properties", NbtType.COMPOUND);
		
		for(int i = 0; i < propertiesTag.size(); i++) {
			CompoundTag property = propertiesTag.getCompound(i);
			properties.put(property.getString("Key"), property.getFloat("Value"));
		}
		
		return new ClientPlayerAttribute(type, uuid, registryKey, properties, translationKey, defaultValue, minValue, maxValue);
	}
	
	@Override
	public Set<IAttributeFunction> functions() {
		return Collections.emptySet();
	}
	
	@Override
	public EntityAttribute get() {
		return Registry.ATTRIBUTE.get(this.registryKey);
	}
	
	@Override
	public UUID uuid() {
		return this.uuid;
	}
	
	@Override
	public Identifier registryKey() {
		return this.registryKey;
	}
	
	@Override
	public AttributeType type() {
		return this.type;
	}
	
	@Override
	public double valueFromType() {
		return this.type.value(this);
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
	public boolean hasProperty(final String keyIn) {
		return this.properties == null ? false : this.properties.containsKey(keyIn);
	}
	
	@Override
	public float getProperty(final String keyIn) {
		return this.hasProperty(keyIn) ? this.properties.getOrDefault(keyIn, 0.0F) : 0.0F;
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
		if(!(object instanceof ClientPlayerAttribute)) return false;
		
		ClientPlayerAttribute attribute = (ClientPlayerAttribute)object;
		return this.registryKey.equals(attribute.registryKey);
	}
	
	@Override
	public String toString() {
		return this.registryKey.toString();
	}
}
