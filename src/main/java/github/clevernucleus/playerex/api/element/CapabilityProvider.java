package github.clevernucleus.playerex.api.element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import github.clevernucleus.playerex.api.ElementRegistry;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Capability helper class used to provide the IO abilities.
 */
public class CapabilityProvider implements ICapabilitySerializable<INBT> {
	private final LazyOptional<IPlayerElements> optional;
	private final IPlayerElements data;
	
	public CapabilityProvider() {
		this.data = new PlayerElements();
		this.optional = LazyOptional.of(() -> data);
	}
	
	@Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nullable Capability<T> par0, Direction par1) {
    	return ElementRegistry.PLAYER_ELEMENTS.orEmpty(par0, optional);
    }
    
    @Override
    public INBT serializeNBT() {
    	return ElementRegistry.PLAYER_ELEMENTS.writeNBT(data, null);
    }
    
    @Override
    public void deserializeNBT(INBT par0) {
    	ElementRegistry.PLAYER_ELEMENTS.readNBT(data, null, par0);
    }
}
