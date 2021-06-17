package com.github.clevernucleus.playerex.handler;

import java.util.Map;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.impl.attribute.PlayerAttribute;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class NetworkHandler {
	public static final Identifier SYNC_CONFIG = new Identifier(ExAPI.MODID, "sync_config");
	public static final Identifier SYNC_ATTRIBUTES = new Identifier(ExAPI.MODID, "sync_attributes");
	
	public static void syncConfig(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		CompoundTag tag = new CompoundTag();
		PlayerEx.CONFIG_CACHE.write(tag);
		buf.writeCompoundTag(tag);
		
		ServerPlayNetworking.send(player, SYNC_CONFIG, buf);
	}
	
	public static void syncAttributes(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		CompoundTag tag = new CompoundTag();
		ListTag attributeList = new ListTag();
		Map<Identifier, IPlayerAttribute> attributes = ExAPI.REGISTRY.get().attributes();
		
		for(Map.Entry<Identifier, IPlayerAttribute> entry : attributes.entrySet()) {
			PlayerAttribute attribute = (PlayerAttribute)entry.getValue();
			CompoundTag attributeTag = new CompoundTag();
			attribute.write(attributeTag);
			attributeList.add(attributeTag);
		}
		
		tag.put("Attributes", attributeList);
		buf.writeCompoundTag(tag);
		
		ServerPlayNetworking.send(player, SYNC_ATTRIBUTES, buf);
	}
}
