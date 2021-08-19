package com.github.clevernucleus.playerex.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModifierDataManager implements ModifierData, AutoSyncedComponent {
	private final PlayerEntity player;
	private final Map<Identifier, Double> data;
	private boolean hasLevelPotential;
	
	public ModifierDataManager(PlayerEntity player) {
		this.player = player;
		this.data = new HashMap<Identifier, Double>();
		this.hasLevelPotential = false;
		
		PlayerEx.MANAGER.modifiers.keySet().forEach(key -> this.data.put(key, 0.0D));
	}
	
	private boolean isPresent(final EntityAttribute attributeIn) {
		Identifier registryKey = Registry.ATTRIBUTE.getId(attributeIn);
		
		if(registryKey == null) return false;
		
		return this.data.containsKey(registryKey);
	}
	
	@Nullable
	private UUID uuid(final EntityAttribute attributeIn) {
		Identifier registryKey = Registry.ATTRIBUTE.getId(attributeIn);
		
		return PlayerEx.MANAGER.modifiers.getOrDefault(registryKey, (UUID)null);
	}
	
	private void apply(final EntityAttribute attributeIn) {
		UUID uuid = this.uuid(attributeIn);
		
		if(uuid == null) return;
		
		EntityAttributeInstance instance = this.player.getAttributeInstance(attributeIn);
		EntityAttributeModifier modifier = new EntityAttributeModifier(uuid, "PlayerEx Modifier", this.get(attributeIn), EntityAttributeModifier.Operation.ADDITION);
		
		if(instance == null) return;
		
		instance.removeModifier(modifier);
		instance.addPersistentModifier(modifier);
	}
	
	private void remove(final EntityAttribute attributeIn) {
		UUID uuid = this.uuid(attributeIn);
		
		if(uuid == null) return;
		
		EntityAttributeInstance instance = this.player.getAttributeInstance(attributeIn);
		
		if(instance == null) return;
		
		instance.removeModifier(uuid);
	}
	
	public boolean hasLevelPotential() {
		return this.hasLevelPotential;
	}
	
	public void setHasLevelPotential(final boolean hasLevelPotential) {
		this.hasLevelPotential = hasLevelPotential;
	}
	
	public void refresh(final ModifierDataManager manager) {
		for(Identifier identifier : manager.data.keySet()) {
			EntityAttribute attribute = Registry.ATTRIBUTE.get(identifier);
			
			if(attribute == null) continue;
			
			this.data.replace(identifier, manager.data.getOrDefault(identifier, 0.0D));
			this.apply(attribute);
		}
		
		ExAPI.DATA.sync(this.player);
	}
	
	@Override
	public void reset() {
		for(Identifier identifier : this.data.keySet()) {
			EntityAttribute attribute = Registry.ATTRIBUTE.get(identifier);
			
			if(attribute == null) continue;
			
			this.data.replace(identifier, 0.0D);
			this.remove(attribute);
		}
		
		ExAPI.DATA.sync(this.player);
	}
	
	@Override
	public double get(final EntityAttribute attributeIn) {
		if(!this.isPresent(attributeIn)) return 0.0D;
		
		Identifier registryKey = Registry.ATTRIBUTE.getId(attributeIn);
		
		return this.data.getOrDefault(registryKey, 0.0D);
	}
	
	@Override
	public void set(final EntityAttribute attributeIn, final double valueIn) {
		if(!this.isPresent(attributeIn)) return;
		
		Identifier registryKey = Registry.ATTRIBUTE.getId(attributeIn);
		
		this.data.replace(registryKey, valueIn);
		this.apply(attributeIn);
		
		ExAPI.DATA.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			NbtCompound entry = new NbtCompound();
			entry.putString("Key", registryKey.toString());
			entry.putDouble("Value", valueIn);
			
			tag.put("Modifier", entry);
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player == this.player;
	}
	
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
		
		if(tag == null) return;
		
		if(tag.contains("Modifiers")) {
			NbtList data = tag.getList("Modifiers", NbtType.COMPOUND);
			
			for(int i = 0; i < data.size(); i++) {
				NbtCompound entry = data.getCompound(i);
				String key = entry.getString("Key");
				Identifier identifier = new Identifier(key);
				double value = entry.getDouble("Value");
				this.data.put(identifier, value);
			}
		}
		
		if(tag.contains("Modifier")) {
			NbtCompound entry  = tag.getCompound("Modifier");
			Identifier key = new Identifier(entry.getString("Key"));
			double value = entry.getDouble("Value");
			
			this.data.replace(key, value);
		}
		
		if(tag.contains("Potential")) {
			this.hasLevelPotential = tag.getBoolean("Potential");
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList data = tag.getList("Modifiers", NbtType.COMPOUND);
		
		for(int i = 0; i < data.size(); i++) {
			NbtCompound entry = data.getCompound(i);
			String key = entry.getString("Key");
			Identifier identifier = new Identifier(key);
			double value = entry.getDouble("Value");
			this.data.put(identifier, value);
		}
		
		this.hasLevelPotential = tag.getBoolean("Potential");
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList data = new NbtList();
		
		for(Identifier identifier : this.data.keySet()) {
			NbtCompound entry = new NbtCompound();
			String key = identifier.toString();
			double value = this.data.get(identifier);
			entry.putString("Key", key);
			entry.putDouble("Value", value);
			data.add(entry);
		}
		
		tag.put("Modifiers", data);
		tag.putBoolean("Potential", this.hasLevelPotential);
	}
}
