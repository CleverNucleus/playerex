package com.github.clevernucleus.playerex.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PersistentPlayerCache;
import com.github.clevernucleus.playerex.util.NameLevelPair;
import com.mojang.authlib.GameProfile;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;

public final class PersistentPlayerCacheManager implements PersistentPlayerCache {
	private final Map<UUID, NameLevelPair> cache;
	
	public PersistentPlayerCacheManager() {
		this.cache = new HashMap<UUID, NameLevelPair>();
	}
	
	@Nullable
	private NameLevelPair convert(final ServerPlayerEntity player, final GameProfile profile) {
		EntityAttribute attribute = ExAPI.LEVEL.get();
		AttributeContainer container = player.getAttributes();
		
		if(attribute == null || !container.hasAttribute(attribute)) return null;
		
		String name = profile.getName();
		int level = Math.round((float)container.getValue(attribute));
		
		NameLevelPair pair = new NameLevelPair(name, level);
		
		return pair;
	}
	
	public void cachePlayer(final ServerPlayerEntity player) {
		GameProfile profile = player.getGameProfile();
		UUID uuid = profile.getId();
		
		if(uuid == null || this.cache.containsKey(uuid)) return;
		
		NameLevelPair pair = this.convert(player, profile);
		
		if(pair == null) return;
		
		this.cache.put(uuid, pair);
	}
	
	public void removePlayer(final UUID uuid) {
		if(uuid == null) return;
		
		this.cache.remove(uuid);
	}
	
	public void removePlayer(final ServerPlayerEntity player) {
		GameProfile profile = player.getGameProfile();
		UUID uuid = profile.getId();
		
		this.removePlayer(uuid);
	}
	
	public void update(final ServerPlayerEntity player) {
		GameProfile profile = player.getGameProfile();
		UUID uuid = profile.getId();
		
		if(uuid == null || !this.cache.containsKey(uuid)) return;
		
		NameLevelPair pair = this.convert(player, profile);
		
		if(pair == null) return;
		
		this.cache.put(uuid, pair);
	}
	
	@Override
	public void clear() {
		this.cache.clear();
	}
	
	@Override
	public NameLevelPair[] get() {
		NameLevelPair[] pairs = this.cache.values().toArray(new NameLevelPair[this.cache.size()]);
		
		return pairs;
	}
	
	@Override
	public NameLevelPair get(final ServerPlayerEntity player) {
		GameProfile profile = player.getGameProfile();
		UUID uuid = profile.getId();
		
		if(uuid == null || !this.cache.containsKey(uuid)) return new NameLevelPair("", 0);
		
		return this.cache.get(uuid);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		if(!tag.contains("Cache")) return;
		
		NbtList list = tag.getList("Cache", NbtType.COMPOUND);
		
		for(int i = 0; i < list.size(); i++) {
			NbtCompound entry = list.getCompound(i);
			UUID uuid = entry.getUuid("UUID");
			String name = entry.getString("Name");
			int level = entry.getInt("Level");
			
			NameLevelPair pair = new NameLevelPair(name, level);
			
			this.cache.put(uuid, pair);
		}
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList list = new NbtList();
		
		for(UUID uuid : this.cache.keySet()) {
			NameLevelPair pair = this.cache.get(uuid);
			NbtCompound entry = new NbtCompound();
			entry.putUuid("UUID", uuid);
			entry.putString("Name", pair.getLeft());
			entry.putInt("Level", pair.getRight());
			list.add(entry);
		}
		
		tag.put("Cache", list);
	}
}
