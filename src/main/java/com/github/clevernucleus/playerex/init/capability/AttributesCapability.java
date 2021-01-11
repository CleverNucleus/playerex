package com.github.clevernucleus.playerex.init.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import com.github.clevernucleus.playerex.init.Registry;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.fml.network.NetworkDirection;

/**
 * Capability implementation (object).
 */
public class AttributesCapability implements IPlayerAttributes {
	private Map<IPlayerAttribute, Float> clientStore, previousStore;
	private AttributeModifierManager attributeModifierManager;
	private CompoundNBT tag;
	
	public AttributesCapability() {
		MutableAttribute var0 = AttributeModifierMap.createMutableAttribute();
		ListNBT var1 = new ListNBT();
		CompoundNBT var2 = new CompoundNBT();
		
		this.tag = new CompoundNBT();
		this.tag.put("Attributes", var1);
		this.tag.put("Modifiers", var2);
		
		for(IPlayerAttribute var : PlayerAttributes.attributes()) {
			putAttribute(var, 0D);
			
			if(var.type() == IPlayerAttribute.Type.ALL || var.type() == IPlayerAttribute.Type.DATA) {
				var0 = var0.createMutableAttribute(var.get());
			}
		}
		
		this.clientStore = new HashMap<>();
		this.previousStore = new HashMap<>();
		this.attributeModifierManager = new AttributeModifierManager(var0.create());
	}
	
	/**
	 * @param par0
	 * @param par1
	 * @return The correct AttributeModifierManager instance based on the input IPlayerAttribute.
	 */
	private AttributeModifierManager getAttributeModifier(PlayerEntity par0, IPlayerAttribute par1) {
		if(par1.type() == IPlayerAttribute.Type.GAME) {
			return par0.getAttributeManager();
		} else if(par1.type() == IPlayerAttribute.Type.ALL || par1.type() == IPlayerAttribute.Type.DATA) {
			return this.attributeModifierManager;
		}
		
		return null;
	}
	
	/** 
	 * @return True if the attributeList is empty;
	 */
	private boolean isAttributeListEmpty() {
		return this.tag.getList("Attributes", 10).isEmpty();
	}
	
	/**
	 * Creates a CompoundNBT tag for the input IPlayerAttribute with the input storage value.
	 * @param par0
	 * @param par1
	 */
	private void createAttributeTag(IPlayerAttribute par0, double par1) {
		CompoundNBT var1 = new CompoundNBT();
		
		var1.putString("Name", par0.toString());
		var1.putDouble("Value", par1);
		
		this.tag.getList("Attributes", 10).add(var1);
	}
	
	/**
	 * If the Attribute Tag for the input IPlayerAttribute does not exist, creates one with {@link #createAttributeTag(IPlayerAttribute, double)}.
	 * If the Attribute Tag for the input IPlayerAttribute does exist however, only sets the storage value to the input value.
	 * @param par0
	 * @param par1
	 */
	private void putAttribute(IPlayerAttribute par0, double par1) {
		if(isAttributeListEmpty()) {
			createAttributeTag(par0, par1);
			
			return;
		}
		
		for(INBT var : this.tag.getList("Attributes", 10)) {
			CompoundNBT var0 = (CompoundNBT)var;
			String var1 = var0.getString("Name");
			
			if(var1.equals(par0.toString())) {
				var0.putDouble("Value", par1);
				
				return;
			}
		}
		
		createAttributeTag(par0, par1);
	}
	
	/**
	 * @param par0
	 * @return The input IPlayerAttribute instance's storage value if it exists; else returns the input IPlayerAttribute instance's default value.
	 */
	private double getAttribute(IPlayerAttribute par0) {
		double var0 = par0.get().getDefaultValue();
		
		if(isAttributeListEmpty()) return var0;
		
		for(INBT var : this.tag.getList("Attributes", 10)) {
			CompoundNBT var1 = (CompoundNBT)var;
			String var2 = var1.getString("Name");
			
			if(var2.equals(par0.toString())) return var1.getDouble("Value");
		}
		
		return var0;
	}
	
