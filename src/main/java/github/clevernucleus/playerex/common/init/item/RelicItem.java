package github.clevernucleus.playerex.common.init.item;

import java.util.Map;

import javax.annotation.Nullable;

import github.clevernucleus.playerex.api.ElementRegistry;
import github.clevernucleus.playerex.api.Util;
import github.clevernucleus.playerex.api.element.IElement;
import github.clevernucleus.playerex.common.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

/**
 * Tiered Relic bauble.
 */
public class RelicItem extends Item implements ILoot {
	private final float weight;
	
	public RelicItem(final float par0) {
		super(new Properties().group(Group.INSTANCE).maxStackSize(1));
		this.weight = par0;
	}
	
	@Override
	public float getWeight() {
		return this.weight;
	}
	
	@Override
	public void inventoryTick(ItemStack par0, World par1, Entity par2, int par3, boolean par4) {
		if(par0.hasTag() && par0.getTag().contains("Elements") && par0.getTag().contains("Rareness")) return;
		
		Util.createRandomRelic(par0);
	}
	
	/**
	 * @param par0 The input stack.
	 * @param par1 The player/entity.
	 */
	public void equipped(final ItemStack par0, final LivingEntity par1) {
		if(par1.world.isRemote) return;
		if(!(par1 instanceof PlayerEntity)) return;
		if(!par0.hasTag()) return;
		
		PlayerEntity var0 = (PlayerEntity)par1;
		Map<IElement, Double> var1 = Util.attributeMap(par0.getTag());
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
			for(Map.Entry<IElement, Double> var2 : var1.entrySet()) {
				var2.getKey().add(var0, var, var2.getValue().doubleValue());
			}
		});
		
		CommonEvents.syncTag(var0);
	}
	
	/**
	 * @param par0 The input stack.
	 * @param par1 The player/entity.
	 */
	public void unequipped(final ItemStack par0, final LivingEntity par1) {
		if(par1.world.isRemote) return;
		if(!(par1 instanceof PlayerEntity)) return;
		if(!par0.hasTag()) return;
		
		PlayerEntity var0 = (PlayerEntity)par1;
		Map<IElement, Double> var1 = Util.attributeMap(par0.getTag());
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
			for(Map.Entry<IElement, Double> var2 : var1.entrySet()) {
				var2.getKey().add(var0, var, -var2.getValue().doubleValue());
			}
		});
		
		CommonEvents.syncTag(var0);
	}
	
	@Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack par0, @Nullable CompoundNBT par1) {
		return new ICapabilityProvider() {
			private final LazyOptional<ICurio> curio = LazyOptional.of(() -> new ICurio() {
				
				@Override
				public void onEquip(String var0, int var1, LivingEntity var2) {
					equipped(par0, var2);
				}
				
				@Override
				public void onUnequip(String var0, int var1, LivingEntity var2) {
					unequipped(par0, var2);
				}
			});
			
			@Override
			public <T> LazyOptional<T> getCapability(Capability<T> par0, Direction par1) {
				return CuriosCapability.ITEM.orEmpty(par0, curio);
			}
		};
	}
}
