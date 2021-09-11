package com.github.clevernucleus.playerex.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
	private double modifyAttackDamage(double d, PlayerEntity player) {
		if(player == null) return d;
		
		ModifierData data = ExAPI.DATA.get(player);
		double value = data.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		
		System.out.println("v: " + value);
		// TODO got sleepy will finish later
		return d + value + 10;
	}
	/*
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 3), ordinal = 0)
	private double modifyAttackSpeed(double d, PlayerEntity player) {
		if(player == null) return d;
		
		ModifierData data = ExAPI.DATA.get(player);
		double value = data.get(EntityAttributes.GENERIC_ATTACK_SPEED);
		
		return d + value;
	}*/
}
