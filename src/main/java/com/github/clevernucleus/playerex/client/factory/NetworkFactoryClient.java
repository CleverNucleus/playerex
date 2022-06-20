package com.github.clevernucleus.playerex.client.factory;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.EntityAttributeSupplier;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PacketType;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.factory.NetworkFactory;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
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

public final class NetworkFactoryClient {
	public static CompletableFuture<PacketByteBuf> loginQueryReceived(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
		NbtCompound tag = buf.readNbt();
		
		client.execute(() -> {
			((ConfigImpl)ExAPI.getConfig()).getServerInstance().readFromNbt(tag);
		});
		
		return CompletableFuture.completedFuture(PacketByteBufs.empty());
	}
	
	public static void openAttributesScreen(final int pageId) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(pageId);
		
		ClientPlayNetworking.send(NetworkFactory.SCREEN, buf);
	}
	
	public static void openInventoryScreen(ButtonWidget button) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(-1);
		
		ClientPlayNetworking.send(NetworkFactory.SCREEN, buf);
		MinecraftClient client = MinecraftClient.getInstance();
		client.setScreen(new InventoryScreen(client.player));
	}
	
	public static void notifiedLevelUp(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		client.execute(() -> client.player.playSound(PlayerEx.LEVEL_UP_SOUND, SoundCategory.NEUTRAL, ExAPI.getConfig().levelUpVolume(), 1.5F));
	}
	
	public static void modifyAttributes(PacketType type, Consumer<BiConsumer<EntityAttributeSupplier, Double>>[] consumers) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		NbtList lst = new NbtList();
		
		for(var consumer : consumers) {
			NbtCompound entry = new NbtCompound();
			consumer.accept((supplier, value) -> {
				Identifier identifier = supplier.getId();
				
				if(identifier != null) {
					entry.putString("Key", identifier.toString());
					entry.putDouble("Value", value);
					lst.add(entry);
				}
			});
		}
		
		tag.put("Data", lst);
		tag.putByte("Type", type.id());
		buf.writeNbt(tag);
		
		ClientPlayNetworking.send(NetworkFactory.MODIFY, buf);
	}
}
