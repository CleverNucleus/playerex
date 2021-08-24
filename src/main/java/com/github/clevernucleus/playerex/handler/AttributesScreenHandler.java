package com.github.clevernucleus.playerex.handler;

import com.github.clevernucleus.playerex.PlayerEx;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class AttributesScreenHandler extends ScreenHandler {
	public AttributesScreenHandler(int syncId, PlayerInventory inv) {
		super(PlayerEx.ATTRIBUTES_SCREEN, syncId);
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
