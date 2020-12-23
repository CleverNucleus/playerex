package git.clevernucleus.playerex.network;

import java.util.function.Supplier;

import git.clevernucleus.playerex.container.PlayerElementsContainerProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Network packet responsible for switching between the player's default inventory and the player elements inventory.
 */
public class SwitchScreens {
	private boolean playerInventory;
	
	public SwitchScreens() {}
	
	/**
	 * Constructor.
	 * @param par0 Set true to switch to the player's default inventory, false to switch to the player's elements inventory.
	 */
	public SwitchScreens(final boolean par0) {
		this.playerInventory = par0;
	}
	
	/**
	 * Receives a packet and writes the contents to the input buffer.
	 * @param par0 Input packet.
	 * @param par1 Input buffer
	 */
	public static void encode(SwitchScreens par0, PacketBuffer par1) {
		par1.writeBoolean(par0.playerInventory);
	}
	
	/**
	 * Receives an input buffer and retrieves the contents to write to a new packet instance.
	 * @param par0 Input buffer.
	 * @return A new Packet instance.
	 */
	public static SwitchScreens decode(PacketBuffer par0) {
		return new SwitchScreens(par0.readBoolean());
	}
	
	/**
	 * Handles the packet's functionality.
	 * @param par0 Packet input.
	 * @param par1 Network context.
	 */
	public static void handle(SwitchScreens par0, Supplier<NetworkEvent.Context> par1) {
		par1.get().enqueueWork(() -> {
			if(par0.playerInventory) {
				ServerPlayerEntity var0 = par1.get().getSender();
				
				if(var0 != null) {
					var0.openContainer.onContainerClosed(var0);
					var0.openContainer = var0.container;
				}
			} else {
				par1.get().getSender().closeContainer();
				NetworkHooks.openGui(par1.get().getSender(), new PlayerElementsContainerProvider());
			}
		});
		
		par1.get().setPacketHandled(true);
	}
}
