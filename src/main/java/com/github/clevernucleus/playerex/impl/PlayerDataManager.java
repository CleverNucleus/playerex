package com.github.clevernucleus.playerex.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttributeInstance;
import com.github.clevernucleus.playerex.api.EntityAttributeSupplier;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.sync.ComponentPacketWriter;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.registry.Registry;

public final class PlayerDataManager implements PlayerData, AutoSyncedComponent {
	private static final String KEY_SET = "Set", KEY_REMOVE = "Remove", KEY_RESET = "Reset", KEY_MODIFIERS = "Modifiers", KEY_REFUND_POINTS = "RefundPoints", KEY_SKILL_POINTS = "SkillPoints";
	private final PlayerEntity player;
	private final Map<Identifier, Double> data;
	private int refundPoints, skillPoints;
	public boolean hasNotifiedLevelUp;
	
	public PlayerDataManager(final PlayerEntity player) {
		this.player = player;
		this.data = new HashMap<Identifier, Double>();
		this.hasNotifiedLevelUp = false;
	}
	
	private void sync(ComponentPacketWriter packet) {
		if(this.player.getWorld().isClient) return;
		ExAPI.PLAYER_DATA.sync(this.player, packet);
	}
	
	private void readModifiersFromNbt(NbtCompound tag, BiFunction<Identifier, Double, Object> function) {
		NbtList modifiers = tag.getList(KEY_MODIFIERS, NbtType.COMPOUND);
		
		for(int i = 0; i < modifiers.size(); i++) {
			NbtCompound entry = modifiers.getCompound(i);
			Identifier identifier = new Identifier(entry.getString("Key"));
			final double value = entry.getDouble("Value");
			function.apply(identifier, value);
		}
	}
	
	private boolean isValid(final Identifier registryKey, final Consumer<EntityAttributeInstance> ifPresent, final Consumer<EntityAttributeInstance> otherwise) {
		EntityAttribute attribute = Registries.ATTRIBUTE.get(registryKey);
		
		if(attribute == null) return false;
		AttributeContainer container = this.player.getAttributes();
		EntityAttributeInstance instance = container.getCustomInstance(attribute);
		
		if(instance == null) return false;
		if(instance.getModifier(ExAPI.PLAYEREX_MODIFIER_ID) != null) {
			ifPresent.accept(instance);
		} else {
			otherwise.accept(instance);
		}
		
		return true;
	}
	
	private boolean trySet(final Identifier registryKey, final double valueIn) {
		if(!this.isValid(registryKey, instance -> ((IEntityAttributeInstance)instance).updateModifier(ExAPI.PLAYEREX_MODIFIER_ID, valueIn), instance -> {
			EntityAttributeModifier modifier = new EntityAttributeModifier(ExAPI.PLAYEREX_MODIFIER_ID, "PlayerEx Attribute", valueIn, EntityAttributeModifier.Operation.ADDITION);
			instance.addPersistentModifier(modifier);
		})) return false;
		
		this.data.put(registryKey, valueIn);
		return true;
	}
	
	private boolean tryRemove(final Identifier registryKey, final Consumer<Identifier> consumer) {
		if(!this.isValid(registryKey, instance -> instance.removeModifier(ExAPI.PLAYEREX_MODIFIER_ID), instance -> {})) return false;
		consumer.accept(registryKey);
		return true;
	}
	
	@Override
	public double get(final EntityAttributeSupplier attributeIn) {
		if(attributeIn.get() == null) return 0.0D;
		return this.data.getOrDefault(attributeIn.getId(), 0.0D);
	}
	
