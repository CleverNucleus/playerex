package github.clevernucleus.playerex.common.network;

import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import github.clevernucleus.playerex.api.ElementRegistry;
import github.clevernucleus.playerex.api.element.IElement;
import github.clevernucleus.playerex.common.event.CommonEvents;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class AddPlayerElement {
	private CompoundNBT tag;
	
	public AddPlayerElement() {}
	
	@SafeVarargs
	public AddPlayerElement(Pair<IElement, Double> ... par0) {
		this.tag = new CompoundNBT();
		ListNBT var0 = new ListNBT();
		
		for(Pair<IElement, Double> var : par0) {
			CompoundNBT var1 = new CompoundNBT();
			String var2 = var.getFirst().getRegistryName().toString();
			double var3 = var.getSecond().doubleValue();
			
			var1.putString("Name", var2);
			var1.putDouble("Value", var3);
			var0.add(var1);
		}
		
		this.tag.put("Elements", var0);
	}
	
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
				if(par0.tag.contains("Elements")) {
					ListNBT var1 = par0.tag.getList("Elements", 10);
					Map<IElement, Double> var2 = Maps.newHashMap();
					
					for(int var = 0; var < var1.size(); var++) {
						CompoundNBT var3 = var1.getCompound(var);
						ResourceLocation var4 = new ResourceLocation(var3.getString("Name"));
						IElement var5 = ElementRegistry.getElement(var4);
						
						var2.put(var5, var3.getDouble("Value"));
					}
					
					ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> var2.forEach((var3, var4) -> var3.add(var0, var, var4)));
					CommonEvents.syncTag(var0);
				}
			}
			
		});
		
		par1.get().setPacketHandled(true);
	}
}
