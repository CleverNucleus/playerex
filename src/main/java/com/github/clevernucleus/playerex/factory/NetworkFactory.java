package com.github.clevernucleus.playerex.factory;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.config.ConfigImpl;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public final class NetworkFactory {
	public static final Identifier CONFIG = new Identifier(ExAPI.MODID, "config");
	public static final Identifier MODIFY = new Identifier(ExAPI.MODID, "modify");
	public static final Identifier SCREEN = new Identifier(ExAPI.MODID, "screen");
	public static final Identifier NOTIFY = new Identifier(ExAPI.MODID, "notify");
	
	public static void loginQueryStart(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		
		((ConfigImpl)ExAPI.getConfig()).getServerInstance().writeToNbt(tag);
		
		buf.writeNbt(tag);
		sender.sendPacket(CONFIG, buf);
	}
	
	public static void loginQueryResponse(MinecraftServer server, ServerLoginNetworkHandler handler, boolean understood, PacketByteBuf buf, LoginSynchronizer synchronizer, PacketSender responseSender) {
		if(!understood) {
			handler.disconnect(new LiteralText("Disconnected: network communication issue."));
		}
	}
	
	public static void switchScreen(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		// TODO
		
		server.execute(() -> {
			
		});
	}
	
	public static void modifyAttributes(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		// TODO
		
		server.execute(() -> {
			
		});
	}
	
	public static void notifyLevelUp(final ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, NOTIFY, PacketByteBufs.empty());
	}
}
