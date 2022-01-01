package com.github.clevernucleus.playerex.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class PageRegistry {
	
	
	public static void registerPage(final Page page) {
		com.github.clevernucleus.playerex.client.gui.PageRegistryImpl.addPage(page);
	}
	
	
	public static void registerLayer(final Identifier pageId, PageLayer.Builder builder) {
		com.github.clevernucleus.playerex.client.gui.PageRegistryImpl.addLayer(pageId, builder);
	}
}
