package com.github.clevernucleus.playerex.handler;

import java.util.UUID;

import com.github.clevernucleus.playerex.PlayerEx;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public final class NetworkHandler {
	public static void loginQueryStart(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		NbtList ids = new NbtList();
		
		for(Identifier identifier : PlayerEx.MANAGER.modifiers.keySet()) {
			UUID uuid = PlayerEx.MANAGER.modifiers.get(identifier);
			NbtCompound entry = new NbtCompound();
			entry.putString("key", identifier.toString());
			entry.putUuid("id", uuid);
			ids.add(entry);
		}
		
		tag.put("Ids", ids);
		buf.writeNbt(tag);
		sender.sendPacket(PlayerEx.HANDSHAKE, buf);
	}
	
	public static void loginQueryResponse(MinecraftServer server, ServerLoginNetworkHandler handler, boolean understood, PacketByteBuf buf, LoginSynchronizer synchronizer, PacketSender responseSender) {
		if(understood) {
			String version = buf.readString();
			
			if(!version.equals(PlayerEx.VERSION)) {
				handler.disconnect(new LiteralText("Disconnected: client has PlayerEx " + version + ", but the server requires PlayerEx " + PlayerEx.VERSION + "."));
			}
		} else {
			handler.disconnect(new LiteralText("Disconnected: server requires client to have PlayerEx version " + PlayerEx.VERSION + "."));
		}
	}
}
