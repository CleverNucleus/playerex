package com.github.clevernucleus.playerex.client;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.handler.NetworkHandler;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public final class NetworkHandlerClient {
	public static CompletableFuture<PacketByteBuf> loginQueryReceived(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
		NbtCompound tag = buf.readNbt();
		
		client.execute(() -> {
			if(tag.contains("Config")) {
				NbtCompound cfg = tag.getCompound("Config");
				
				PlayerEx.CONFIG.read(cfg);
			}
			
			PlayerEx.MANAGER.modifiers.clear();
			
			if(tag.contains("Modifiers")) {
				NbtList list = tag.getList("Modifiers", NbtType.COMPOUND);
				
				for(int i = 0; i < list.size(); i++) {
					NbtCompound entry = list.getCompound(i);
					Identifier key = new Identifier(entry.getString("Key"));
					UUID value = entry.getUuid("Value");
					
					PlayerEx.MANAGER.modifiers.put(key, value);
				}
			}
		});
		
		PacketByteBuf bufOut = PacketByteBufs.create();
		bufOut.writeString(PlayerEx.VERSION);
		
		return CompletableFuture.completedFuture(bufOut);
	}
	
	public static void levelUpEvent(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		client.execute(() -> {
			client.player.playSound(PlayerEx.LEVEL_UP_SOUND, SoundCategory.NEUTRAL, ExAPI.CONFIG.get().levelUpVolume(), 1.5F);
		});
	}
	
	public static void openAttributesScreen(ButtonWidget button) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(false);
		
		ClientPlayNetworking.send(NetworkHandler.SCREEN, buf);
	}
	
	public static void openInventoryScreen(ButtonWidget button) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(true);
		
		ClientPlayNetworking.send(NetworkHandler.SCREEN, buf);
		MinecraftClient client = MinecraftClient.getInstance();
		client.setScreen(new InventoryScreen(client.player));
	}
}
