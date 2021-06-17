package com.github.clevernucleus.playerex.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.event.PlayerLevelUpEvent;
import com.github.clevernucleus.playerex.api.util.Maths;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	private Random playerex$random = new Random();
	
	@Inject(method = "tick", at = @At("RETURN"))
	private void onTick(CallbackInfo info) {
		EventHandler.onTick((PlayerEntity)(Object)this);
	}
	
	@Inject(method = "addExperience", at = @At("RETURN"))
	private void onExperienceAdded(int experience, CallbackInfo info) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		
		if(!player.world.isClient) {
			AttributeDataManager data = (AttributeDataManager)ExAPI.DATA.get(player);
			int currentXp = player.experienceLevel;
			int requiredXp = Maths.requiredXp(player);
			boolean levelled = data.levelled();
			
			if(currentXp >= requiredXp && experience > 0 && !levelled) {
				PlayerLevelUpEvent.LEVEL_UP.invoker().onLevelUp(player);
				data.setLevelled(true);
			} else if(currentXp < requiredXp) {
				data.setLevelled(false);
			}
		}
	}
	
	@ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), name = "bl3", ordinal = 2)
	public boolean modifyCrit(boolean bl3, Entity target) {
		if(target instanceof LivingEntity) {
			return EventHandler.onCritChance((PlayerEntity)(Object)this, this.playerex$random, bl3);
		}
		
		return bl3;
	}
	
	@ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 2), name = "f", ordinal = 0)
    public float modifyDamage(float f, Entity target) {
		if(target instanceof LivingEntity) {
			float f2 = f / 1.5F;
			
			return EventHandler.onCritDamage((PlayerEntity)(Object)this, this.playerex$random, f2);
		}
		
		return f;
	}
}
