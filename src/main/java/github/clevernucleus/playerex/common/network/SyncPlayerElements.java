package github.clevernucleus.playerex.common.network;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import github.clevernucleus.playerex.api.ElementRegistry;
import github.clevernucleus.playerex.common.PlayerEx;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Network packet responsible for syncing server entity display data to the client.
 */
public class SyncPlayerElements {
	private CompoundNBT tag;
	
	public SyncPlayerElements() {}
	
	/**
	 * Constructor.
	 * @param par0 Compound tag to send.
	 */
	public SyncPlayerElements(final @Nonnull CompoundNBT par0) {
		this.tag = par0;
	}
	
	/**
	 * Receives a packet and writes the contents to the input buffer.
	 * @param par0 Input packet.
	 * @param par1 Input buffer
	 */
	public static void encode(SyncPlayerElements par0, PacketBuffer par1) {
		par1.writeCompoundTag(par0.tag);
	}
	
	/**
	 * Receives an input buffer and retrieves the contents to write to a new packet instance.
	 * @param par0 Input buffer.
	 * @return A new Packet instance.
	 */
	public static SyncPlayerElements decode(PacketBuffer par0) {
		return new SyncPlayerElements(par0.readCompoundTag());
	}
	
	/**
	 * Handles the packet's functionality.
	 * @param par0 Packet input.
	 * @param par1 Network context.
	 */
	public static void handle(SyncPlayerElements par0, Supplier<NetworkEvent.Context> par1) {
		if(par1.get().getDirection().getReceptionSide().isClient()) {
			par1.get().enqueueWork(() -> {
				PlayerEntity var0 = PlayerEx.PROXY.clientPlayer();
				
				if(par0.tag == null || var0 == null) return;
				
				var0.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(par0.tag.getDouble("generic.knockbackResistance"));
				var0.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(par0.tag.getDouble("generic.attackDamage"));
				
				ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> var.read(par0.tag.getCompound("Elements")));
			});
			
			par1.get().setPacketHandled(true);
		}
	}
}
