package com.github.clevernucleus.playerex.mixin;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.impl.attribute.IAttributeContainer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements IAttributeContainer {
	protected AttributeContainer playerex$Attributes = new AttributeContainer(DefaultAttributeRegistry.get(EntityType.PLAYER));
	
	@Inject(method = "heal", at = @At("HEAD"))
	private void onHeal(float amount, CallbackInfo info) {
		EventHandler.onHeal((LivingEntity)(Object)this, amount);
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		boolean result = EventHandler.onDamage((LivingEntity)(Object)this, source, amount);
		
		if(result) {
			info.setReturnValue(false);
		}
	}
	
	@Inject(method = "getAttributes", require = 1, allow = 1, at = @At("RETURN"), cancellable = true)
	private void attributes(CallbackInfoReturnable<AttributeContainer> info) {
		LivingEntity living = (LivingEntity)(Object)this;
		
		if(living instanceof PlayerEntity) {
			info.setReturnValue(playerex$Attributes);
		}
	}
	
	//TODO this should run every time a player joins the server and every time a datapack is reloaded
	@Override
	public void reloadAttributes() {
		// A packet to the client should be sent updating our ExAPI attributes registry BEFORE this runs
		// TODO this whole method runs on the server AND client
		
		// BIG TODO :: remove the set(() -> attribute) thing all together and just have the get() method refer to Registry.ATTRIBUTE.get(id)
		
		// ALSO TODO :: scrap the common/client divide with Gson PlayerAttribute/AttributeFunction; test if we can just use those objects normally (pretty sure we can)
		
		LivingEntity living = (LivingEntity)(Object)this;
		
		if(!(living instanceof PlayerEntity)) return;
		
		PlayerEntity player = (PlayerEntity)living;
		Map<EntityAttribute, EntityAttributeInstance> originalAttributes = new HashMap<EntityAttribute, EntityAttributeInstance>();
		
		Registry.ATTRIBUTE.forEach(attribute -> {
			EntityAttributeInstance instance = player.getAttributeInstance(attribute);
			
			if(instance != null) {
				originalAttributes.put(attribute, instance);
			}
		});
		
		Map<Identifier, IPlayerAttribute> cachedMap = null;// this should be our registry
		
		DefaultAttributeContainer.Builder builder = DefaultAttributeContainer.builder();
		
		for(Map.Entry<Identifier, IPlayerAttribute> attributes : cachedMap.entrySet()) {
			// attributes#register >>>> this should register the attribute to Registry.ATTRIBUTES
			
			// add the attributes to the builder
		}
		
		AttributeContainer container = new AttributeContainer(builder.build());
		
		for(Map.Entry<EntityAttribute, EntityAttributeInstance> entry : originalAttributes.entrySet()) {
			EntityAttribute attribute = entry.getKey();
			EntityAttributeInstance instance = entry.getValue();
			Identifier key = Registry.ATTRIBUTE.getId(attribute);
			
			if(key != null) {
				container.getCustomInstance(attribute).setFrom(instance);
			}
		}
		
		playerex$Attributes = container;
	}
}
