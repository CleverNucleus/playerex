package com.github.clevernucleus.playerex;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ExConfigProvider;
import com.github.clevernucleus.playerex.api.ExRegistryProvider;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.event.PlayerAttributeModifiedEvent;
import com.github.clevernucleus.playerex.api.event.PlayerLevelUpEvent;
import com.github.clevernucleus.playerex.config.ConfigCache;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.handler.EventHandler;
import com.github.clevernucleus.playerex.impl.ExRegistryImpl;
import com.github.clevernucleus.playerex.impl.attribute.AttributeManager;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public final class PlayerEx implements ModInitializer {
	public static final ConfigCache CONFIG_CACHE = new ConfigCache();
	public static final Identifier LEVEL_UP_SOUND = new Identifier(ExAPI.MODID, "level_up");
	public static final Identifier SP_SPEND_SOUND = new Identifier(ExAPI.MODID, "sp_spend");
	public static final SoundEvent LEVEL_UP = new SoundEvent(LEVEL_UP_SOUND);
	public static final SoundEvent SP_SPEND = new SoundEvent(SP_SPEND_SOUND);
	
	@SuppressWarnings("deprecation")
	@Override
	public void onInitialize() {
		AutoConfig.register(ConfigImpl.class, GsonConfigSerializer::new);
		((ExConfigProvider)ExAPI.CONFIG).build(AutoConfig.getConfigHolder(ConfigImpl.class).get());
		((ExRegistryProvider)ExAPI.REGISTRY).init(new ExRegistryImpl());
		
		ExAPI.ATTRIBUTES.build();
		CONFIG_CACHE.build();
		
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new AttributeManager());
		
		Registry.register(Registry.SOUND_EVENT, LEVEL_UP_SOUND, LEVEL_UP);
		Registry.register(Registry.SOUND_EVENT, SP_SPEND_SOUND, SP_SPEND);
		
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(EventHandler::dataPackReload);
		ServerPlayConnectionEvents.JOIN.register(EventHandler::onPlayerJoin);
		ServerPlayerEvents.COPY_FROM.register(EventHandler::respawn);
		PlayerLevelUpEvent.LEVEL_UP.register(EventHandler::levelUp);
		PlayerAttributeModifiedEvent.MODIFIED.register(EventHandler::attributeModified);
		
		Registry.register(Registry.ITEM, new Identifier("playerex:test_item"), new Item(new Item.Settings()) {
			@Override
			public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
				int size = ExAPI.REGISTRY.get().attributes().size();
				
				if(hand == Hand.OFF_HAND) {
					AttributeData data = ExAPI.DATA.get(user);
					double value = data.get(ExAPI.ATTRIBUTES.get().maxHealth.get());
					
					if(world.isClient) {
						user.sendMessage(new LiteralText("Client Max Health: " + value), false);
					} else {
						user.sendMessage(new LiteralText("Server Max Health: " + value), false);
					}
				} else {
					if(world.isClient) {
						user.sendMessage(new LiteralText("Client: " + size), false);
					} else {
						user.sendMessage(new LiteralText("Server: " + size), false);
					}
				}
				
				
				return super.use(world, user, hand);
			}
		});
	}
}
