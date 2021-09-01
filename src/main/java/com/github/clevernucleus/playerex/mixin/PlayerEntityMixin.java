package com.github.clevernucleus.playerex.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.event.PlayerLevelUpEvent;
import com.github.clevernucleus.playerex.impl.ModifierDataManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {
	
	@Inject(method = "addExperience", at = @At("TAIL"))
	private void onAddExperience(int experience, CallbackInfo info) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		
		if(player.world.isClient) return;
		
		ModifierDataManager data = (ModifierDataManager)ExAPI.DATA.get(player);
		int currentXp = player.experienceLevel;
		int requiredXp = ExAPI.CONFIG.get().requiredXp(player);
		boolean hasLevelPotential = data.hasLevelPotential();
		boolean newLevelPotential = hasLevelPotential;
		
		if(!hasLevelPotential && currentXp >= requiredXp) {
			if(player instanceof ServerPlayerEntity) {
				ActionResult result = PlayerLevelUpEvent.EVENT.invoker().onLevelUp((ServerPlayerEntity)player);
				
				if(result != ActionResult.PASS) {
					return;
				}
			}
			
			newLevelPotential = true;
		} else if(hasLevelPotential && currentXp < requiredXp) {
			newLevelPotential = false;
		}
		
		if(hasLevelPotential != newLevelPotential) {
			data.setHasLevelPotential(newLevelPotential);
			final boolean potential = newLevelPotential;
			
			ExAPI.DATA.sync(player, (buf, p) -> {
				NbtCompound tag = new NbtCompound();
				tag.putBoolean("Potential", potential);
				
				buf.writeNbt(tag);
			});
		}
	}
	
	@ModifyVariable(method = "attack", at = @At("STORE"), name = "bl3", ordinal = 2)
	private boolean onAttackChance(boolean bl3, Entity target) {
		if(target instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity)(Object)this;
			AttributeContainer container = player.getAttributes();
			EntityAttribute attribute = ExAPI.MELEE_CRIT_CHANCE.get();
			
			if(!container.hasAttribute(attribute)) return false;
			float value = (float)container.getValue(attribute);
			float crits = (new Random()).nextFloat();
			
			return (crits < value) && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
		}
		
		return false;
	}
	
	@ModifyVariable(method = "attack", at = @At("STORE"), name = "f", ordinal = 2)
	private float onAttackDamage(float f, Entity target) {
		if(target instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity)(Object)this;
			AttributeContainer container = player.getAttributes();
			EntityAttribute attribute = ExAPI.MELEE_CRIT_DAMAGE.get();
			
			if(!container.hasAttribute(attribute)) return f;
			float value = (float)container.getValue(attribute);
			
			return f * (1.0F + value);
		}
		
		return f;
	}
}
