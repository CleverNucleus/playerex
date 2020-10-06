package clevernucleus.playerex.common.event;

import javax.annotation.Nonnull;

import clevernucleus.playerex.api.ElementRegistry;
import clevernucleus.playerex.api.element.CapabilityProvider;
import clevernucleus.playerex.api.element.IPlayerElements;
import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.network.SyncPlayerElements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

/**
 * Repository for common events on the FORGE bus.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {
	
	/**
	 * Initialises the player capabilities with the config.
	 * @param par0
	 * @param par1
	 */
	private static void initTag(final @Nonnull PlayerEntity par0, IPlayerElements par1) {
		CompoundNBT var0 = par1.write();
		
		if(var0.getBoolean("Initialised")) return;
		
		ElementRegistry.CONSTITUTION.add(par0, par1, 6D);//TODO: Config
		ElementRegistry.STRENGTH.add(par0, par1, 0D);//TODO: Config
		ElementRegistry.DEXTERITY.add(par0, par1, 0D);//TODO: Config
		ElementRegistry.INTELLIGENCE.add(par0, par1, 0D);//TODO: Config
		ElementRegistry.LUCKINESS.add(par0, par1, 0D);//TODO: Config
		ElementRegistry.HEALTH.add(par0, par1, -20D);//TODO: Config
		
		var0.putBoolean("Initialised", true);
	}
	
	/**
	 * Sync event pass-through with safety functions.
	 * @param par0
	 */
	private static void syncTag(final @Nonnull PlayerEntity par0) {
		if(par0 == null) return;
		if(par0.world.isRemote) return;
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(par0).ifPresent(var -> {
			initTag(par0, var);
			
			CompoundNBT var0 = new CompoundNBT();
			
			var0.put("Elements", var.write());
			var0.putDouble("generic.knockbackResistance", par0.getAttribute(Attributes.field_233820_c_).getBaseValue());
			var0.putDouble("generic.attackDamage", par0.getAttribute(Attributes.field_233823_f_).getBaseValue());
			
			Registry.NETWORK.sendTo(new SyncPlayerElements(var0), ((ServerPlayerEntity)par0).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
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
    public static void onPlayerEntityCloned(final net.minecraftforge.event.entity.player.PlayerEvent.Clone par0) {
		PlayerEntity var0 = par0.getPlayer();
		PlayerEntity var1 = par0.getOriginal();
		
		if(var0.world.isRemote) return;
		
		try {
			ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(par1 -> {
				ElementRegistry.GET_PLAYER_ELEMENTS.apply(var1).ifPresent(par2 -> {
					var0.getAttribute(Attributes.field_233818_a_).setBaseValue(var1.getAttribute(Attributes.field_233818_a_).getBaseValue());
					var0.getAttribute(Attributes.field_233820_c_).setBaseValue(var1.getAttribute(Attributes.field_233820_c_).getBaseValue());
					var0.getAttribute(Attributes.field_233823_f_).setBaseValue(var1.getAttribute(Attributes.field_233823_f_).getBaseValue());
					var0.getAttribute(Attributes.field_233825_h_).setBaseValue(var1.getAttribute(Attributes.field_233825_h_).getBaseValue());
					var0.getAttribute(Attributes.field_233826_i_).setBaseValue(var1.getAttribute(Attributes.field_233826_i_).getBaseValue());
					var0.getAttribute(Attributes.field_233827_j_).setBaseValue(var1.getAttribute(Attributes.field_233827_j_).getBaseValue());
					var0.getAttribute(Attributes.field_233828_k_).setBaseValue(var1.getAttribute(Attributes.field_233828_k_).getBaseValue());
					
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
    public static void onPlayerChangedDimension(final net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent par0) {
		syncTag(par0.getPlayer());
	}
	
	/**
	 * Event firing when the player respawns.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onPlayerRespawn(final net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent par0) {
		syncTag(par0.getPlayer());
	}
	
	/**
	 * Event firing when a player logs in.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onPlayerLoggedIn(final net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent par0) {
		syncTag(par0.getPlayer());
	}
}
