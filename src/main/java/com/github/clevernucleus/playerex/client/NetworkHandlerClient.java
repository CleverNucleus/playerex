package com.github.clevernucleus.playerex.client;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.github.clevernucleus.playerex.PlayerEx;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
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
}
