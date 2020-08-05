package clevernucleus.playerex.common.init.event;

import javax.annotation.Nonnull;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.capability.CapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Repository for common events on the FORGE bus.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {
	
	/**
	 * Sync event pass-through with safety functions.
	 * @param par0
	 */
	private static void syncTag(final @Nonnull PlayerEntity par0) {
		if(par0 == null) return;
		if(par0.world.isRemote) return;
		
		par0.getCapability(Registry.PLAYER_ELEMENTS, null).ifPresent(var -> {
			var.sync(par0);
		});
	}
	
	/**
	 * Event for attaching capabilities.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onCapabilityAttachEntity(final net.minecraftforge.event.AttachCapabilitiesEvent<Entity> par0) {
		if(par0.getObject() instanceof PlayerEntity) {
			par0.addCapability(new ResourceLocation(PlayerEx.MODID, "PlayerElements"), new CapabilityProvider());
		}
	}
	
	/**
	 * Event firing when the player gets cloned.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onPlayerEntityCloned(net.minecraftforge.event.entity.player.PlayerEvent.Clone par0) {
		PlayerEntity var0 = par0.getPlayer();
		
		if(var0.world.isRemote) return;
		
		try {
			Registry.PLAYER_ELEMENTS_FROM_PLAYER.apply(var0).ifPresent(var1 -> {
				Registry.PLAYER_ELEMENTS_FROM_PLAYER.apply(par0.getOriginal()).ifPresent(var2 -> {
					var1.read(var2.write());
				});
			});
		} catch(Exception parE) {}
		
		syncTag(var0);
	}
	
	/**
	 * Event firing when a player changes dimensions.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onPlayerChangedDimension(net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent par0) {
		syncTag(par0.getPlayer());
	}
	
	/**
	 * Event firing when the player respawns.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onPlayerRespawn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent par0) {
		syncTag(par0.getPlayer());
	}
	
	/**
	 * Event firing when a player logs in.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onPlayerLoggedIn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent par0) {
		syncTag(par0.getPlayer());
	}
}
