package github.clevernucleus.playerex.common.init.container;

import github.clevernucleus.playerex.common.PlayerEx;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Object wrapper used as an identifier and getter for the PlayerElementsContainer.
 */
public class PlayerElementsContainerProvider implements INamedContainerProvider {
	
	@Override
	public Container createMenu(int par0, PlayerInventory par1, PlayerEntity par2) {
		return new PlayerElementsContainer(par0, par1);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(PlayerEx.MODID + ".player_elements");
	}
}
