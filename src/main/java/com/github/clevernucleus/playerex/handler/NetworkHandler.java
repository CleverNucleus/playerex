package com.github.clevernucleus.playerex.handler;

import java.util.UUID;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public final class NetworkHandler {
	public static final Identifier SYNC = new Identifier(ExAPI.MODID, "sync");
	public static final Identifier LEVEL = new Identifier(ExAPI.MODID, "level");
	
	public static void loginQueryStart(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		NbtCompound cfg = new NbtCompound();
		NbtList list = new NbtList();
		
		PlayerEx.CONFIG.write(cfg);
		
		for(Identifier identifier : PlayerEx.MANAGER.modifiers.keySet()) {
			NbtCompound entry = new NbtCompound();
			String key = identifier.toString();
			UUID value = PlayerEx.MANAGER.modifiers.get(identifier);
			
			entry.putString("Key", key);
			entry.putUuid("Value", value);
			list.add(entry);
		}
		
		tag.put("Config", cfg);
		tag.put("Modifiers", list);
		buf.writeNbt(tag);
		sender.sendPacket(SYNC, buf);
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
	
	public static ActionResult levelUpEvent(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, LEVEL, PacketByteBufs.empty());
		
		return ActionResult.PASS;
	}
}
