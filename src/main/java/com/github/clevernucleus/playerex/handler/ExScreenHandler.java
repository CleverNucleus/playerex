package com.github.clevernucleus.playerex.handler;

import com.github.clevernucleus.playerex.PlayerEx;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class ExScreenHandler extends ScreenHandler {
	public final int pageId;
	
	public ExScreenHandler(int syncId, PlayerInventory inv, int pageId) {
		super(PlayerEx.EX_SCREEN, syncId);
		this.pageId = pageId;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
