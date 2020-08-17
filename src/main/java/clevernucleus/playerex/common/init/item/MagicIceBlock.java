package clevernucleus.playerex.common.init.item;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BreakableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;

public class MagicIceBlock extends BreakableBlock {
	public MagicIceBlock() {
		super(Properties.from(Blocks.ICE));
	}
	
	@Override
	public void tick(BlockState par0, ServerWorld par1, BlockPos par2, Random par3) {
		if(par1.getLightFor(LightType.BLOCK, par2) > 11 - par0.getOpacity(par1, par2)) {
			par1.removeBlock(par2, false);
		}
	}
}
