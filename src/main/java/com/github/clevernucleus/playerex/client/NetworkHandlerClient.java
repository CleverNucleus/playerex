package com.github.clevernucleus.playerex.client;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.config.ConfigCache;
import com.github.clevernucleus.playerex.handler.NetworkHandler;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class NetworkHandlerClient {
	public static CompletableFuture<PacketByteBuf> loginQueryReceived(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
		NbtCompound tag = buf.readNbt();
		
		client.execute(() -> {
			if(tag.contains("Cfg")) {
				NbtCompound cfg = tag.getCompound("Cfg");
				ConfigCache.INSTANCE.readFromNbt(cfg);
			}
			
			if(tag.contains("Ids")) {
				NbtList ids = tag.getList("Ids", NbtType.COMPOUND);
				
				for(int i = 0; i < ids.size(); i++) {
					NbtCompound entry = ids.getCompound(i);
					Identifier identifier = new Identifier(entry.getString("key"));
					UUID uuid = entry.getUuid("id");
					
					PlayerEx.MANAGER.cache.put(identifier, uuid);
				}
			}
			
			PlayerEx.MANAGER.load();
		});
		
		PacketByteBuf bufOut = PacketByteBufs.create();
		bufOut.writeString(PlayerEx.VERSION);
		
		return CompletableFuture.completedFuture(bufOut);
	}
	
	public static void openAttributesScreen() {
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
