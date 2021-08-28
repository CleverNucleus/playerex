package com.github.clevernucleus.playerex.handler;

import java.util.UUID;

import com.github.clevernucleus.dataattributes.api.API;
import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class NetworkHandler {
	public static final Identifier SYNC = new Identifier(ExAPI.MODID, "sync");
	public static final Identifier LEVEL = new Identifier(ExAPI.MODID, "level");
	public static final Identifier SCREEN = new Identifier(ExAPI.MODID, "screen");
	public static final Identifier MODIFY = new Identifier(ExAPI.MODID, "modify");
	
	public static void loginQueryStart(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		NbtCompound cfg = new NbtCompound();
		NbtList list = new NbtList();
		
		PlayerEx.CONFIG.write(cfg);
		
		for(Identifier identifier : PlayerEx.MANAGER.modifiers.keySet()) {
			NbtCompound entry = new NbtCompound();
			String key = identifier.toString();
			UUID value = PlayerEx.MANAGER.modifiers.get(identifier);
			
			entry.putString("Key", key);
			entry.putUuid("Value", value);
			list.add(entry);
		}
		
		tag.put("Config", cfg);
		tag.put("Modifiers", list);
		buf.writeNbt(tag);
		sender.sendPacket(SYNC, buf);
	}
	
	public static void loginQueryResponse(MinecraftServer server, ServerLoginNetworkHandler handler, boolean understood, PacketByteBuf buf, LoginSynchronizer synchronizer, PacketSender responseSender) {
		if(understood) {
			String version = buf.readString();
			
			if(!version.equals(PlayerEx.VERSION)) {
				handler.disconnect(new LiteralText("Disconnected: client has PlayerEx " + version + ", but the server requires PlayerEx " + PlayerEx.VERSION + "."));
			}
		} else {
			handler.disconnect(new LiteralText("Disconnected: server requires client to have PlayerEx version " + PlayerEx.VERSION + "."));
		}
	}
	
	public static ActionResult levelUpEvent(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, LEVEL, PacketByteBufs.empty());
		
		return ActionResult.PASS;
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
		NbtCompound tag = buf.readNbt();
		
		server.execute(() -> {
			if(player != null) {
				ModifierData data = ExAPI.DATA.get(player);
				NbtList list = tag.getList("Data", NbtType.COMPOUND);
				PacketType type = PacketType.from(tag.getByte("Type"));
				boolean checked = false;
				
				switch(type) {
					case LEVEL : {
						int requiredXp = ExAPI.CONFIG.get().requiredXp(player);
						
						if(player.experienceLevel >= requiredXp) {
							checked = true;
							player.addExperienceLevels(-requiredXp);
						}
						
						break;
					}
					
					case SKILL : {
						AttributeContainer container = player.getAttributes();
						EntityAttribute attribute = ExAPI.SKILL_POINTS.get();
						
						if(attribute == null || !container.hasAttribute(attribute)) break;
						if(container.getValue(attribute) >= 1.0D) {
							checked = true;
						}
						
						break;
					}
					
					case REFUND : {
						if(data.refundPoints() >= 1) {
							checked = true;
							data.addRefundPoints(-1);
						}
						
						break;
					}
				}
				
				if(checked) {
					for(int i = 0; i < list.size(); i++) {
						NbtCompound dat = list.getCompound(i);
						Identifier key = new Identifier(dat.getString("Key"));
						EntityAttribute attribute = API.getAttribute(key).get();
						
						if(attribute == null) continue;
						
						double value = dat.getDouble("Value");
						
						data.add(attribute, value);
					}
				}
			}
		});
	}
	
	public enum PacketType {
		LEVEL((byte)0), SKILL((byte)1), REFUND((byte)2);
		
		private static final PacketType[] TYPES = new PacketType[] {LEVEL, SKILL, REFUND};
		private final byte id;
		
		private PacketType(final byte id) { this.id = id; }
		
		public static PacketType from(final byte id) {
			int index = MathHelper.clamp(id, 0, 2);
			return TYPES[index];
		}
		
		public byte id() {
			return this.id;
		}
	}
}
