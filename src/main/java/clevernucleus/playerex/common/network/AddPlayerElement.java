package clevernucleus.playerex.common.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.element.IElement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Network packet responsible for adding attribute data from the client to the server.
 */
public class AddPlayerElement {
	private CompoundNBT tag;
	
	public AddPlayerElement() {}
	
	/**
	 * Constructor.
	 * @param par0 Compound tag to send.
	 */
	public AddPlayerElement(final @Nonnull CompoundNBT par0) {
		this.tag = par0;
	}
	
	/**
	 * Receives a packet and writes the contents to the input buffer.
	 * @param par0 Input packet.
	 * @param par1 Input buffer
	 */
	public static void encode(AddPlayerElement par0, PacketBuffer par1) {
		par1.writeCompoundTag(par0.tag);
	}
	
	/**
	 * Receives an input buffer and retrieves the contents to write to a new packet instance.
	 * @param par0 Input buffer.
	 * @return A new Packet instance.
	 */
	public static AddPlayerElement decode(PacketBuffer par0) {
		return new AddPlayerElement(par0.readCompoundTag());
	}
	
	/**
	 * Handles the packet's functionality.
	 * @param par0 Packet input.
	 * @param par1 Network context.
	 */
	public static void handle(AddPlayerElement par0, Supplier<NetworkEvent.Context> par1) {
		par1.get().enqueueWork(() -> {
			ServerPlayerEntity var0 = par1.get().getSender();
			
			if(var0 != null && par0.tag != null) {
				if(par0.tag.contains("elements")) {
					ListNBT var1 = par0.tag.getList("elements", 10);
					Map<IElement, Double> var2 = new HashMap<IElement, Double>();
					
					for(int var = 0; var < var1.size(); var++) {
						CompoundNBT var3 = var1.getCompound(var);
						Optional<IElement> var4 = Registry.ELEMENT_FROM_ID.apply(var3.getString("Name"));
						var4.ifPresent(var5 -> {
							var2.put(var5, var3.getDouble("Value"));
						});
					}
					
					Registry.ELEMENTS.apply(var0).ifPresent(var -> {
						var2.forEach((var3, var4) -> {
							var.add(var0, var3, var4);
						});
						
						var.sync(var0);
					});
				}
			}
		});
		
		par1.get().setPacketHandled(true);
	}
}
