package com.github.clevernucleus.playerex.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttributeInstance;
import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public final class PlayerDataManager implements PlayerData, AutoSyncedComponent {
	private static final List<BiFunction<PlayerData, LivingEntity, Double>> REFUND_CONDITIONS = new ArrayList<BiFunction<PlayerData, LivingEntity, Double>>();
	private final PlayerEntity player;
	private final Map<Identifier, Double> data;
	private int refundPoints, skillPoints;
	
	public PlayerDataManager(PlayerEntity player) {
		this.player = player;
		this.data = new HashMap<Identifier, Double>();
	}
	
	public static void addRefundCondition(final BiFunction<PlayerData, LivingEntity, Double> condition) {
		REFUND_CONDITIONS.add(condition);
	}
	
	private UUID uuid(final Identifier registryKey) {
		return PlayerEx.MANAGER.modifiers.getOrDefault(registryKey, (UUID)null);
	}
	
	private Optional<Identifier> tryRemove(final EntityAttribute attributeIn) {
		Identifier identifier = Registry.ATTRIBUTE.getId(attributeIn);
		
		if(identifier == null) return Optional.empty();
		
		AttributeContainer container = this.player.getAttributes();
		EntityAttributeInstance instance = container.getCustomInstance(attributeIn);
		
		if(instance == null) return Optional.empty();
		
		UUID uuid = this.uuid(identifier);
		
		if(instance.getModifier(uuid) != null) {
			instance.removeModifier(uuid);
		}
		
		this.data.remove(identifier);
		
		return Optional.of(identifier);
	}
	
	private boolean trySet(final Identifier registryKey, final double valueIn) {
		EntityAttribute attribute = Registry.ATTRIBUTE.get(registryKey);
		AttributeContainer container = this.player.getAttributes();
		EntityAttributeInstance instance = container.getCustomInstance(attribute);
		UUID uuid = this.uuid(registryKey);
		
		if(instance == null || uuid == null) return false;
		
		if(instance.getModifier(uuid) == null) {
			EntityAttributeModifier modifier = new EntityAttributeModifier(uuid, "PlayerData Attribute", valueIn, EntityAttributeModifier.Operation.ADDITION);
			instance.addPersistentModifier(modifier);
		} else {
			((IEntityAttributeInstance)instance).updateModifier(uuid, valueIn);
		}
		
		this.data.put(registryKey, valueIn);
		
		return true;
	}
	
	@Override
	public double get(final EntityAttribute attributeIn) {
		Identifier identifier = Registry.ATTRIBUTE.getId(attributeIn);
		
		if(identifier == null) return 0.0D;
		
		return this.data.getOrDefault(identifier, 0.0D);
	}
	
	@Override
	public void set(final EntityAttribute attributeIn, final double valueIn) {
		double value = valueIn;//attributeIn.clamp(valueIn);
		
		Identifier registryKey = Registry.ATTRIBUTE.getId(attributeIn);
		if(!this.trySet(registryKey, value)) return;
		
		ExAPI.INSTANCE.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			NbtCompound entry = new NbtCompound();
			entry.putString("Key", registryKey.toString());
			entry.putDouble("Value", value);
			tag.put("Set", entry);
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public void add(final EntityAttribute attributeIn, final double valueIn) {
		final double value = this.get(attributeIn);
		this.set(attributeIn, value + valueIn);
	}
	
	@Override
	public void remove(final EntityAttribute attributeIn) {
		this.tryRemove(attributeIn).ifPresent(identifier -> {
			ExAPI.INSTANCE.sync(this.player, (buf, player) -> {
				NbtCompound tag = new NbtCompound();
				tag.putString("Remove", identifier.toString());
				buf.writeNbt(tag);
			});
		});
	}
	
	@Override
	public void reset() {
		NbtList list = new NbtList();
		
		for(Iterator<Identifier> iterator = this.data.keySet().iterator(); iterator.hasNext();) {
			Identifier identifier = iterator.next();
			EntityAttribute attribute = Registry.ATTRIBUTE.get(identifier);
			
			list.add(NbtString.of(identifier.toString()));
			this.tryRemove(attribute);
		}
		
		ExAPI.INSTANCE.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.put("Reset", list);
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public void addSkillPoints(final int pointsIn) {
		this.skillPoints += pointsIn;
		
		ExAPI.INSTANCE.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.putInt("SkillPoints", this.skillPoints);
		});
	}
	
	@Override
	public int addRefundPoints(final int pointsIn) {
		final int previous = this.refundPoints;
		double maxRefundPt = 0.0D;
		
		for(BiFunction<PlayerData, LivingEntity, Double> condition : REFUND_CONDITIONS) {
			maxRefundPt += condition.apply(this, this.player);
		}
		
		double refund = MathHelper.clamp((double)(this.refundPoints + pointsIn), 0.0D, maxRefundPt);
		this.refundPoints = Math.round((float)refund);
		
		ExAPI.INSTANCE.sync(this.player, (buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.putInt("RefundPoints", this.refundPoints);
		});
		
		return this.refundPoints - previous;
	}
	
	@Override
	public int skillPoints() {
		return this.skillPoints;
	}
	
	@Override
	public int refundPoints() {
		return this.refundPoints;
	}
	
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player == this.player;
	}
	
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
		
		if(tag == null) return;
		
		if(tag.contains("Set")) {
			NbtCompound entry = tag.getCompound("Set");
			Identifier identifier = new Identifier(entry.getString("Key"));
			double value = entry.getDouble("Value");
			this.data.put(identifier, value);
		}
		
		if(tag.contains("Remove")) {
			Identifier identifier = new Identifier(tag.getString("Remove"));
			this.data.remove(identifier);
		}
		
		if(tag.contains("Reset")) {
			NbtList list = tag.getList("Reset", NbtType.STRING);
			
			for(int i = 0; i < list.size(); i++) {
				Identifier identifier = new Identifier(list.getString(i));
				this.data.remove(identifier);
			}
			
			this.refundPoints = 0;
			this.skillPoints = 0;
		}
		
		if(tag.contains("Modifiers")) {
			this.readFromNbt(tag);
		}
		
		if(tag.contains("RefundPoints")) {
			this.refundPoints = tag.getInt("RefundPoints");
		}
		
		if(tag.contains("SkillPoints")) {
			this.skillPoints = tag.getInt("SkillPoints");
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList modifiers = tag.getList("Modifiers", NbtType.COMPOUND);
		
		for(int i = 0; i < modifiers.size(); i++) {
			NbtCompound entry = modifiers.getCompound(i);
			Identifier key = new Identifier(entry.getString("Key"));
			double value = entry.getDouble("Value");
			this.trySet(key, value);
		}
		
		this.refundPoints = tag.getInt("RefundPoints");
		this.skillPoints = tag.getInt("SkillPoints");
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList modifiers = new NbtList();
		
		for(Identifier identifier : this.data.keySet()) {
			NbtCompound entry = new NbtCompound();
			double value = this.data.get(identifier);
			entry.putString("Key", identifier.toString());
			entry.putDouble("Value", value);
			modifiers.add(entry);
		}
		
		tag.put("Modifiers", modifiers);
		tag.putInt("RefundPoints", this.refundPoints);
		tag.putInt("SkillPoints", this.skillPoints);
	}
}
