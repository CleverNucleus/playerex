package com.github.clevernucleus.playerex.util;

import java.util.UUID;

import net.minecraft.item.Item;

public final class ItemFieldAccess extends Item {
	private ItemFieldAccess() {
		super(new Item.Settings());
	}
	
	public static UUID attackDamageModifierId() {
		return ATTACK_DAMAGE_MODIFIER_ID;
	}
	
	public static UUID attackSpeedModifierId() {
		return ATTACK_SPEED_MODIFIER_ID;
	}
}
