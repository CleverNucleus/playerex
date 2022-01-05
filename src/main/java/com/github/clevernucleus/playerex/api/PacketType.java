package com.github.clevernucleus.playerex.api;

import java.util.Map;

import org.apache.commons.lang3.function.TriFunction;

import com.github.clevernucleus.dataattributes.api.util.Maths;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public enum PacketType {
	DEFAULT((byte)0, (server, player, data) -> true),
	LEVEL((byte)1, PacketType::level),
	SKILL((byte)2, PacketType::skill),
	REFUND((byte)3, PacketType::refund);
	
	private static final Map<Byte, PacketType> TYPES = Maths.enumLookupMap(PacketType.values(), v -> v.id());
	
	private final byte id;
	private final TriFunction<MinecraftServer, ServerPlayerEntity, PlayerData, Boolean> function;
	
	private PacketType(final byte id, TriFunction<MinecraftServer, ServerPlayerEntity, PlayerData, Boolean> function) {
		this.id = id;
		this.function = function;
	}
	
	private static boolean level(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		int requiredXp = ExAPI.getConfig().requiredXp(player);
		
		if(player.experienceLevel >= requiredXp) {
			player.addExperienceLevels(-requiredXp);
			data.addSkillPoints(1);
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean skill(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		if(data.skillPoints() >= 1) {
			data.addSkillPoints(-1);
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean refund(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		if(data.refundPoints() >= 1) {
			data.addRefundPoints(-1);
			data.addSkillPoints(1);
			return true;
		} else {
			return false;
		}
	}
	
	public static PacketType fromId(final byte id) {
		return TYPES.getOrDefault(id, DEFAULT);
	}
	
	public byte id() {
		return this.id;
	}
	
	public boolean test(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		return this.function.apply(server, player, data);
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.id);
	}
}