	/**
	 * Adds a new CompountNBT tag to the relevant modifier list to hold the input AttributeModifier; if the tag already exists, then nothing is done.
	 * If no modifier for the input attribute exists yet, also creates the attribute's modifier list.
	 * @param par0
	 * @param par1
	 */
	private void putModifier(IPlayerAttribute par0, AttributeModifier par1) {
		CompoundNBT var0 = this.tag.getCompound("Modifiers");
		ListNBT var1 = (var0.contains(par0.toString()) ? var0.getList(par0.toString(), 10) : new ListNBT());
		
		for(INBT var : var1) {
			CompoundNBT var2 = (CompoundNBT)var;
			UUID var3 = var2.getUniqueId("UUID");
			UUID var4 = par1.getID();
			
			if(var3.equals(var4)) return;
		}
		
		var1.add(par1.write());
		var0.put(par0.toString(), var1);
	}
	
	/**
	 * Removes the input attribute modifier tag from the attribute's modifier list if it exists.
	 * If, after removal, the attribute's modifier list is empty, removes the modifier list too.
	 * @param par0
	 * @param par1
	 */
	private void removeModifier(IPlayerAttribute par0, AttributeModifier par1) {
		CompoundNBT var0 = this.tag.getCompound("Modifiers");
		
		if(!var0.contains(par0.toString())) return;
		
		ListNBT var1 = var0.getList(par0.toString(), 10);
		
		for(int var = 0; var < var1.size(); var++) {
			CompoundNBT var2 = var1.getCompound(var);
			UUID var3 = var2.getUniqueId("UUID");
			UUID var4 = par1.getID();
			
			if(var3.equals(var4)) {
				var1.remove(var);
			}
		}
		
		if(var1.isEmpty()) var0.remove(par0.toString());
	}
	
	/**
	 * Takes all modifiers from the modifier list and reapplies them.
	 * @param par0
	 */
	private void refreshModifierMap(PlayerEntity par0) {
		CompoundNBT var0 = this.tag.getCompound("Modifiers");
		Multimap<IPlayerAttribute, AttributeModifier> var1 = ArrayListMultimap.create();
		
		if(var0.isEmpty()) return;
		
		for(String var : var0.keySet()) {
			ListNBT var2 = var0.getList(var, 10);
			IPlayerAttribute var3 = PlayerAttributes.fromRegistryName(var);
			
			if(var3 == null) return;
			
			for(INBT var4 : var2) {
				CompoundNBT var5 = (CompoundNBT)var4;
				AttributeModifier var6 = AttributeModifier.read(var5);
				
				var1.put(var3, var6);
			}
		}
		
		Multimap<Attribute, AttributeModifier> var2 = HashMultimap.create();
		Multimap<Attribute, AttributeModifier> var3 = HashMultimap.create();
		
		var1.forEach((var4, var5) -> {
			if(var4.type() == IPlayerAttribute.Type.GAME) {
				var2.put(var4.get(), var5);
			} else if(var4.type() == IPlayerAttribute.Type.ALL || var4.type() == IPlayerAttribute.Type.DATA) {
				var3.put(var4.get(), var5);
			}
		});
		
		par0.getAttributeManager().reapplyModifiers(var2);
		this.attributeModifierManager.reapplyModifiers(var3);
	}
	
