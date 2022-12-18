package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

@Mixin(ExperienceOrbEntity.class)
abstract class ExperienceOrbEntityMixin {
	
	@Inject(method = "<init>", at = @At("TAIL"))
	public void playerex_init(World world, double x, double y, double z, int amount, CallbackInfo ci) {
		BlockPos pos = new BlockPos(x, y, z);
		Chunk chunk = world.getChunk(pos);
		
		ExAPI.EXPERIENCE_DATA.maybeGet(chunk).ifPresent(data -> {
			if(data.updateExperienceNegationFactor(amount)) {
				((ExperienceOrbEntity)(Object)this).remove(RemovalReason.DISCARDED);
			}
		});
	}
}
