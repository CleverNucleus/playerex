package clevernucleus.playerex.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.container.PlayerElementsContainer;
import clevernucleus.playerex.common.network.SwitchScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Main screen for interacting with the player's element attributes.
 */
public class PlayerElementsScreen extends ContainerScreen<PlayerElementsContainer> {
	
	/** Main GUI resources. */
	public static final ResourceLocation GUI = new ResourceLocation(PlayerEx.MODID, "textures/gui/gui.png");
	
	public PlayerElementsScreen(final PlayerElementsContainer par0, final PlayerInventory par1, final ITextComponent par2) {
		super(par0, par1, par2);
	}
	
	@Override
	public void render(MatrixStack par0, int par1, int par2, float par3) {
		this.renderBackground(par0);
		
		super.render(par0, par1, par2, par3);
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack par0, int par1, int par2) {
		this.font.drawString(par0, this.title.getString(), 9F, 9F, 4210752);//TODO pages/tabs/slots
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack par0, float par1, int par2, int par3) {
		int var0 = this.guiLeft;
		int var1 = (this.height - this.ySize) / 2;
		
		this.minecraft.getTextureManager().bindTexture(GUI);
		this.blit(par0, var0, var1, 0, 0, this.xSize, this.ySize);
		this.buttons.forEach(var -> var.render(par0, par2, par3, par1));
	}
	
	@Override
	protected void init() {
		this.buttons.clear();
		super.init();
		
		this.addButton(new TexturedButton(this, 155, 7, 14, 13, 190, 0, 0, (var0, var1) -> {
			Registry.NETWORK.sendToServer(new SwitchScreens(true));
			InventoryScreen var2 = new InventoryScreen(Minecraft.getInstance().player);
			Minecraft.getInstance().displayGuiScreen(var2);
		}));
	}
}
