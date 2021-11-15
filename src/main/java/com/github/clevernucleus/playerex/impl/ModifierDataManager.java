package com.github.clevernucleus.playerex.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;
import com.google.common.collect.Lists;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public final class ModifierDataManager implements ModifierData, AutoSyncedComponent {
	private final PlayerEntity player;
	private final Map<Identifier, Double> data;
	//private final Map<Identifier, Double> cache;
	private boolean hasLevelPotential;
	private int refundPoints;
	
	public ModifierDataManager(PlayerEntity player) {
		this.player = player;
		this.data = new HashMap<Identifier, Double>();
		//this.cache = new HashMap<Identifier, Double>();
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
	
	private void syncCache() {
		/*
		ExAPI.DATA.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			NbtList cache = new NbtList();
			
			for(Identifier identifier : this.cache.keySet()) {
				NbtCompound entry = new NbtCompound();
				entry.putString("Key", identifier.toString());
				entry.putDouble("Value", this.cache.get(identifier));
				cache.add(entry);
			}
			
			tag.put("Cache", cache);
			buf.writeNbt(tag);
		});
		*/
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
	public int refundPoints() {
		return this.refundPoints;
	}
	
	@Override
	public int addRefundPoints(final int pointsIn) {
		List<Supplier<EntityAttribute>> primaries = Lists.newArrayList(
			ExAPI.CONSTITUTION,
			ExAPI.STRENGTH,
			ExAPI.DEXTERITY,
			ExAPI.INTELLIGENCE,
			ExAPI.LUCKINESS
		);
		
		final int previousRefunds = this.refundPoints;
		double maxRefundPoints = 0.0D;
		
		for(Supplier<EntityAttribute> supplier : primaries) {
			EntityAttribute attribute = supplier.get();
			
			if(attribute == null) continue;
			
			maxRefundPoints += this.get(attribute);
		}
		
		double refund = MathHelper.clamp((double)(this.refundPoints + pointsIn), 0.0D, maxRefundPoints);
		this.refundPoints = (int)Math.round(refund);
		
		ExAPI.DATA.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.putInt("RefundPoints", this.refundPoints);
			buf.writeNbt(tag);
		});
		
		return this.refundPoints - previousRefunds;
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
	public void add(final EntityAttribute attributeIn, final double valueIn) {
		final double value = this.get(attributeIn);
		
		this.set(attributeIn, value + valueIn);
	}
	
	@Override
	public void putInCache(final Identifier keyIn, final double valueIn) {
		if(keyIn == null) return;
		//this.cache.put(keyIn, valueIn);
		this.syncCache();
	}
	
	@Override
	public void removeFromCache(final Identifier keyIn) {
		if(keyIn == null) return;
		//this.cache.remove(keyIn);
		this.syncCache();
	}
	
	@Override
	public double getFromCache(final Identifier keyIn) {
		if(keyIn == null) return 0.0D;
		return 0.0D;//this.cache.getOrDefault(keyIn, 0.0D);
	}
	
	@Override
	public boolean cacheContains(final Identifier keyIn) {
		if(keyIn == null) return false;
		return false;//this.cache.containsKey(keyIn);
	}
	
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player == this.player;
	}
	
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
		
		if(tag == null) return;
		/*
		if(tag.contains("Cache")) {
			NbtList cache = tag.getList("Cache", NbtType.COMPOUND);
			
			for(int i = 0; i < cache.size(); i++) {
				NbtCompound entry = cache.getCompound(i);
				String key = entry.getString("Key");
				Identifier identifier = new Identifier(key);
				double value = entry.getDouble("Value");
				this.cache.put(identifier, value);
			}
		}
		*/
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
		
		if(tag.contains("RefundPoints")) {
			this.refundPoints = tag.getInt("RefundPoints");
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList data = tag.getList("Modifiers", NbtType.COMPOUND);
		//NbtList cache = tag.getList("Cache", NbtType.COMPOUND);
		
		for(int i = 0; i < data.size(); i++) {
			NbtCompound entry = data.getCompound(i);
			String key = entry.getString("Key");
			Identifier identifier = new Identifier(key);
			double value = entry.getDouble("Value");
			this.data.put(identifier, value);
		}
		/*
		for(int i = 0; i < cache.size(); i++) {
			NbtCompound entry = cache.getCompound(i);
			String key = entry.getString("Key");
			Identifier identifier = new Identifier(key);
			double value = entry.getDouble("Value");
			this.cache.put(identifier, value);
		}
		*/
		this.hasLevelPotential = tag.getBoolean("Potential");
		this.refundPoints = tag.getInt("RefundPoints");
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList data = new NbtList();
		//NbtList cache = new NbtList();
		
		for(Identifier identifier : this.data.keySet()) {
			NbtCompound entry = new NbtCompound();
			String key = identifier.toString();
			double value = this.data.get(identifier);
			entry.putString("Key", key);
			entry.putDouble("Value", value);
			data.add(entry);
		}
		/*
		for(Identifier identifier : this.cache.keySet()) {
			NbtCompound entry = new NbtCompound();
			String key = identifier.toString();
			double value = this.cache.get(identifier);
			entry.putString("Key", key);
			entry.putDouble("Value", value);
			cache.add(entry);
		}
		*/
		tag.put("Modifiers", data);
		//tag.put("Cache", cache);
		tag.putBoolean("Potential", this.hasLevelPotential);
		tag.putInt("RefundPoints", this.refundPoints);
	}
}
