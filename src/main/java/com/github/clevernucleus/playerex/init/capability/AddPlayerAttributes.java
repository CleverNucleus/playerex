package com.github.clevernucleus.playerex.init.capability;

import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class AddPlayerAttributes {
	private CompoundNBT tag;
	
	public AddPlayerAttributes() {}
	
	
	@SafeVarargs
	public AddPlayerAttributes(Pair<IPlayerAttribute, Float> ... par0) {
		this.tag = new CompoundNBT();
		ListNBT var0 = new ListNBT();
		
		for(Pair<IPlayerAttribute, Float> var : par0) {
			CompoundNBT var1 = new CompoundNBT();
			String var2 = var.getFirst().toString();
			float var3 = var.getSecond().floatValue();
			
			var1.putString("Name", var2);
			var1.putFloat("Value", var3);
			var0.add(var1);
		}
		
		this.tag.put("Attributes", var0);
	}
	
	/**
	 * Constructor.
	 * @param par0 Compound tag to send.
	 */
	public AddPlayerAttributes(final @Nonnull CompoundNBT par0) {
		this.tag = par0;
	}
	
	/**
	 * Receives a packet and writes the contents to the input buffer.
	 * @param par0 Input packet.
	 * @param par1 Input buffer
	 */
	public static void encode(AddPlayerAttributes par0, PacketBuffer par1) {
		par1.writeCompoundTag(par0.tag);
	}
	
	/**
	 * Receives an input buffer and retrieves the contents to write to a new packet instance.
	 * @param par0 Input buffer.
	 * @return A new Packet instance.
	 */
	public static AddPlayerAttributes decode(PacketBuffer par0) {
		return new AddPlayerAttributes(par0.readCompoundTag());
	}
	
	/**
	 * Handles the packet's functionality.
	 * @param par0 Packet input.
	 * @param par1 Network context.
	 */
	public static void handle(AddPlayerAttributes par0, Supplier<NetworkEvent.Context> par1) {
		par1.get().enqueueWork(() -> {
			ServerPlayerEntity var0 = par1.get().getSender();
			
			if(var0 != null && par0.tag != null) {
				ListNBT var1 = par0.tag.getList("Attributes", 10);
				Map<IPlayerAttribute, Float> var2 = Maps.newHashMap();
				
				for(INBT var : var1) {
					CompoundNBT var3 = (CompoundNBT)var;
					IPlayerAttribute var4 = PlayerAttributes.fromRegistryName(var3.getString("Name"));
					
					var2.put(var4, var3.getFloat("Value"));
				}
				
				ExAPI.playerAttributes(var0).ifPresent(var -> var2.forEach((var3, var4) -> var.add(var0, var3, var4.doubleValue())));
			}
		});
		
		par1.get().setPacketHandled(true);
	}
}
