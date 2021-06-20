package com.github.clevernucleus.playerex.impl.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.AttributeType;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction.Type;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.Expose;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class PlayerAttribute implements IPlayerAttribute {
	@Expose private AttributeType type;
	@Expose private UUID uuid;
	@Expose private double defaultValue, minValue, maxValue;
	@Expose private String translationKey;
	@Expose private HashMap<String, Float> properties;
	@Expose private ArrayList<AttributeFunction> functions;
	private Set<IAttributeFunction> functionsAppended;
	private Identifier registryKey;
	
	private PlayerAttribute(AttributeType type, UUID uuid, HashMap<String, Float> properties, ArrayList<AttributeFunction> functions, String translationKey, double defaultValue, double minValue, double maxValue) {
		this.type = type;
		this.uuid = uuid;
		this.properties = properties;
		this.functions = functions;
		this.translationKey = translationKey;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	protected PlayerAttribute build(Identifier identifier) {
		if(identifier == null) throw new IllegalStateException("Cannot build the attribute because the registry key is null!");
		
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
	
	public static PlayerAttribute read(CompoundTag tag) {
		AttributeType type = AttributeType.fromName(tag.getString("Type"));
		UUID uuid = tag.getUuid("UUID");
		Identifier registryKey = new Identifier(tag.getString("RegistryKey"));
		double defaultValue = tag.getDouble("DefaultValue");
		double minValue = tag.getDouble("MinValue");
		double maxValue = tag.getDouble("MaxValue");
		String translationKey = tag.getString("TranslationKey");
		HashMap<String, Float> properties = new HashMap<String, Float>();
		ListTag propertiesTag = tag.getList("Properties", NbtType.COMPOUND);
		ArrayList<AttributeFunction> functions = new ArrayList<AttributeFunction>();
		ListTag functionsTag = tag.getList("Functions", NbtType.COMPOUND);
		
		for(int i = 0; i < propertiesTag.size(); i++) {
			CompoundTag propertyTag = propertiesTag.getCompound(i);
			properties.put(propertyTag.getString("Key"), propertyTag.getFloat("Value"));
		}
		
		for(int i = 0; i < functionsTag.size(); i++) {
			CompoundTag functionTag = functionsTag.getCompound(i);
			Type functionType = Type.from(functionTag.getByte("Type"));
			String functionId = functionTag.getString("Key");
			double functionMultiplier = functionTag.getDouble("Value");
			AttributeFunction function = new AttributeFunction(functionId, functionType, functionMultiplier);
			functions.add(function);
		}
		
		PlayerAttribute attribute = new PlayerAttribute(type, uuid, properties, functions, translationKey, defaultValue, minValue, maxValue);
		
		return attribute.build(registryKey);
	}
	
	public void write(CompoundTag tag) {
		tag.putString("Type", this.type.toString());
		tag.putUuid("UUID", this.uuid);
		tag.putString("RegistryKey", this.registryKey.toString());
		tag.putDouble("DefaultValue", this.defaultValue);
		tag.putDouble("MinValue", this.minValue);
		tag.putDouble("MaxValue", this.maxValue);
		tag.putString("TranslationKey", this.translationKey);
		
		ListTag properties = new ListTag();
		ListTag functions = new ListTag();
		
		for(Map.Entry<String, Float> entry : this.properties.entrySet()) {
			CompoundTag property = new CompoundTag();
			property.putString("Key", entry.getKey());
			property.putFloat("Value", entry.getValue());
			properties.add(property);
		}
		
		tag.put("Properties", properties);
		
		for(IAttributeFunction function : this.functionsAppended) {
			CompoundTag functionTag = new CompoundTag();
			functionTag.putString("Key", function.attributeKey().toString());
			functionTag.putByte("Type", function.type().id());
			functionTag.putDouble("Value", function.multiplier());
			functions.add(functionTag);
		}
		
		tag.put("Functions", functions);
	}
	
	public EntityAttribute get() {
		return Registry.ATTRIBUTE.get(this.registryKey);
	}
	
	@Override
	public Set<IAttributeFunction> functions() {
		return ImmutableSet.copyOf(this.functionsAppended);
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
		if(!(object instanceof PlayerAttribute)) return false;
		
		PlayerAttribute attribute = (PlayerAttribute)object;
		return this.registryKey.equals(attribute.registryKey);
	}
	
	@Override
	public String toString() {
		return this.registryKey.toString();
	}
}
