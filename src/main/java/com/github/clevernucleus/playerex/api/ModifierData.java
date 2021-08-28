package com.github.clevernucleus.playerex.api;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.attribute.EntityAttribute;


public interface ModifierData extends Component {
	
	
	int refundPoints();
	
	
	int addRefundPoints(final int pointsIn);
	
	
	double get(final EntityAttribute attributeIn);
	
	
	void set(final EntityAttribute attributeIn, final double valueIn);
	
	
	void add(final EntityAttribute attributeIn, final double valueIn);
	
	
	void reset();
}
