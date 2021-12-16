package com.github.clevernucleus.playerex;

import java.util.UUID;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.impl.ModifierJsonManager;
import com.github.clevernucleus.playerex.impl.PlayerDataManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PlayerEx implements ModInitializer {
	public static final String VERSION = FabricLoader.getInstance().getModContainer(ExAPI.MODID).get().getMetadata().getVersion().getFriendlyString();
	public static final Identifier HANDSHAKE = new Identifier(ExAPI.MODID, "handshake");
	public static final ModifierJsonManager MANAGER = new ModifierJsonManager();
	
	private static void loginQueryStart(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
		PacketByteBuf buf = PacketByteBufs.create();
		NbtCompound tag = new NbtCompound();
		NbtList ids = new NbtList();
		
		for(Identifier identifier : PlayerEx.MANAGER.modifiers.keySet()) {
			UUID uuid = PlayerEx.MANAGER.modifiers.get(identifier);
			NbtCompound entry = new NbtCompound();
			entry.putString("key", identifier.toString());
			entry.putUuid("id", uuid);
			ids.add(entry);
		}
		
		tag.put("Ids", ids);
		buf.writeNbt(tag);
		sender.sendPacket(HANDSHAKE, buf);
	}
	
	private static void loginQueryResponse(MinecraftServer server, ServerLoginNetworkHandler handler, boolean understood, PacketByteBuf buf, LoginSynchronizer synchronizer, PacketSender responseSender) {
		if(understood) {
			String version = buf.readString();
			
			if(!version.equals(VERSION)) {
				handler.disconnect(new LiteralText("Disconnected: client has PlayerEx " + version + ", but the server requires PlayerEx " + VERSION + "."));
			}
		} else {
			handler.disconnect(new LiteralText("Disconnected: server requires client to have PlayerEx version " + VERSION + "."));
		}
	}
	
	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MANAGER);
		ServerLifecycleEvents.SERVER_STARTING.register(server -> MANAGER.load());
		ServerLoginConnectionEvents.QUERY_START.register(PlayerEx::loginQueryStart);
		ServerLoginNetworking.registerGlobalReceiver(HANDSHAKE, PlayerEx::loginQueryResponse);
		
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			PlayerData playerData = ExAPI.INSTANCE.get(newPlayer);
			playerData.reset(); // TODO if we want everything to reset after dying
		});
		
		Registry.register(Registry.ITEM, new Identifier("playerex:test"), new Item(new Item.Settings()) {
			
			@Override
			public net.minecraft.util.TypedActionResult<net.minecraft.item.ItemStack> use(net.minecraft.world.World world, net.minecraft.entity.player.PlayerEntity user, net.minecraft.util.Hand hand) {
				boolean i = user.isSneaking();
				
				EntityAttribute attribute = EntityAttributes.GENERIC_ARMOR;
				PlayerData data = ExAPI.INSTANCE.get(user);
				String side = world.isClient ? "CLIENT" : "SERVER";
				
				if(!i) {
					user.sendMessage(new LiteralText("Data -> " + side + ": " + data.get(attribute)), false);
					user.sendMessage(new LiteralText("Game -> " + side + ": " + user.getAttributeValue(attribute)), false);
				} else {
					if(!world.isClient) {
						data.add(attribute, 2.0D);
					}
				}
				
				return super.use(world, user, hand);
			}
		});
	}
}
