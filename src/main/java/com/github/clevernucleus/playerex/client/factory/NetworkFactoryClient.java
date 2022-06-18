package com.github.clevernucleus.playerex.client.factory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.config.ConfigImpl;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;

public final class NetworkFactoryClient {
	public static CompletableFuture<PacketByteBuf> loginQueryReceived(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
		NbtCompound tag = buf.readNbt();
		
		client.execute(() -> {
			((ConfigImpl)ExAPI.getConfig()).getServerInstance().readFromNbt(tag);
		});
		
		return CompletableFuture.completedFuture(PacketByteBufs.empty());
	}
	
	public static void notifiedLevelUp(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		client.execute(() -> client.player.playSound(PlayerEx.LEVEL_UP_SOUND, SoundCategory.NEUTRAL, ExAPI.getConfig().levelUpVolume(), 1.5F));
	}
}
