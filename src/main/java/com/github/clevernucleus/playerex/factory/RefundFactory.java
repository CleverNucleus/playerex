package com.github.clevernucleus.playerex.factory;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;

import net.minecraft.entity.player.PlayerEntity;

public final class RefundFactory {
	public static final RefundFactory STORE = new RefundFactory();
	
	private RefundFactory() {}
	
	public void forEach(Consumer<BiFunction<PlayerData, PlayerEntity, Double>> registry) {
		registry.accept((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.CONSTITUTION, 0.0D, value -> data.get(ExAPI.CONSTITUTION)));
		registry.accept((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.STRENGTH, 0.0D, value -> data.get(ExAPI.STRENGTH)));
		registry.accept((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.DEXTERITY, 0.0D, value -> data.get(ExAPI.DEXTERITY)));
		registry.accept((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.INTELLIGENCE, 0.0D, value -> data.get(ExAPI.INTELLIGENCE)));
		registry.accept((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.LUCKINESS, 0.0D, value -> data.get(ExAPI.LUCKINESS)));
	}
}
