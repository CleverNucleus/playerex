package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	//protected AttributeContainer playerex$Attributes = new AttributeContainer(DefaultAttributeRegistry.get(EntityType.PLAYER));
	
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
			PlayerEntity player = (PlayerEntity)living;
			AttributeDataManager data = (AttributeDataManager)ExAPI.DATA.get(player);
			info.setReturnValue(data.container());
		}
	}
	/*
	protected EntityAttribute getOrDefault(IPlayerAttribute keyIn) {
		Identifier registryKey = keyIn.registryKey();
		EntityAttribute value = Registry.ATTRIBUTE.get(registryKey);
		
		if(value == null) {
			value = Registry.register(Registry.ATTRIBUTE, registryKey, (new ClampedEntityAttribute(keyIn.translationKey(), keyIn.defaultValue(), keyIn.minValue(), keyIn.maxValue())).setTracked(true));
		} else {
			value.setTracked(true);
		}
		
		return ((IClampedEntityAttribute)value).withLimits(keyIn.minValue(), keyIn.maxValue());
	}
	
	//TODO this should run every time a player joins the server and every time a datapack is reloaded
	@Override
	public void reloadAttributes() {
		// A packet to the client should be sent updating our ExAPI attributes registry BEFORE this runs
		// TODO this whole method runs on the server AND client
		
		// BIG TODO :: remove the set(() -> attribute) thing all together and just have the get() method refer to Registry.ATTRIBUTE.get(id)
		
		// ALSO TODO :: scrap the common/client divide with Gson PlayerAttribute/AttributeFunction; test if we can just use those objects normally (pretty sure we can)
		
		
		
		//System.out.println("Fired Check");
		
		LivingEntity living = (LivingEntity)(Object)this;
		
		if(!(living instanceof PlayerEntity)) return;
		
		//System.out.println("Player Check");
		
		PlayerEntity player = (PlayerEntity)living;
		
		boolean isClient = player.world.isClient;
		String side = isClient ? "Client" : "Server";
		
		Map<EntityAttribute, EntityAttributeInstance> originalAttributes = new HashMap<EntityAttribute, EntityAttributeInstance>();
		
		Registry.ATTRIBUTE.forEach(attribute -> {
			EntityAttributeInstance instance = player.getAttributeInstance(attribute);
			
			if(instance != null) {
				originalAttributes.put(attribute, instance);
			}
		});
		
		player.sendMessage(new LiteralText(side + ": Pre-Refresh OriginalAttributesSize: " + originalAttributes.size()), false);
		//System.out.println(side + ": Pre-Refresh OriginalAttributesSize: " + originalAttributes.size());
		
		DefaultAttributeContainer.Builder builder = DefaultAttributeContainer.builder();
		Set<EntityAttribute> newAttributes = new HashSet<EntityAttribute>();
		
		for(Map.Entry<Identifier, IPlayerAttribute> entry : ExAPI.REGISTRY.get().attributes().entrySet()) {
			IPlayerAttribute attributeKey = entry.getValue();
			EntityAttribute attribute = getOrDefault(attributeKey);
			
			builder.add(attribute, attributeKey.defaultValue());
			newAttributes.add(attribute);
		}
		
		player.sendMessage(new LiteralText(side + ": New Container size: " + newAttributes.size()), false);
		
		AttributeContainer container = new AttributeContainer(builder.build());
		
		//// --------------- Maybe this section is not needed on the client as the server syncs it's instances (?)
		
		if(!isClient) {
			for(Map.Entry<EntityAttribute, EntityAttributeInstance> entry : originalAttributes.entrySet()) {
				EntityAttribute attribute = entry.getKey();
				EntityAttributeInstance instance = entry.getValue();
				Identifier key = Registry.ATTRIBUTE.getId(attribute);
				
				if(key != null) {
					container.getCustomInstance(attribute).setFrom(instance);
				}
			}
		}
		
		///// -------------
		
		PlayerAttribute p = (PlayerAttribute)PlayerAttributes.CONSTITUTION.get();
		
		player.sendMessage(new LiteralText(side + ": New Container has CON: " + container.hasAttribute(p.get())), false);
		//System.out.println(side + ": New Container has CON: " + container.hasAttribute(p.get()));
		
		playerex$Attributes = container;////// ----- this doesn't need to happen on respawn; only on first join
	}*/
}
