package com.github.clevernucleus.playerex.impl;

import org.apache.commons.lang3.function.TriFunction;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PacketType;
import com.github.clevernucleus.playerex.api.PlayerData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public enum PacketTypes implements PacketType {
	LEVEL("level", PacketTypes::level), SKILL("skill", PacketTypes::skill), REFUND("refund", PacketTypes::refund);
	
	private final String id;
	private final TriFunction<MinecraftServer, ServerPlayerEntity, PlayerData, Boolean> function;
	
	private PacketTypes(final String id, TriFunction<MinecraftServer, ServerPlayerEntity, PlayerData, Boolean> function) {
		this.id = id;
		this.function = function;
	}
	
	private static boolean level(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		int requiredXp = ExAPI.getConfig().requiredXp(player);
		
		if(player.experienceLevel >= requiredXp) {
			player.addExperience(-requiredXp);
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean skill(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		return DataAttributesAPI.ifPresent(player, ExAPI.SKILL_POINTS, false, value -> value >= 1.0F);
	}
	
	private static boolean refund(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		if(data.refundPoints() >= 1) {
			data.addRefundPoints(-1);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String id() {
		return this.id;
	}
	
	@Override
	public boolean test(final MinecraftServer server, final ServerPlayerEntity player, final PlayerData data) {
		return this.function.apply(server, player, data);
	}
}
