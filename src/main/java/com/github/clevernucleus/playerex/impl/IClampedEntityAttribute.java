package com.github.clevernucleus.playerex.impl;

import net.minecraft.entity.attribute.EntityAttribute;

public interface IClampedEntityAttribute {
	EntityAttribute withLimits(double min, double max);
}
