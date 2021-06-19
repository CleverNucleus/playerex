package com.github.clevernucleus.playerex.client.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.attribute.AttributeType;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction.Type;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.impl.attribute.IAttributeWrapper;
import com.github.clevernucleus.playerex.impl.attribute.IClampedEntityAttribute;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public final class ClientPlayerAttribute implements IPlayerAttribute, IAttributeWrapper {
	private AttributeType type;
	private UUID uuid;
	private double defaultValue, minValue, maxValue;
	private String translationKey;
	private Map<String, Float> properties;
	private Set<IAttributeFunction> functions;
	private Identifier registryKey;
	private Lazy<EntityAttribute> attribute;
	
	private ClientPlayerAttribute(final AttributeType type, final UUID uuid, final Identifier keyIn, final Map<String, Float> properties, final Set<IAttributeFunction> functions, final String translationKey, final double defaultValue, final double minValue, double maxValue) {
		this.type = type;
		this.uuid = uuid;
		this.registryKey = keyIn;
		this.properties = properties;
		this.functions = functions;
		this.translationKey = translationKey;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	private ClientPlayerAttribute set(Supplier<EntityAttribute> attribute) {
		if(this.attribute == null) {
			this.attribute = new Lazy<EntityAttribute>(attribute);
		}
		
		return this;
	}
	
	private EntityAttribute getOrDefault() {
		EntityAttribute value = Registry.ATTRIBUTE.get(this.registryKey);
		
		if(value == null) {
			value = Registry.register(Registry.ATTRIBUTE, this.registryKey, (new ClampedEntityAttribute(this.translationKey(), this.defaultValue(), this.minValue(), this.maxValue())).setTracked(true));
		} else {
			value.setTracked(true);
		}
		
		return ((IClampedEntityAttribute)value).withLimits(this.minValue(), this.maxValue());
	}
	
	public ClientPlayerAttribute build() {
		EntityAttribute attribute = this.getOrDefault();
		
		return this.set(() -> attribute);
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
		Set<IAttributeFunction> functions = new HashSet<IAttributeFunction>();
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
			ClientAttributeFunction function = new ClientAttributeFunction(functionId, functionType, functionMultiplier);
			functions.add(function);
		}
		
		return new ClientPlayerAttribute(type, uuid, registryKey, properties, functions, translationKey, defaultValue, minValue, maxValue);
	}
	
	@Override
	public Set<IAttributeFunction> functions() {
		return this.functions;
	}
	
	@Override
	public EntityAttribute get() {
		return this.attribute.get();
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
