package com.github.clevernucleus.playerex.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttributeInstance;
import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class PlayerDataManager implements PlayerData, AutoSyncedComponent {
	private final PlayerEntity player;
	private final Map<Identifier, Double> data;
	
	public PlayerDataManager(PlayerEntity player) {
		this.player = player;
		this.data = new HashMap<Identifier, Double>();
	}
	
	private UUID uuid(final EntityAttribute attributeIn) {
		Identifier identifier = Registry.ATTRIBUTE.getId(attributeIn);
		return PlayerEx.MANAGER.modifiers.getOrDefault(identifier, (UUID)null);
	}
	
	@Override
	public double get(final EntityAttribute attributeIn) {
		Identifier identifier = Registry.ATTRIBUTE.getId(attributeIn);
		
		if(identifier == null) return 0.0D;
		
		return this.data.get(identifier);
	}
	
	@Override
	public void set(final EntityAttribute attributeIn, final double valueIn) {
		Identifier identifier = Registry.ATTRIBUTE.getId(attributeIn);
		
		if(identifier == null) return;
		
		AttributeContainer container = this.player.getAttributes();
		EntityAttributeInstance instance = container.getCustomInstance(attributeIn);
		
		if(instance == null) return;
		
		UUID uuid = this.uuid(attributeIn);
		
		if(instance.getModifier(uuid) == null) {
			instance.addPersistentModifier(null);
		} else {
			((IEntityAttributeInstance)instance).updateModifier(uuid, valueIn);
		}
		
		this.data.put(identifier, valueIn);
		
		ExAPI.INSTANCE.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			NbtCompound entry = new NbtCompound();
			entry.putString("Key", identifier.toString());
			entry.putDouble("Value", valueIn);
			tag.put("Set", entry);
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public void add(final EntityAttribute attributeIn, final double valueIn) {
		final double value = this.get(attributeIn);
		this.set(attributeIn, value + valueIn);
	}
	
	@Override
	public void remove(final EntityAttribute attributeIn) {
		Identifier identifier = Registry.ATTRIBUTE.getId(attributeIn);
		
		if(identifier == null) return;
		
		AttributeContainer container = this.player.getAttributes();
		EntityAttributeInstance instance = container.getCustomInstance(attributeIn);
		
		if(instance == null) return;
		
		UUID uuid = this.uuid(attributeIn);
		
		if(instance.getModifier(uuid) != null) {
			instance.removeModifier(uuid);
		}
		
		this.data.remove(identifier);
		
		ExAPI.INSTANCE.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.putString("Remove", identifier.toString());
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
		
		if(tag.contains("Set")) {
			NbtCompound entry = tag.getCompound("Set");
			Identifier identifier = new Identifier(entry.getString("Key"));
			double value = entry.getDouble("Value");
			this.data.put(identifier, value);
		}
		
		if(tag.contains("Remove")) {
			Identifier identifier = new Identifier(tag.getString("Remove"));
			this.data.remove(identifier);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList modifiers = tag.getList("Modifiers", NbtType.COMPOUND);
		
		for(int i = 0; i < modifiers.size(); i++) {
			NbtCompound entry = modifiers.getCompound(i);
			Identifier identifier = new Identifier(entry.getString("Key"));
			double value = entry.getDouble("Value");
			this.data.put(identifier, value);
		}
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList modifiers = new NbtList();
		
		for(Identifier identifier : this.data.keySet()) {
			NbtCompound entry = new NbtCompound();
			double value = this.data.get(identifier);
			entry.putString("Key", identifier.toString());
			entry.putDouble("Value", value);
			modifiers.add(entry);
		}
		
		tag.put("Modifiers", modifiers);
	}
}
