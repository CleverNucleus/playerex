package clevernucleus.playerex.common.init.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

/**
 * PlayerElements capability access interface.
 */
public interface IPlayerElements {
	
	/**
	 * Writes capability data to a tag.
	 * @return A tag with the capability data.
	 */
	CompoundNBT write();
	
	/**
	 * Reads the data from the input tag to the capability.
	 * @param par0 Input tag.
	 */
	void read(CompoundNBT par0);
	
	/**
	 * Syncs the input player's capability with the client.
	 * @param par0 PlayerEntity input.
	 */
	void sync(PlayerEntity par0);
}
