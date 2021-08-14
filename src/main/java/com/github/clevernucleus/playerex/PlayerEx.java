package com.github.clevernucleus.playerex;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.handler.NetworkHandler;
import com.github.clevernucleus.playerex.impl.ModifierJsonLoader;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class PlayerEx implements ModInitializer {
	public static final ModifierJsonLoader MANAGER = new ModifierJsonLoader();
	/** Manual; ugh, I know. */
	public static final String VERSION = "3.0.1";
	
	public static final Item P = new Item(new Item.Settings()) {
		
		@Override
		public net.minecraft.util.TypedActionResult<net.minecraft.item.ItemStack> use(net.minecraft.world.World world, net.minecraft.entity.player.PlayerEntity user, net.minecraft.util.Hand hand) {
			if(!world.isClient) {
				ExAPI.DATA.get(user).set(EntityAttributes.GENERIC_ARMOR, 10);
			}
			
			
			return super.use(world, user, hand);
		}
	};
	
	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		ServerLoginConnectionEvents.QUERY_START.register(NetworkHandler::loginQueryStart);
		//ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(null);
		ServerLoginNetworking.registerGlobalReceiver(NetworkHandler.SYNC, NetworkHandler::loginQueryResponse);
		
		Registry.register(Registry.ITEM, new Identifier(ExAPI.MODID, "test"), P);
	}
}
