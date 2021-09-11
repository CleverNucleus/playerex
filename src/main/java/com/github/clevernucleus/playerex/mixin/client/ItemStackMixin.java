package com.github.clevernucleus.playerex.mixin.client;

import java.util.UUID;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.util.EntityAttributeInstanceAccess;
import com.github.clevernucleus.playerex.util.ItemFieldAccess;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	private double modifyGeneralisedValue(double dIn, PlayerEntity player, EntityAttribute attributeIn, Supplier<UUID> uuidIn) {
		if(player == null) return dIn;
		
		EntityAttributeInstance instance = player.getAttributeInstance(attributeIn);
		EntityAttributeInstanceAccess access = (EntityAttributeInstanceAccess)instance;
		
		double d = dIn;
		
		for(EntityAttributeModifier modifier : access.modifiersByOperation(EntityAttributeModifier.Operation.ADDITION)) {
			if(modifier.getId().equals(uuidIn.get())) continue;
			
			d += modifier.getValue();
		}
		
		double e = d;
		
		for(EntityAttributeModifier modifier : access.modifiersByOperation(EntityAttributeModifier.Operation.MULTIPLY_BASE)) {
			e += d * modifier.getValue();
		}
		
		for(EntityAttributeModifier modifier : access.modifiersByOperation(EntityAttributeModifier.Operation.MULTIPLY_TOTAL)) {
			e *= 1.0D + modifier.getValue();
		}
		
		return attributeIn.clamp(e);
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
	private double modifyAttackDamage(double d, PlayerEntity player) {
		if(!ExAPI.CONFIG.get().enableTooltipFix()) return d;
		
		return modifyGeneralisedValue(d, player, EntityAttributes.GENERIC_ATTACK_DAMAGE, ItemFieldAccess::attackDamageModifierId);
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 3), ordinal = 0)
	private double modifyAttackSpeed(double d, PlayerEntity player) {
		if(!ExAPI.CONFIG.get().enableTooltipFix()) return d;
		
		return modifyGeneralisedValue(d, player, EntityAttributes.GENERIC_ATTACK_SPEED, ItemFieldAccess::attackSpeedModifierId);
	}
}
