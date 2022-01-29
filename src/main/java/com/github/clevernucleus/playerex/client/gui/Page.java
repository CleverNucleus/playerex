package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageLayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class Page {
	private static final Identifier DEFAULT = new Identifier(ExAPI.MODID, "textures/gui/default.png");
	private final List<PageLayer> layers;
	private final Identifier pageId, icon, texture;
	private final Text title;
	
	public Page(final Identifier pageId, final Identifier icon, final Identifier texture, final Text title) {
		this.layers = new ArrayList<PageLayer>();
		this.pageId = pageId;
		this.icon = icon;
		this.texture = texture;
		this.title = title;
	}
	
	public Page(final Identifier pageId, final Identifier icon, final Text title) {
		this(pageId, icon, DEFAULT, title);
	}
	
	protected final void addLayer(PageLayer layer) {
		this.layers.add(layer);
	}
	
	protected final void forEachLayer(Consumer<PageLayer> consumer) {
		this.layers.forEach(consumer);
	}
	
	public Identifier id() {
		return this.pageId;
	}
	
	public Identifier icon() {
		return this.icon;
	}
	
	public Identifier texture() {
		return this.texture;
	}
	
	public Text title() {
		return this.title;
	}
}
