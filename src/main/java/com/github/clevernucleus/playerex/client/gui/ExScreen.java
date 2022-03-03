package com.github.clevernucleus.playerex.client.gui;

import java.util.List;
import java.util.function.Consumer;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.client.NetworkHandlerClient;
import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.github.clevernucleus.playerex.client.gui.widget.TabButtonWidget;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.handler.ExScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ExScreen extends AbstractInventoryScreen<ExScreenHandler> {
	private int tab = 0;
	
	public ExScreen(ExScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
		this.tab = screenHandler.pageId;
		this.getPages().forEach(page -> this.addLayers(page, screenHandler, playerInventory, text));
	}
	
	private List<Page> getPages() {
		return ((ExScreenData)this).pages();
	}
	
	private void addLayers(Page page, ExScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		for(PageLayer.Builder builder : PageRegistryImpl.findPageLayers(page.id())) {
			page.addLayer(builder.build(this, screenHandler, playerInventory, text));
		}
	}
	
	private Page currentPage() {
		int index = MathHelper.clamp(this.tab, 0, this.getPages().size() - 1);
		return this.getPages().get(index);
	}
	
	private void forEachButton(Consumer<ButtonWidget> consumer) {
		this.children().stream().filter(e -> e instanceof ButtonWidget).forEach(e -> consumer.accept((ButtonWidget)e));
	}
	
	private void forEachTab(Consumer<TabButtonWidget> consumer) {
		this.children().stream().filter(e -> e instanceof TabButtonWidget).forEach(e -> consumer.accept((TabButtonWidget)e));
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(PlayerExClient.keyBinding.matchesKey(keyCode, scanCode)) {
			this.onClose();
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.currentPage().forEachLayer(layer -> layer.render(matrices, mouseX, mouseY, delta));
		this.forEachButton(button -> button.renderTooltip(matrices, mouseX, mouseY));
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		int u = this.x;
		int v = (this.height - this.backgroundHeight) / 2;
		
		RenderSystem.setShaderTexture(0, this.currentPage().texture());
		this.drawTexture(matrices, u + 6, v + 6, 0, 0, this.backgroundWidth - 12, this.backgroundWidth - 12);
		
		RenderSystem.setShaderTexture(0, ((ConfigImpl)ExAPI.getConfig()).darkMode ? PlayerExClient.GUI_DARK : PlayerExClient.GUI);
		this.drawTexture(matrices, u, v, 0, 0, this.backgroundWidth, this.backgroundWidth);
		this.currentPage().forEachLayer(layer -> layer.drawBackground(matrices, delta, mouseX, mouseY));
		this.forEachButton(button -> button.render(matrices, mouseX, mouseY, delta));
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		this.textRenderer.draw(matrices, this.currentPage().title(), (float)this.titleX, (float)(this.titleY + 2), 4210752);
	}
	
	@Override
	protected void init() {
		super.init();
		this.clearChildren();
		this.addDrawableChild(new TabButtonWidget(this, PlayerExClient.INVENTORY, 0, 0, -28, true, NetworkHandlerClient::openInventoryScreen));
		
		for(int i = 0; i < this.getPages().size(); i++) {
			Page page = this.getPages().get(i);
			int j = i + 1;
			int u = ((j % 5) * 29) + (j < 6 ? 0 : 3);
			int v = j < 6 ? -28 : 162;
			
			this.addDrawableChild(new TabButtonWidget(this, page, j, u, v, true, btn -> {
				TabButtonWidget button = (TabButtonWidget)btn;
				this.tab = button.index() - 1;
				this.forEachTab(tab -> tab.active = true);
				button.active = false;
				this.init();
			}));
		}
		
		this.forEachTab(tab -> {
			if(tab.index() - 1 == this.tab) {
				tab.active = false;
			}
		});
		
		this.currentPage().forEachLayer(layer -> {
			layer.init(this.client, this.width, this.height);
			layer.children().stream().filter(e -> e instanceof ButtonWidget).forEach(e -> this.addDrawableChild((ButtonWidget)e));
		});
	}
}
