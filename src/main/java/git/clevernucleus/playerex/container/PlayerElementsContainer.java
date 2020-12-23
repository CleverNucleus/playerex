package git.clevernucleus.playerex.container;

import git.clevernucleus.playerex.event.RegistryEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

/**
 * Container for holding player elements.
 */
public class PlayerElementsContainer extends Container {
	public PlayerElementsContainer(final int par0, final PlayerInventory par1) {
		super(RegistryEvents.ELEMENTS_CONTAINER, par0);
	}
	
	@Override
	public boolean canInteractWith(final PlayerEntity par0) {
		return true;
	}
}
