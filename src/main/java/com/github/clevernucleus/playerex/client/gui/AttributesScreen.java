package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageRegistry;
import com.github.clevernucleus.playerex.api.client.PageRegistry.PageBuilder;
import com.github.clevernucleus.playerex.api.client.PageScreen;
import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;
import com.github.clevernucleus.playerex.client.gui.widget.TabButtonWidget;
import com.github.clevernucleus.playerex.client.network.ClientNetworkHandler;
import com.github.clevernucleus.playerex.handler.AttributesScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AttributesScreen extends AbstractInventoryScreen<AttributesScreenHandler> {
	public static final Identifier EX_GUI = new Identifier(ExAPI.MODID, "textures/gui/gui.png");
	private final List<PageScreen> pages = new ArrayList<PageScreen>();
	private PageScreen currentPageScreen;
	private int currentTab = 0;
	
	public AttributesScreen(AttributesScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
		
		this.pages.add(this.currentTab, PageRegistry.find(PlayerExClient.ATTTRIBUTES_PAGE_KEY).build(this, screenHandler, playerInventory));
		this.currentPageScreen = this.pages.get(this.currentTab);
		
		PageRegistry.immutableMap().entrySet().stream().filter(this::filter).map(entry -> entry.getValue().build(this, screenHandler, playerInventory)).forEach(this.pages::add);
	}
	
	private boolean filter(Entry<Identifier, PageBuilder> entry) {
		return !entry.getKey().equals(PlayerExClient.ATTTRIBUTES_PAGE_KEY);
	}
	
	private void onTooltip(ButtonWidget button, MatrixStack matrices, int mouseX, int mouseY) {
		this.renderTooltip(matrices, ((TabButtonWidget)button).page().getTitle(), mouseX, mouseY);
	}
	
	@Override
	protected void applyStatusEffectOffset() {
		if(this.client.player.getStatusEffects().isEmpty()) {
			this.x = (this.width - this.backgroundWidth) / 2;
			this.drawStatusEffects = false;
		} else {
			this.drawStatusEffects = true;
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		
		this.currentPageScreen.render(matrices, mouseX, mouseY, delta);
		this.buttons.forEach(btn -> btn.renderToolTip(matrices, mouseX, mouseY));
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		int u = this.x;
		int v = (this.height - this.backgroundHeight) / 2;
		
		this.client.getTextureManager().bindTexture(EX_GUI);
		this.drawTexture(matrices, u, v, 0, 0, this.backgroundWidth, this.backgroundHeight);
		this.currentPageScreen.drawBackground(matrices, delta, mouseX, mouseY);
		this.buttons.forEach(btn -> btn.render(matrices, mouseX, mouseY, delta));
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		this.textRenderer.draw(matrices, this.currentPageScreen.getTitle(), (float)this.titleX, (float)(this.titleY + 2), 4210752);
	}
	
	@Override
	protected void init() {
		super.init();
		this.buttons.clear();
		this.addButton(new ScreenButtonWidget(this, 155, 7, 190, 0, 14, 13, ClientNetworkHandler::openInventoryScreen));
		
		for(int i = 0; i < this.pages.size(); i++) {
			PageScreen page = this.pages.get(i);
			
			int u = ((i % 6) * 29) + (i < 6 ? 0 : 3);
			int v = i < 6 ? -28 : (this.backgroundHeight - 4);
			
			this.addButton(new TabButtonWidget(this, page, i, u, v, btn -> {
				TabButtonWidget button = (TabButtonWidget)btn;
				this.currentPageScreen = button.page();
				this.currentTab = button.index();
				this.buttons.stream().filter(b -> b instanceof TabButtonWidget).forEach(b -> b.active = true);
				button.active = false;
				this.init();
			}, this::onTooltip));
		}
		
		this.buttons.stream().filter(btn -> btn instanceof TabButtonWidget).forEach(btn -> {
			TabButtonWidget button = (TabButtonWidget)btn;
			
			if(button.index() == this.currentTab) {
				button.active = false;
			}
		});
		
		this.currentPageScreen.init(this.client, this.width, this.height);
		this.currentPageScreen.buttons().forEach(this::addButton);
	}
}