	@Override
	public void set(final EntityAttributeSupplier attributeIn, final double valueIn) {
		EntityAttribute attribute = attributeIn.get();
		
		if(attribute == null) return;
		Identifier identifier = attributeIn.getId();
		double value = attribute.clamp(valueIn);
		
		if(!this.trySet(identifier, value)) return;
		this.sync((buf, player) -> {
			NbtCompound tag = new NbtCompound();
			NbtCompound entry = new NbtCompound();
			entry.putString("Key", identifier.toString());
			entry.putDouble("Value", value);
			tag.put(KEY_SET, entry);
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public void add(final EntityAttributeSupplier attributeIn, final double valueIn) {
		final double value = this.get(attributeIn);
		this.set(attributeIn, value + valueIn);
	}
	
	@Override
	public void remove(final EntityAttributeSupplier attributeIn) {
		Identifier identifier = attributeIn.getId();
		
		if(!this.tryRemove(identifier, this.data::remove)) return;
		this.sync((buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.putString(KEY_REMOVE, identifier.toString());
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public void reset() {
		NbtList list = new NbtList();
		
		for(Iterator<Identifier> iterator = this.data.keySet().iterator(); iterator.hasNext();) {
			Identifier identifier = iterator.next();
			
			if(!this.tryRemove(identifier, id -> iterator.remove())) continue;
			list.add(NbtString.of(identifier.toString()));
		}
		
		this.refundPoints = 0;
		this.skillPoints = 0;
		this.sync((buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.put(KEY_RESET, list);
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public void addSkillPoints(final int pointsIn) {
		this.skillPoints += pointsIn;
		this.sync((buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.putInt(KEY_SKILL_POINTS, this.skillPoints);
			buf.writeNbt(tag);
		});
	}
	
	@Override
	public int addRefundPoints(final int pointsIn) {
		final int previous = this.refundPoints;
		double maxRefundPt = 0.0D;
		
		for(var refundCondition : RefundConditionImpl.get()) {
			maxRefundPt += refundCondition.apply(this, this.player);
		}
		
		double refund = MathHelper.clamp((double)(this.refundPoints + pointsIn), 0.0D, maxRefundPt);
		this.refundPoints = Math.round((float)refund);
		this.sync((buf, player) -> {
			NbtCompound tag = new NbtCompound();
			tag.putInt(KEY_REFUND_POINTS, this.refundPoints);
			buf.writeNbt(tag);
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
		if(tag.contains(KEY_SET)) {
			NbtCompound entry = tag.getCompound(KEY_SET);
			Identifier identifier = new Identifier(entry.getString("Key"));
			final double value = entry.getDouble("Value");
			this.data.put(identifier, value);
		}
		
		if(tag.contains(KEY_REMOVE)) {
			Identifier identifier = new Identifier(tag.getString(KEY_REMOVE));
			this.data.remove(identifier);
		}
		
		if(tag.contains(KEY_RESET)) {
			NbtList list = tag.getList(KEY_RESET, NbtType.STRING);
			
			for(int i = 0; i < list.size(); i++) {
				Identifier identifier = new Identifier(list.getString(i));
				this.data.remove(identifier);
			}
			
			this.refundPoints = 0;
			this.skillPoints = 0;
			this.hasNotifiedLevelUp = false;
		}
		
		if(tag.contains(KEY_MODIFIERS)) {
			this.readModifiersFromNbt(tag, this.data::put);
		}
		
		if(tag.contains(KEY_REFUND_POINTS)) {
			this.refundPoints = tag.getInt(KEY_REFUND_POINTS);
		}
		
		if(tag.contains(KEY_SKILL_POINTS)) {
			this.skillPoints = tag.getInt(KEY_SKILL_POINTS);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.readModifiersFromNbt(tag, this::trySet);
		this.refundPoints = tag.getInt(KEY_REFUND_POINTS);
		this.skillPoints = tag.getInt(KEY_SKILL_POINTS);
		this.hasNotifiedLevelUp = tag.getBoolean("NotifiedLevelUp");
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
		
		tag.put(KEY_MODIFIERS, modifiers);
		tag.putInt(KEY_REFUND_POINTS, this.refundPoints);
		tag.putInt(KEY_SKILL_POINTS, this.skillPoints);
		tag.putBoolean("NotifiedLevelUp", this.hasNotifiedLevelUp);
	}
}
