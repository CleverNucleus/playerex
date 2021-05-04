package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientReg;
import com.github.clevernucleus.playerex.api.client.Page;
import com.github.clevernucleus.playerex.client.ClientConfig;
import com.github.clevernucleus.playerex.init.Registry;
import com.github.clevernucleus.playerex.init.container.PlayerAttributesContainer;
import com.github.clevernucleus.playerex.init.container.SwitchScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PlayerAttributesScreen extends ContainerScreen<PlayerAttributesContainer> {
	
	/** Main GUI resources. */
	public static final ResourceLocation GUI = new ResourceLocation(ExAPI.MODID, "textures/gui/gui.png");
	
	/** Tab resources. */
	public static final ResourceLocation TAB = new ResourceLocation(ExAPI.MODID, "textures/gui/tab.png");
	
	private static final int[][] TAB_LOCATIONS = new int[][] {{0, -28}, {32, -28}, {61, -28}, {90, -28}, {119, -28}, {148, -28}, {3, 162}, {32, 162}, {61, 162}, {90, 162}, {119, 162}, {148, 162}};
	
	private List<Page> pages = new ArrayList<Page>();
	private Page activePage;
	
	public PlayerAttributesScreen(final PlayerAttributesContainer par0, final PlayerInventory par1, final ITextComponent par2) {
		super(par0, par1, par2);
		
		this.pages.add(0, ClientReg.getPage(DefaultPage.REGISTRY_NAME));
		this.activePage = this.pages.get(0);
		
		Stream<Page> var0 = ClientReg.pageRegistry().stream().filter(var -> var != DefaultPage.REGISTRY_NAME).map(ClientReg::getPage);
		List<Page> var1 = var0.collect(Collectors.toList());
		
		var1.forEach(var -> {
			if(this.pages.size() < 12) {
				this.pages.add(var);
			}
		});
		var0.close();
	}
	
	public Page getPage(int par0) {
		return this.pages.get(par0);
	}
	
	@Override
	public void render(MatrixStack par0, int par1, int par2, float par3) {
		this.renderBackground(par0);
		
		super.render(par0, par1, par2, par3);
		
		this.activePage.render(par0, par1, par2, par3);
		this.buttons.forEach(var -> {
			if(var instanceof PageButton) {
				PageButton var1 = (PageButton)var;
				
				if(var1.isHovered()) {
					this.renderTooltip(par0, getPage(var1.additionalData()).getTitle(), par1, par2);
				}
			}
		});
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack par0, int par1, int par2) {
		this.activePage.drawGuiContainerForegroundLayer(par0, par1, par2);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack par0, float par1, int par2, int par3) {
		int var0 = this.guiLeft;
		int var1 = (this.height - this.ySize) / 2;
		
		this.minecraft.getTextureManager().bindTexture(GUI);
		this.blit(par0, var0, var1, 0, 0, this.xSize, this.ySize);
		this.activePage.drawGuiContainerBackgroundLayer(par0, par1, par2, par3);
		this.buttons.forEach(var -> {
			var.render(par0, par2, par3, par1);
		});
	}
	
	@Override
	protected void init() {
		super.init();
		
		this.addButton(new TexturedButton(this, ClientConfig.CLIENT.guiButtonX.get().intValue(), ClientConfig.CLIENT.guiButtonY.get().intValue(), 14, 13, 190, 0, -1, (var0, var1) -> {
			Registry.NETWORK.sendToServer(new SwitchScreens(true));
			InventoryScreen var2 = new InventoryScreen(Minecraft.getInstance().player);
			Minecraft.getInstance().displayGuiScreen(var2);
		}));
		
		if(this.pages.size() > 1) {
			for(int var = 0; var < this.pages.size(); var++) {
				this.addButton(new PageButton(this, TAB_LOCATIONS[var][0], TAB_LOCATIONS[var][1], 28, 32, (var % 6) * 28, (var < 6 ? 0 : 64), var, (var0, var1) -> {
					this.activePage = getPage(var1);
					this.activePage.init(this.minecraft, this, this.width, this.height);
					this.buttons.forEach(var2 -> {
						if(var2 instanceof PageButton) {
							var2.active = true;
						}
					});
					
					for(Page var2 : this.pages) {
						if(var2 != this.activePage) {
							for(Widget var3 : var2.getButtonList()) {
								var3.visible = false;
							}
						}
					}
					
					for(Widget var2 : this.activePage.getButtonList()) {
						var2.visible = true;
					}
				}));
			}
		}
		
		this.activePage.init(this.minecraft, this, this.width, this.height);
		
		for(Widget var : this.activePage.getButtonList()) {
			this.addButton(var);
		}
	}
}
