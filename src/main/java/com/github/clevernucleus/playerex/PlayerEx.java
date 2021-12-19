package com.github.clevernucleus.playerex;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.api.event.LivingEntityEvents;
import com.github.clevernucleus.playerex.api.event.PlayerEntityEvents;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.ModifierJsonManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PlayerEx implements ModInitializer {
	public static final String VERSION = FabricLoader.getInstance().getModContainer(ExAPI.MODID).get().getMetadata().getVersion().getFriendlyString();
	public static final Identifier HANDSHAKE = new Identifier(ExAPI.MODID, "handshake");
	public static final ModifierJsonManager MANAGER = new ModifierJsonManager();
	
	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		ServerLifecycleEvents.SERVER_STARTING.register(server -> MANAGER.load());
		ServerLoginConnectionEvents.QUERY_START.register(NetworkHandler::loginQueryStart);
		ServerLoginNetworking.registerGlobalReceiver(HANDSHAKE, NetworkHandler::loginQueryResponse);
		ServerPlayerEvents.COPY_FROM.register(EventHandler::copyFrom);
		
		LivingEntityEvents.TICK.register(EventHandler::tick);
		LivingEntityEvents.DAMAGE.register(EventHandler::onDamage);
		LivingEntityEvents.INCOMING_HEAL.register(EventHandler::heal);
		LivingEntityEvents.INCOMING_DAMAGE.register(EventHandler::damageModified);
		PlayerEntityEvents.ATTACK_CRIT_BOOLEAN.register(EventHandler::attackCritChance);
		PlayerEntityEvents.ATTACK_CRIT_DAMAGE.register(EventHandler::attackCritDamage);
		
		Registry.register(Registry.ITEM, new Identifier("playerex:test"), new Item(new Item.Settings()) {
			
			@Override
			public net.minecraft.util.TypedActionResult<net.minecraft.item.ItemStack> use(net.minecraft.world.World world, net.minecraft.entity.player.PlayerEntity user, net.minecraft.util.Hand hand) {
				boolean i = user.isSneaking();
				
				EntityAttribute attribute = ExAPI.HEALTH_REGENERATION.get();
				PlayerData data = ExAPI.INSTANCE.get(user);
				//String side = world.isClient ? "CLIENT" : "SERVER";
				
				if(i) {
					//user.sendMessage(new LiteralText("Data -> " + side + ": " + data.get(attribute)), false);
					//user.sendMessage(new LiteralText("Game -> " + side + ": " + user.getAttributeValue(attribute)), false);
					if(!world.isClient) {
						data.add(attribute, 0.001D);
					}
				} else {
					if(!world.isClient) {
						data.add(attribute, -0.001D);
					}
				}
				
				return super.use(world, user, hand);
			}
		});
	}
}
