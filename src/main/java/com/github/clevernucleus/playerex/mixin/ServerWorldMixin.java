package com.github.clevernucleus.playerex.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
abstract class ServerWorldMixin {
	
	@Redirect(method = "addEntity", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
	private void playerex_addEntity(Logger logger, String arg0, Object arg1) {}
}
