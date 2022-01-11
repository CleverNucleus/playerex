package com.github.clevernucleus.playerex;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PlayerEx implements ModInitializer {
	public static final String VERSION = FabricLoader.getInstance().getModContainer(ExAPI.MODID).get().getMetadata().getVersion().getFriendlyString();
	public static final Identifier HANDSHAKE = new Identifier(ExAPI.MODID, "handshake");
	public static final ModifierJsonManager MANAGER = new ModifierJsonManager();
	public static final SoundEvent LEVEL_UP_SOUND = new SoundEvent(new Identifier(ExAPI.MODID, "level_up"));
	public static final SoundEvent SP_SPEND_SOUND = new SoundEvent(new Identifier(ExAPI.MODID, "sp_spend"));
	public static final ScreenHandlerType<ExScreenHandler> EX_SCREEN = ScreenHandlerRegistry.registerExtended(new Identifier(ExAPI.MODID, "ex_screen"), (syncId, inv, buf) -> new ExScreenHandler(syncId, inv, buf.readInt()));
	
	@Override
	public void onInitialize() {
		AutoConfig.register(ConfigImpl.class, GsonConfigSerializer::new);
		
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		ServerLifecycleEvents.SERVER_STARTING.register(EventHandler::serverStarting);
		ServerLoginConnectionEvents.QUERY_START.register(NetworkHandler::loginQueryStart);
		ServerLoginNetworking.registerGlobalReceiver(HANDSHAKE, NetworkHandler::loginQueryResponse);
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.SCREEN, NetworkHandler::switchScreen);
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.MODIFY, NetworkHandler::modifyAttributes);
		ServerPlayerEvents.COPY_FROM.register(EventHandler::copyFrom);
		
		EntityAttributeModifiedEvents.CLAMPED.register(EventHandler::clamp);
		
		LivingEntityEvents.DAMAGE.register(EventHandler::onDamage);
		LivingEntityEvents.INCOMING_HEAL.register(EventHandler::heal);
		LivingEntityEvents.INCOMING_DAMAGE.register(EventHandler::damageModified);
		PlayerEntityEvents.ATTACK_CRIT_BOOLEAN.register(EventHandler::attackCritChance);
		PlayerEntityEvents.ATTACK_CRIT_DAMAGE.register(EventHandler::attackCritDamage);
		
		DamageHandler.STORE.forEach(DamageModificationRegistry::register);
		
		PlayerData.registerRefundCondition((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.CONSTITUTION, 0.0D, value -> data.get(ExAPI.CONSTITUTION.get())));
		PlayerData.registerRefundCondition((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.STRENGTH, 0.0D, value -> data.get(ExAPI.STRENGTH.get())));
		PlayerData.registerRefundCondition((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.DEXTERITY, 0.0D, value -> data.get(ExAPI.DEXTERITY.get())));
		PlayerData.registerRefundCondition((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.INTELLIGENCE, 0.0D, value -> data.get(ExAPI.INTELLIGENCE.get())));
		PlayerData.registerRefundCondition((data, player) -> DataAttributesAPI.ifPresent(player, ExAPI.LUCKINESS, 0.0D, value -> data.get(ExAPI.LUCKINESS.get())));
		
		Registry.register(Registry.SOUND_EVENT, LEVEL_UP_SOUND.getId(), LEVEL_UP_SOUND);
		Registry.register(Registry.SOUND_EVENT, SP_SPEND_SOUND.getId(), SP_SPEND_SOUND);
		
		Registry.register(Registry.ITEM, new Identifier("playerex:test"), new Item(new Item.Settings()) {
			@Override
			public net.minecraft.util.TypedActionResult<ItemStack> use(net.minecraft.world.World world, PlayerEntity user, Hand hand) {
				boolean i = user.isSneaking();
				EntityAttribute attribute = ExAPI.WITHER_RESISTANCE.get();
				PlayerData data = ExAPI.INSTANCE.get(user);
				
				if(i) {
					data.add(attribute, 1.0D);
				} else {
					data.add(attribute, -1.0D);
				}
				
				
				
				return super.use(world, user, hand);
			}
		});
	}
}
