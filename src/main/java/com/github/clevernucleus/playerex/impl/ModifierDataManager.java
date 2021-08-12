package com.github.clevernucleus.playerex.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.clevernucleus.playerex.api.ModifierData;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

public final class ModifierDataManager implements ModifierData, AutoSyncedComponent {
	private final PlayerEntity player;
	private final Map<Identifier, Float> data;
	// TODO most efficient structure yet to be figured out... mhhhh
	public ModifierDataManager(PlayerEntity player) {
		this.player = player;
		this.data = new HashMap<Identifier, Float>();
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		if(!tag.contains("Modifiers")) return;
		
		NbtList data = tag.getList("Modifiers", NbtType.COMPOUND);
		
		for(int i = 0; i < data.size(); i++) {
			NbtCompound entry = data.getCompound(i);
			String key = entry.getString("Key");
			Identifier identifier = new Identifier(key);
			float value = entry.getFloat("Value");
			this.data.put(identifier, value);
		}
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList data = new NbtList();
		
		for(Identifier identifier : this.data.keySet()) {
			NbtCompound entry = new NbtCompound();
			String key = identifier.toString();
			float value = this.data.get(identifier);
			entry.putString("Key", key);
			entry.putFloat("Value", value);
			data.add(entry);
		}
		
		tag.put("Modifiers", data);
	}
}
