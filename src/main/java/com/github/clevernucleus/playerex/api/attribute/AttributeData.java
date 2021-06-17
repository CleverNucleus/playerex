package com.github.clevernucleus.playerex.api.attribute;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.attribute.EntityAttributeModifier;


public interface AttributeData extends Component {
	
	
	void addRefundPoints(final int pointsIn);
	
	
	int refundPoints();
	
	
	double get(final IPlayerAttribute keyIn);
	
	
	void set(final IPlayerAttribute keyIn, final double valueIn);
	
	
	void add(final IPlayerAttribute keyIn, final double valueIn);
	
	
	void applyAttributeModifier(final IPlayerAttribute keyIn, final EntityAttributeModifier modifierIn);
	
	
	void removeAttributeModifier(final IPlayerAttribute keyIn, final EntityAttributeModifier modifierIn);
}
