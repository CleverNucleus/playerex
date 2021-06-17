package com.github.clevernucleus.playerex.client.network;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.impl.ExRegistryImpl;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;

public final class ClientNetworkHandler {
	public static void syncConfig(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		CompoundTag tag = buf.readCompoundTag();
		
		client.execute(() -> {
			PlayerEx.CONFIG_CACHE.read(tag);
		});
	}
	
	public static void syncAttributes(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		CompoundTag tag = buf.readCompoundTag();
		
		client.execute(() -> {
			if(tag.contains("Attributes")) {
				ListTag attributes = tag.getList("Attributes", NbtType.COMPOUND);
				
				for(int i = 0; i < attributes.size(); i++) {
					CompoundTag attributeTag = attributes.getCompound(i);
					ClientPlayerAttribute attribute = ClientPlayerAttribute.read(attributeTag);
					((ExRegistryImpl)ExAPI.REGISTRY.get()).put(attribute.registryKey(), attribute);
				}
			}
		});
	}
}
