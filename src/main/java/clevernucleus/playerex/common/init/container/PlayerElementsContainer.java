package clevernucleus.playerex.common.init.container;

import clevernucleus.playerex.common.init.Registry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

/**
 * Container for holding player elements.
 */
public class PlayerElementsContainer extends Container {
	public PlayerElementsContainer(final int par0, final PlayerInventory par1) {
		super(Registry.ELEMENTS_CONTAINER, par0);
	}
	
	@Override
	public boolean canInteractWith(final PlayerEntity par0) {
		return true;
	}
}
