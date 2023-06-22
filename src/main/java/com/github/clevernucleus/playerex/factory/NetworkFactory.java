package com.github.clevernucleus.playerex.factory;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.EntityAttributeSupplier;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PacketType;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.config.ConfigImpl;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
			handler.disconnect(Text.literal("Disconnected: network communication issue."));
		}
	}
	
	public static void switchScreen(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int pageId = buf.readInt();
		
		server.execute(() -> {
			if(player != null) {
				if(pageId < 0) {
					player.onHandledScreenClosed();
					//player.closeHandledScreen();
				} else {
					if(!ExAPI.getConfig().isAttributesGuiDisabled()) {
						player.openHandledScreen(new ExScreenFactory(pageId));
					}
				}
			}
		});
	}
	
	public static void modifyAttributes(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		NbtCompound tag = buf.readNbt();
		
		server.execute(() -> {
			if(player != null) {
				PlayerData data = ExAPI.PLAYER_DATA.get(player);
				NbtList list = tag.getList("Data", NbtType.COMPOUND);
				PacketType packetType = PacketType.fromId(tag.getByte("Type"));
				
				if(packetType.test(server, player, data)) {
					for(int i = 0; i < list.size(); i++) {
						NbtCompound entry = list.getCompound(i);
						Identifier identifier = new Identifier(entry.getString("Key"));
						EntityAttributeSupplier attribute = EntityAttributeSupplier.of(identifier);
						DataAttributesAPI.ifPresent(player, attribute, (Object)null, amount -> {
							double value = entry.getDouble("Value");
							data.add(attribute, value);
							return (Object)null;
						});
					}
				}
			}
		});
	}
	
	public static void notifyLevelUp(final ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, NOTIFY, PacketByteBufs.empty());
	}
}
