package com.github.clevernucleus.playerex.handler;

import java.util.UUID;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.config.ConfigCache;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public final class NetworkHandler {
	public static final Identifier SCREEN = new Identifier(ExAPI.MODID, "screen");
	
	public static void loginQueryStart(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		NbtCompound cfg = new NbtCompound();
		NbtList ids = new NbtList();
		
		ConfigCache.INSTANCE.writeToNbt(cfg);
		
		for(Identifier identifier : PlayerEx.MANAGER.modifiers.keySet()) {
			UUID uuid = PlayerEx.MANAGER.modifiers.get(identifier);
			NbtCompound entry = new NbtCompound();
			entry.putString("key", identifier.toString());
			entry.putUuid("id", uuid);
			ids.add(entry);
		}
		
		tag.put("Cfg", cfg);
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
	
	public static void switchScreen(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		boolean isPlayerInventory = buf.readBoolean();
		
		server.execute(() -> {
			if(player != null) {
				if(isPlayerInventory) {
					player.closeScreenHandler();
				} else {
					player.openHandledScreen(new ExScreenProvider());
				}
			}
		});
	}
}