	/**
	 * Sends data from the server to the client.
	 * @param par0 PlayerEntity instance.
	 */
	private void send(PlayerEntity par0) {
		if(par0 == null) return;
		if(par0.world.isRemote) return;
		
		CompoundNBT var0 = new CompoundNBT();
		ListNBT var1 = new ListNBT();
		
		for(IPlayerAttribute var : PlayerAttributes.attributes()) {
			CompoundNBT var2 = new CompoundNBT();
			
			var2.putString("Name", var.toString());
			var2.putFloat("Value", (float)get(par0, var));
			var1.add(var2);
		}
		
		var0.put("Data", var1);
		
		Registry.NETWORK.sendTo(new SyncPlayerAttributes(var0), ((ServerPlayerEntity)par0).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}
	
	@Override
	public double get(PlayerEntity par0, IPlayerAttribute par1) {
		if(par0.world.isRemote) return this.clientStore.getOrDefault(par1, 0F);
		
		return getAttributeModifier(par0, par1).getAttributeValue(par1.get());
	}
	
	@Override
	public void add(PlayerEntity par0, IPlayerAttribute par1, double par2) {
		double var0 = getAttribute(par1) + par2;
		
		Multimap<Attribute, AttributeModifier> var1 = HashMultimap.create();
		AttributeModifier var2 = new AttributeModifier(par1.uuid(), par1.toString(), var0, AttributeModifier.Operation.ADDITION);
		
		var1.put(par1.get(), var2);
		
		getAttributeModifier(par0, par1).reapplyModifiers(var1);
		putAttribute(par1, var0);
		
		PlayerAttributes.adders().get(par1.registryName()).forEach(var -> var.accept(par0, this, par2));
	}
	
	@Override
	public void forceSet(PlayerEntity par0, IPlayerAttribute par1, double par2) {
		Multimap<Attribute, AttributeModifier> var0 = HashMultimap.create();
		AttributeModifier var1 = new AttributeModifier(par1.uuid(), par1.toString(), 0D, AttributeModifier.Operation.ADDITION);
		
		var0.put(par1.get(), var1);
		
		getAttributeModifier(par0, par1).reapplyModifiers(var0);
		putAttribute(par1, 0D);
	}
	
	@Override
	public IPlayerAttributes applyModifier(PlayerEntity par0, IPlayerAttribute par1, AttributeModifier par2) {
		if(par0 == null || par1 == null || par2 == null) return this;
		
		Multimap<Attribute, AttributeModifier> var0 = HashMultimap.create();
		
		var0.put(par1.get(), par2);
		
		putModifier(par1, par2);
		getAttributeModifier(par0, par1).reapplyModifiers(var0);
		
		PlayerAttributes.modifiers().get(par1.registryName()).forEach(var -> var.accept(par0, this::applyModifier, par2));
		
		return this;
	}
	
	@Override
	public IPlayerAttributes removeModifier(PlayerEntity par0, IPlayerAttribute par1, AttributeModifier par2) {
		if(par0 == null || par1 == null || par2 == null) return this;
		
		Multimap<Attribute, AttributeModifier> var0 = HashMultimap.create();
		
		var0.put(par1.get(), par2);
		
		removeModifier(par1, par2);
		getAttributeModifier(par0, par1).removeModifiers(var0);
		
		PlayerAttributes.modifiers().get(par1.registryName()).forEach(var -> var.accept(par0, this::removeModifier, par2));
		
		return this;
	}
	
	@Override
	public CompoundNBT write() {
		return this.tag;
	}
	
	@Override
	public void read(CompoundNBT par0) {
		this.tag = par0;
	}
	
	/**
	 * Updates and refreshes the capability data during event's such as death and cloning. Dev's should AVOID using this.
	 * @param par0 PlayerEntity instance.
	 */
	public void update(PlayerEntity par0) {
		for(IPlayerAttribute par1 : PlayerAttributes.attributes()) {
			double var0 = getAttribute(par1);
			
			Multimap<Attribute, AttributeModifier> var1 = HashMultimap.create();
			AttributeModifier var2 = new AttributeModifier(par1.uuid(), par1.toString(), var0, AttributeModifier.Operation.ADDITION);
			
			var1.put(par1.get(), var2);
			
			getAttributeModifier(par0, par1).reapplyModifiers(var1);
		}
		
		refreshModifierMap(par0);
	}
	
	/**
	 * Receives data from the server.
	 * @param par0
	 */
	public void receive(CompoundNBT par0) {
		if(par0 == null) return;
		if(!par0.contains("Data")) return;
		
		ListNBT var0 = par0.getList("Data", 10);
		
		for(INBT var : var0) {
			CompoundNBT var1 = (CompoundNBT)var;
			IPlayerAttribute var2 = PlayerAttributes.fromRegistryName(var1.getString("Name"));
			
			this.clientStore.put(var2, var1.getFloat("Value"));
		}
	}
	
	/**
	 * Syncs data from the server to the client if a change is detected.
	 * @param par0
	 */
	public void sync(PlayerEntity par0) {
		if(par0 == null) return;
		
		Map<IPlayerAttribute, Float> var0 = new HashMap<>();
		
		for(IPlayerAttribute var : PlayerAttributes.attributes()) {
			var0.put(var, (float)get(par0, var));
		}
		
		if(!var0.equals(this.previousStore)) {
			this.previousStore = var0;
			
			send(par0);
		}
	}
}
