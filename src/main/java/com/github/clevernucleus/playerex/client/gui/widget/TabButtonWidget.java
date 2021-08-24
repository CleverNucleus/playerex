package com.github.clevernucleus.playerex.client.gui.widget;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.Page;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class TabButtonWidget extends ButtonWidget {
	private static final Identifier TABS = new Identifier(ExAPI.MODID, "textures/gui/tab.png");
	private HandledScreen<?> parent;
	private Page page;
	private int index, dx, dy;
	
	public TabButtonWidget(HandledScreen<?> parent, Page page, int index, int x, int y, PressAction onPress, TooltipSupplier tooltipSupplier) {
		super(x, y, 28, 32, LiteralText.EMPTY, onPress, tooltipSupplier);
		
		this.parent = parent;
		this.page = page;
		this.index = index;
		this.dx = x;
		this.dy = y;
	}
}
