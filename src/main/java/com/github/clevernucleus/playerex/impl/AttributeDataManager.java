package com.github.clevernucleus.playerex.impl;

import com.github.clevernucleus.playerex.api.attribute.AttributeData;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class AttributeDataManager implements AttributeData, AutoSyncedComponent {
	private PlayerEntity player;
	
	public AttributeDataManager(PlayerEntity player) {
		this.player = player;
	}
	
	private EntityAttribute from(final Identifier id) {
		return Registry.ATTRIBUTE.get(id);
	}
	
	@Override
	public void readFromNbt(CompoundTag tag) {
		
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		
	}
}
