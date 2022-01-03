package com.github.clevernucleus.playerex.client;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.PacketType;
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
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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
	
	public static void modifyAttributes(PacketType type, Consumer<BiConsumer<Supplier<EntityAttribute>, Double>>[] consumers) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		NbtList lst = new NbtList();
		
		for(var consumer : consumers) {
			NbtCompound entry = new NbtCompound();
			consumer.accept((supplier, value) -> {
				EntityAttribute attribute = supplier.get();
				Identifier identifier = Registry.ATTRIBUTE.getId(attribute);
				
				if(identifier != null) {
					entry.putString("Key", identifier.toString());
					entry.putDouble("Value", value);
					lst.add(entry);
				}
			});
		}
		
		tag.put("Data", lst);
		tag.putString("Type", type.id());
		buf.writeNbt(tag);
		
		ClientPlayNetworking.send(NetworkHandler.MODIFY, buf);
	}
}
