package com.github.clevernucleus.playerex.handler;

import java.util.Map;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.util.Maths;
import com.github.clevernucleus.playerex.impl.attribute.PlayerAttribute;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class NetworkHandler {
	public static final Identifier SYNC_CONFIG = new Identifier(ExAPI.MODID, "sync_config");
	public static final Identifier SYNC_ATTRIBUTES = new Identifier(ExAPI.MODID, "sync_attributes");
	public static final Identifier SWITCH_SCREEN = new Identifier(ExAPI.MODID, "switch_screen");
	public static final Identifier MODIFY_ATTRIBUTES = new Identifier(ExAPI.MODID, "modify_attributes");
	public static final Identifier LEVEL_UP_EVENT = new Identifier(ExAPI.MODID, "level_up_event");
	
	public static void levelUpEvent(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, LEVEL_UP_EVENT, PacketByteBufs.create());
	}
	
	public static void switchScreen(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		boolean isPlayerInventory = buf.readBoolean();
		
		server.execute(() -> {
			if(player != null) {
				if(isPlayerInventory) {
					player.closeScreenHandler();
				} else {
					player.openHandledScreen(new AttributesScreenProvider());
				}
			}
		});
	}
	
	public static void modifyAttributes(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		CompoundTag tag = buf.readCompoundTag();
		
		server.execute(() -> {
			if(player != null) {
				AttributeData data = ExAPI.DATA.get(player);
				ListTag list = tag.getList("Data", NbtType.COMPOUND);
				PacketType type = PacketType.from(tag.getByte("Type"));
				
				boolean checked = true;
				
				if(type.equals(PacketType.LEVEL)) {
					if(player.experienceLevel < Maths.requiredXp(player)) {
						checked = false;
					} else {
						player.addExperienceLevels(-Maths.requiredXp(player));
					}
				} else if(type.equals(PacketType.REFUND)) {
					if(data.refundPoints() < 1) {
						checked = false;
					} else {
						data.addRefundPoints(-1);
					}
				} else if(type.equals(PacketType.SKILL)) {
					if(data.get(PlayerAttributes.SKILLPOINTS.get()) < 1.0D) {
						checked = false;
					}
				}
				
				if(checked) {
					for(int i = 0; i < list.size(); i++) {
						CompoundTag dat = list.getCompound(i);
						Identifier key = new Identifier(dat.getString("Name"));
						IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(key);
						double value = dat.getDouble("Value");
						
						data.add(attribute, value);
					}
				}
			}
		});
	}
	
	public static void syncConfig(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		CompoundTag tag = new CompoundTag();
		PlayerEx.CONFIG_CACHE.write(tag);
		buf.writeCompoundTag(tag);
		
		ServerPlayNetworking.send(player, SYNC_CONFIG, buf);
	}
	
	public static void syncAttributes(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		CompoundTag tag = new CompoundTag();
		ListTag attributeList = new ListTag();
		Map<Identifier, IPlayerAttribute> attributes = ExAPI.REGISTRY.get().attributes();
		
		for(Map.Entry<Identifier, IPlayerAttribute> entry : attributes.entrySet()) {
			PlayerAttribute attribute = (PlayerAttribute)entry.getValue();
			CompoundTag attributeTag = new CompoundTag();
			attribute.write(attributeTag);
			attributeList.add(attributeTag);
		}
		
		tag.put("Attributes", attributeList);
		buf.writeCompoundTag(tag);
		
		ServerPlayNetworking.send(player, SYNC_ATTRIBUTES, buf);
	}
	
	public enum PacketType {
		LEVEL(0), SKILL(1), REFUND(2);
		
		private static final PacketType[] TYPES = new PacketType[] {LEVEL, SKILL, REFUND};
		private byte id;
		
		private PacketType(int id) { this.id = (byte)id; }
		
		public static PacketType from(int id) {
			return TYPES[id];
		}
		
		public byte key() {
			return this.id;
		}
	}
}
