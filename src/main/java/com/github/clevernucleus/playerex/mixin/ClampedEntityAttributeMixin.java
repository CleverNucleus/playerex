package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.playerex.impl.attribute.IClampedEntityAttribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.math.MathHelper;

@Mixin(ClampedEntityAttribute.class)
public abstract class ClampedEntityAttributeMixin extends EntityAttribute implements IClampedEntityAttribute {
	
	@Unique
	private double playerex$min, playerex$max;
	
	@Shadow
	@Final
	private double minValue;
	
	@Shadow
	@Final
	private double maxValue;
	
	protected ClampedEntityAttributeMixin(String translationKey, double fallback) {
		super(translationKey, fallback);
	}
	
	@Inject(method = "clamp", require = 1, allow = 1, at = @At("HEAD"), cancellable = true)
	private void clamp(double value, CallbackInfoReturnable<Double> info) {
		double trueMin = Math.min(this.playerex$min, this.minValue);
		double trueMax = Math.max(this.playerex$max, this.maxValue);
		double result = MathHelper.clamp(value, trueMin, trueMax);
		
		info.setReturnValue(result);
	}
	
	@Override
	public EntityAttribute withLimits(double min, double max) {
		this.playerex$min = min;
		this.playerex$max = max;
		
		return this;
	}
}
