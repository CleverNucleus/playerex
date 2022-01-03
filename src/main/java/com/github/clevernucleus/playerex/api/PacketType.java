package com.github.clevernucleus.playerex.api;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;


public interface PacketType {
	/** Equivalent of ItemStack#EMPTY. */
	public static final PacketType DEFAULT = new PacketType() {};
	
	
	public static void registerPacketTypes(final PacketType ... packetTypes) {
		if(packetTypes == null) return;
		
		for(PacketType packetType : packetTypes) {
			com.github.clevernucleus.playerex.handler.NetworkHandler.addPacketType(packetType);
		}
	}
	
	default String id() {
		return "";
	}
	
	
	default boolean test(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		return true;
	}
}
