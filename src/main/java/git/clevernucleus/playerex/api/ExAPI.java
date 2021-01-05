package git.clevernucleus.playerex.api;

import git.clevernucleus.playerex.api.attribute.IPlayerAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class ExAPI {
	public static final String MODID = "playerex";
	
	/** Capability access. */
	@CapabilityInject(IPlayerAttributes.class)
	public static final Capability<IPlayerAttributes> PLAYER_ATTRIBUTES = null;
	
	/**
	 * @param par0 Player instance.
	 * @return The player attributes capability instance.
	 */
	public static LazyOptional<IPlayerAttributes> playerAttributes(PlayerEntity par0) {
		return par0.getCapability(PLAYER_ATTRIBUTES, null);
	}
}
