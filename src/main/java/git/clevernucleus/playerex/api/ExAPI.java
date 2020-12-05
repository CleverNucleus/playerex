package git.clevernucleus.playerex.api;

import git.clevernucleus.playerex.api.element.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

/**
 * General API utilities.
 */
public class ExAPI {
	
	/** The modid used to identify playerex. */
	public static final String MODID = "playerex";
	
	/** Capability access. */
	@CapabilityInject(IPlayerElements.class)
	public static final Capability<IPlayerElements> PLAYER_ELEMENTS = null;
	
	/**
	 * @param par0 Player instance.
	 * @return The player elements capability instance.
	 */
	public static LazyOptional<IPlayerElements> playerElements(PlayerEntity par0) {
		return par0.getCapability(PLAYER_ELEMENTS, null);
	}
}
