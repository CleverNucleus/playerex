package com.github.clevernucleus.playerex.client.network;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.ExRegistryImpl;
import com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager;
import com.github.clevernucleus.playerex.impl.attribute.PlayerAttribute;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;

@Environment(EnvType.CLIENT)
public final class ClientNetworkHandler {
	private static PacketByteBuf packet(boolean isInventoryScreen) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(isInventoryScreen);
		
		return buf;
	}
	
	public static void openInventoryScreen(ButtonWidget button) {
		ClientPlayNetworking.send(NetworkHandler.SWITCH_SCREEN, packet(true));
		MinecraftClient client = MinecraftClient.getInstance();
		client.openScreen(new InventoryScreen(client.player));
	}
	
	public static void openAttributesScreen(ButtonWidget button) {
		ClientPlayNetworking.send(NetworkHandler.SWITCH_SCREEN, packet(false));
	}
	
	@SafeVarargs
	public static void modifyAttributes(NetworkHandler.PacketType type, Pair<IPlayerAttribute, Double> ... attributes) {
		if(attributes == null) return;
		
		PacketByteBuf buf = PacketByteBufs.create();
		CompoundTag tag = new CompoundTag();
		ListTag list = new ListTag();
		
		for(Pair<IPlayerAttribute, Double> pair : attributes) {
			CompoundTag data = new CompoundTag();
			IPlayerAttribute attribute = pair.getLeft();
			double value = pair.getRight();
			
			data.putString("Name", attribute.toString());
			data.putDouble("Value", value);
			list.add(data);
		}
		
		tag.put("Data", list);
		tag.putByte("Type", type.key());
		buf.writeCompoundTag(tag);
		
		ClientPlayNetworking.send(NetworkHandler.MODIFY_ATTRIBUTES, buf);
	}
	
	public static void levelUpEvent(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		client.execute(() -> {
			if(ExAPI.CONFIG.get().playLevelUpSound()) {
				client.player.playSound(PlayerEx.LEVEL_UP, SoundCategory.NEUTRAL, ExAPI.CONFIG.get().levelUpVolume(), 1.5F);
			}
		});
	}
	
	public static void syncConfig(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		CompoundTag tag = buf.readCompoundTag();
		
		client.execute(() -> {
			PlayerEx.CONFIG_CACHE.read(tag);
		});
	}
	
	public static void syncAttributes(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		CompoundTag tag = buf.readCompoundTag();
		
		client.execute(() -> {
			if(tag.contains("Attributes")) {
				ListTag attributes = tag.getList("Attributes", NbtType.COMPOUND);
				
				for(int i = 0; i < attributes.size(); i++) {
					CompoundTag attributeTag = attributes.getCompound(i);
					PlayerAttribute attribute = PlayerAttribute.read(attributeTag);
					
					((ExRegistryImpl)ExAPI.REGISTRY.get()).put(attribute.registryKey(), attribute);
				}
				
				AttributeDataManager data = (AttributeDataManager)ExAPI.DATA.get(client.player);
				data.initContainer();
			}
		});
	}
}
