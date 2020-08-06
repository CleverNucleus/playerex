package clevernucleus.playerex.common.event;

import javax.annotation.Nonnull;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.capability.CapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
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
		
		Registry.ELEMENTS.apply(par0).ifPresent(var -> {
			var.init(par0);
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
			par0.addCapability(new ResourceLocation(PlayerEx.MODID, "playerelements"), new CapabilityProvider());
		}
	}
	
	/**
	 * Event firing when the player gets cloned.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onPlayerEntityCloned(net.minecraftforge.event.entity.player.PlayerEvent.Clone par0) {
		PlayerEntity var0 = par0.getPlayer();
		PlayerEntity var1 = par0.getOriginal();
		
		if(var0.world.isRemote) return;
		
		try {
			Registry.ELEMENTS.apply(var0).ifPresent(par1 -> {
				Registry.ELEMENTS.apply(var1).ifPresent(par2 -> {
					var0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(var1.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(var1.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ARMOR).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(var1.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue());
					var0.getAttribute(SharedMonsterAttributes.LUCK).setBaseValue(var1.getAttribute(SharedMonsterAttributes.LUCK).getBaseValue());
					
					par1.read(par2.write());
				});
			});
		} catch(Exception parE) {}
		
		syncTag(var0);
		
		if(par0.isWasDeath()) {
			var0.heal(var0.getMaxHealth());
		}
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
