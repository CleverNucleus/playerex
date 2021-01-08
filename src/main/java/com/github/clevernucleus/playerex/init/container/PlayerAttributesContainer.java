package com.github.clevernucleus.playerex.init.container;

import com.github.clevernucleus.playerex.init.Registry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

/**
 * Container for holding player attributes.
 */
public class PlayerAttributesContainer extends Container {
	public PlayerAttributesContainer(final int par0, final PlayerInventory par1) {
		super(Registry.ATTRIBUTES_CONTAINER, par0);
	}
	
	@Override
	public boolean canInteractWith(final PlayerEntity par0) {
		return true;
	}
}
