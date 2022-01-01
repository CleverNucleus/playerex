package com.github.clevernucleus.playerex;

import com.github.clevernucleus.dataattributes.api.event.EntityAttributeModifiedEvents;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.api.damage.DamageModificationRegistry;
import com.github.clevernucleus.playerex.api.event.LivingEntityEvents;
import com.github.clevernucleus.playerex.api.event.PlayerEntityEvents;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.handler.DamageHandler;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.handler.ExScreenHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.ModifierJsonManager;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PlayerEx implements ModInitializer {
	public static final String VERSION = FabricLoader.getInstance().getModContainer(ExAPI.MODID).get().getMetadata().getVersion().getFriendlyString();
	public static final Identifier HANDSHAKE = new Identifier(ExAPI.MODID, "handshake");
	public static final ModifierJsonManager MANAGER = new ModifierJsonManager();
	public static final ScreenHandlerType<ExScreenHandler> EX_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier(ExAPI.MODID, "ex_screen"), ExScreenHandler::new);
	
	@Override
	public void onInitialize() {
		AutoConfig.register(ConfigImpl.class, GsonConfigSerializer::new);
		
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		ServerLifecycleEvents.SERVER_STARTING.register(EventHandler::serverStarting);
		ServerLoginConnectionEvents.QUERY_START.register(NetworkHandler::loginQueryStart);
		ServerLoginNetworking.registerGlobalReceiver(HANDSHAKE, NetworkHandler::loginQueryResponse);
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.SCREEN, NetworkHandler::switchScreen);
		ServerPlayerEvents.COPY_FROM.register(EventHandler::copyFrom);
		
		EntityAttributeModifiedEvents.CLAMPED.register(EventHandler::clamp);
		
		LivingEntityEvents.TICK.register(EventHandler::tick);
		LivingEntityEvents.DAMAGE.register(EventHandler::onDamage);
		LivingEntityEvents.INCOMING_HEAL.register(EventHandler::heal);
		LivingEntityEvents.INCOMING_DAMAGE.register(EventHandler::damageModified);
		PlayerEntityEvents.ATTACK_CRIT_BOOLEAN.register(EventHandler::attackCritChance);
		PlayerEntityEvents.ATTACK_CRIT_DAMAGE.register(EventHandler::attackCritDamage);
		
		DamageHandler.STORE.forEach(DamageModificationRegistry::register);
		
		Registry.register(Registry.ITEM, new Identifier("playerex:test"), new Item(new Item.Settings()) {
			
			@Override
			public net.minecraft.util.TypedActionResult<net.minecraft.item.ItemStack> use(net.minecraft.world.World world, net.minecraft.entity.player.PlayerEntity user, net.minecraft.util.Hand hand) {
				boolean i = user.isSneaking();
				
				EntityAttribute attribute = ExAPI.LEVEL.get();
				PlayerData data = ExAPI.INSTANCE.get(user);
				//String side = world.isClient ? "CLIENT" : "SERVER";
				
				if(i) {
					//user.sendMessage(new LiteralText("Data -> " + side + ": " + data.get(attribute)), false);
					//user.sendMessage(new LiteralText("Game -> " + side + ": " + user.getAttributeValue(attribute)), false);
					if(!world.isClient) {
						data.add(attribute, 1.0D);
					}
				} else {
					if(!world.isClient) {
						data.add(attribute, -1.0D);
					}
				}
				
				return super.use(world, user, hand);
			}
		});
	}
}
