package clevernucleus.playerex.common.init.capability;

import clevernucleus.playerex.common.init.Registry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkDirection;

public class PlayerElements implements IPlayerElements {
	private CompoundNBT tag;
	
	public PlayerElements() {
		this.tag = new CompoundNBT();
	}
	
	@Override
	public CompoundNBT write() {
		return this.tag;
	}
	
	@Override
	public void read(CompoundNBT par0) {
		this.tag = par0;
	}
	
	@Override
	public void sync(PlayerEntity par0) {
		if(par0.world.isRemote) return;
		
		Registry.NETWORK.sendTo(new SyncPlayerElements(this.tag), ((ServerPlayerEntity)par0).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}
}
