package com.github.clevernucleus.playerex.client.gui;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ExScreenData {
	int getX();
	int getY();
	List<Page> pages();
}
