package com.github.clevernucleus.playerex.impl.attribute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.AttributeType;
import com.github.clevernucleus.playerex.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.event.PlayerAttributeModifiedEvent;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class AttributeDataManager implements AttributeData, AutoSyncedComponent {
	private Map<Identifier, Double> attributes = new HashMap<Identifier, Double>();
	private PlayerEntity player;
	private boolean levelled;
	private int refunds;
	
	public AttributeDataManager(PlayerEntity player) {
		this.player = player;
		
		ExAPI.REGISTRY.get().attributes().forEach((key, value) -> this.attributes.putIfAbsent(key, value.valueFromType()));
	}
	
	private boolean isGame(final IPlayerAttribute attributeIn) {
		if(attributeIn == null) return false;
		return attributeIn.type().equals(AttributeType.GAME);
	}
	
	private boolean hasModifier(final IPlayerAttribute attributeIn, EntityAttributeModifier modifierIn) {
		if(attributeIn == null && modifierIn == null) return false;
		
		return this.player.getAttributeInstance(((IAttributeWrapper)attributeIn).get()).hasModifier(modifierIn);
	}
	
	private EntityAttributeModifier applyModifier(final IPlayerAttribute attributeIn, EntityAttributeModifier modifierIn) {
		if(attributeIn != null && modifierIn != null) {
			this.player.getAttributeInstance(((IAttributeWrapper)attributeIn).get()).addPersistentModifier(modifierIn);
		}
		
		return modifierIn;
	}
	
	private EntityAttributeModifier removeModifier(final IPlayerAttribute attributeIn, EntityAttributeModifier modifierIn) {
		if(attributeIn != null && modifierIn != null) {
			this.player.getAttributeInstance(((IAttributeWrapper)attributeIn).get()).removeModifier(modifierIn);
		}
		
		return modifierIn;
	}
	
	private double reapplyModifier(final IPlayerAttribute attributeIn, final double valueIn) {
		if(this.isGame(attributeIn)) {
			EntityAttributeModifier modifier = new EntityAttributeModifier(attributeIn.uuid(), attributeIn.registryKey().toString() + ":modifier", valueIn, EntityAttributeModifier.Operation.ADDITION);
			
			this.removeModifier(attributeIn, modifier);
			this.applyModifier(attributeIn, modifier);
		}
		
		return valueIn;
	}
	
	private double modifyAttribute(final IPlayerAttribute attributeIn, final double valueIn) {
		double value = valueIn;
		
		if(this.isGame(attributeIn)) {
			value = valueIn - this.player.getAttributeValue(((IAttributeWrapper)attributeIn).get()) + this.getValue(attributeIn);
		}
		
		return this.reapplyModifier(attributeIn, value);
	}
	
	private void applyAttributeModifier(Set<IPlayerAttribute> recursives, final IPlayerAttribute keyIn, final EntityAttributeModifier modifierIn) {
		if(!this.isGame(keyIn) || this.hasModifier(keyIn, modifierIn) || keyIn == null) return;
		if(recursives.contains(keyIn)) return;
		
		recursives.add(keyIn);
		
		final double oldValue = this.get(keyIn);
		this.applyModifier(keyIn, modifierIn);
		final double newValue = this.get(keyIn);
		PlayerAttributeModifiedEvent.MODIFIED.invoker().onAttributeModified(this.player, keyIn, oldValue, newValue);
		
		for(IAttributeFunction function : keyIn.functions()) {
			Identifier subKey = function.attributeKey();
			IPlayerAttribute subAttribute = ExAPI.REGISTRY.get().getAttribute(subKey);
			EntityAttributeModifier.Operation operation = modifierIn.getOperation();
			double current = this.get(subAttribute);
			double limit = subAttribute.maxValue();
			double result = function.type().add(current, function.multiplier() * modifierIn.getValue(), limit);
			double value = modifierIn.getValue();
			
			if(operation == EntityAttributeModifier.Operation.ADDITION) {
				value = result - current;
			}
			
			EntityAttributeModifier subModifier = new EntityAttributeModifier(modifierIn.getId(), modifierIn.getName(), value, operation);
			
			this.applyAttributeModifier(recursives, subAttribute, subModifier);
		}
	}
	
	private double getValue(final IPlayerAttribute keyIn) {
		return this.attributes.getOrDefault(keyIn.registryKey(), keyIn.valueFromType());
	}
	
	private void setValue(final IPlayerAttribute keyIn, final double value) {
		this.attributes.put(keyIn.registryKey(), value);
	}
	
	private void setAttribute(final IPlayerAttribute keyIn, final double valueIn) {
		if(keyIn == null) return;
		
		final double oldValue = this.get(keyIn);
		final double value = MathHelper.clamp(valueIn, keyIn.minValue(), keyIn.maxValue());
		final double modValue = this.modifyAttribute(keyIn, value);
		
		this.setValue(keyIn, modValue);
		
		final double newValue = this.get(keyIn);
		
		PlayerAttributeModifiedEvent.MODIFIED.invoker().onAttributeModified(this.player, keyIn, oldValue, newValue);
		ExAPI.DATA.sync(this.player);
	}
	
	public void respawn(final AttributeDataManager manager) {
		manager.attributes.forEach((key, value) -> this.reapplyModifier(ExAPI.REGISTRY.get().getAttribute(key), value));
	}
	
	
	
	public void p() {
		// server-side:
		
		AttributeContainer one = this.player.getAttributes();
		// loop through all previous/cached attributes to get every attribute instance on this
		EntityAttributeModifier modOne = one.getCustomInstance(null).getModifier(null);
		
		// map every cached attribute to it's modifier
		
		
		// now we can register/refresh attributes
		
		DefaultAttributeContainer.Builder builder = DefaultAttributeContainer.builder();
		// add attributes to builder
		
		AttributeContainer two = new AttributeContainer(builder.build());
		// loop through every cached attribute and see if our new attributes list has it
		// if yes, apply the mapped modifier 'modOne' to the AttributeContainer 'two'
		// this will require the attribute instance
		
		one = two;
		// set one to two
		
		//client-side:
		
		// once attributes have been synced and registered :
		AttributeContainer oneClient = this.player.getAttributes();
		
		DefaultAttributeContainer.Builder builderClient = DefaultAttributeContainer.builder();
		// add attributes to builder
		
		AttributeContainer twoClient = new AttributeContainer(builderClient.build());
		
		oneClient = twoClient;
		
		// reset them
	}
	
	
	
	
	public void setLevelled(final boolean levelledIn) {
		this.levelled = levelledIn;
	}
	
	public boolean levelled() {
		return this.levelled;
	}
	
	public void reset() {
		this.levelled = false;
		this.refunds = 0;
		
		ExAPI.REGISTRY.get().attributes().forEach((key, value) -> this.attributes.replace(key, value.valueFromType()));
		this.attributes.forEach((key, value) -> this.reapplyModifier(ExAPI.REGISTRY.get().getAttribute(key), value));
	}
	
	@Override
	public int addRefundPoints(final int pointsIn) {
		if(this.player.world.isClient) return -1;
		
		IAttribute[] primaries = new IAttribute[] {
			PlayerAttributes.CONSTITUTION,
			PlayerAttributes.STRENGTH,
			PlayerAttributes.DEXTERITY,
			PlayerAttributes.INTELLIGENCE,
			PlayerAttributes.LUCKINESS
		};
		
		final int previousRefunds = this.refundPoints();
		double maxRefundPoints = 0D;
		
		for(IAttribute supplier : primaries) {
			IPlayerAttribute attribute = supplier.get();
			
			if(attribute == null) return -1;
			
			maxRefundPoints += this.getValue(attribute);
		}
		
		double refund = MathHelper.clamp((double)(this.refunds + pointsIn), 0.0D, maxRefundPoints);
		this.refunds = (int)Math.round(refund);
		ExAPI.DATA.sync(this.player);
		
		return this.refunds - previousRefunds;
	}
	
	@Override
	public int refundPoints() {
		return this.refunds;
	}
	
	@Override
	public double get(final IPlayerAttribute keyIn) {
		return this.isGame(keyIn) ? this.player.getAttributeValue(((IAttributeWrapper)keyIn).get()) : this.getValue(keyIn);
	}
	
	@Override
	public void set(final IPlayerAttribute keyIn, final double valueIn) {
		if(keyIn == null || this.player.world.isClient) return;
		
		this.setAttribute(keyIn, valueIn);
		
		for(IAttributeFunction function : keyIn.functions()) {
			Identifier subKey = function.attributeKey();
			IPlayerAttribute subAttribute = ExAPI.REGISTRY.get().getAttribute(subKey);
			double result = function.multiplier() * valueIn;
			
			this.setAttribute(subAttribute, result);
		}
	}
	
	@Override
	public void add(final IPlayerAttribute keyIn, final double valueIn) {
		if(keyIn == null || this.player.world.isClient) return;
		
		double currentValue = this.get(keyIn);
		double newValue = currentValue + valueIn;
		
		this.setAttribute(keyIn, newValue);
		
		for(IAttributeFunction function : keyIn.functions()) {
			Identifier subKey = function.attributeKey();
			IPlayerAttribute subAttribute = ExAPI.REGISTRY.get().getAttribute(subKey);
			double current = this.get(subAttribute);
			double limit = subAttribute.maxValue();
			double result = function.type().add(current, function.multiplier() * valueIn, limit);
			
			this.add(subAttribute, result - current);
		}
	}
	
	@Override
	public void applyAttributeModifier(final IPlayerAttribute keyIn, final EntityAttributeModifier modifierIn) {
		if(this.player.world.isClient) return;
		
		Set<IPlayerAttribute> recursives = new HashSet<IPlayerAttribute>();
		
		this.applyAttributeModifier(recursives, keyIn, modifierIn);
	}
	
	@Override
	public void removeAttributeModifier(final IPlayerAttribute keyIn, final EntityAttributeModifier modifierIn) {
		if(!this.isGame(keyIn) || keyIn == null || this.player.world.isClient) return;
		
		final double oldValue = this.get(keyIn);
		this.removeModifier(keyIn, modifierIn);
		final double newValue = this.get(keyIn);
		PlayerAttributeModifiedEvent.MODIFIED.invoker().onAttributeModified(this.player, keyIn, oldValue, newValue);
		
		for(IAttributeFunction function : keyIn.functions()) {
			Identifier subKey = function.attributeKey();
			IPlayerAttribute subAttribute = ExAPI.REGISTRY.get().getAttribute(subKey);
			EntityAttributeModifier.Operation operation = modifierIn.getOperation();
			double current = this.get(subAttribute);
			double limit = subAttribute.maxValue();
			double result = function.type().add(current, function.multiplier() * modifierIn.getValue(), limit);
			double value = modifierIn.getValue();
			
			if(operation == EntityAttributeModifier.Operation.ADDITION) {
				value = result - current;
			}
			
			EntityAttributeModifier subModifier = new EntityAttributeModifier(modifierIn.getId(), modifierIn.getName(), value, operation);
			
			this.removeAttributeModifier(subAttribute, subModifier);
		}
	}
	
	@Override
	public void readFromNbt(CompoundTag tag) {
		if(!tag.contains("data")) return;
		
		ListTag list = tag.getList("data", NbtType.COMPOUND);
		
		for(int var = 0; var < list.size(); var++) {
			CompoundTag subTag = list.getCompound(var);
			Identifier key = new Identifier(subTag.getString("Name"));
			double value = subTag.getDouble("Value");
			
			this.attributes.put(key, value);
		}
		
		if(!tag.contains("refunds")) return;
		
		this.refunds = tag.getInt("refunds");
		this.levelled = tag.getBoolean("levelled");
	}
	
	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag list = new ListTag();
		
		for(Map.Entry<Identifier, Double> entry : this.attributes.entrySet()) {
			CompoundTag subTag = new CompoundTag();
			subTag.putString("Name", entry.getKey().toString());
			subTag.putDouble("Value", entry.getValue().doubleValue());
			list.add(subTag);
		}
		
		tag.put("data", list);
		tag.putInt("refunds", this.refunds);
		tag.putBoolean("levelled", this.levelled);
	}
}
