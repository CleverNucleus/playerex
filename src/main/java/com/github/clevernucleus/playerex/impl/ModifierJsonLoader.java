package com.github.clevernucleus.playerex.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public final class ModifierJsonLoader implements SimpleSynchronousResourceReloadListener {
	private static final Gson GSON = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
	private static final int PATH_SUFFIX_LENGTH = ".json".length();
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DIRECTORY = "attributes";
	private static final Identifier ID = new Identifier(ExAPI.MODID, "modifiers");
	
	public final Map<Identifier, UUID> modifiers;
	
	public ModifierJsonLoader() {
		this.modifiers = new HashMap<Identifier, UUID>();
	}
	
	@Override
	public void reload(ResourceManager manager) {
		Map<Identifier, ModifiersJson> local = new HashMap<Identifier, ModifiersJson>();
		int length = DIRECTORY.length() + 1;
		
		for(Identifier resource : manager.findResources(DIRECTORY, file -> file.endsWith("modifiers.json"))) {
			String path = resource.getPath();
			Identifier identifier = new Identifier(resource.getNamespace(), path.substring(length, path.length() - PATH_SUFFIX_LENGTH));
			
			try (
				Resource resourceStream = manager.getResource(resource);
				InputStream inputStream = resourceStream.getInputStream();
				Reader readerStream = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			) {
				ModifiersJson json = JsonHelper.deserialize(GSON, readerStream, ModifiersJson.class);
				
				if(json != null) {
					ModifiersJson object = local.put(identifier, json);
					
					if(object != null) throw new IllegalStateException("Duplicate data file ignored with ID " + identifier);
				} else {
					LOGGER.error("Couldn't load data file {} from {} as it's null or empty", identifier, resource);
				}
				
				resourceStream.close();
			} catch(IOException exception) {
				LOGGER.error("Couldn't parse data file {} from {}", identifier, resource, exception);
			}
		}
		
		Map<Identifier, UUID> modifiers = new HashMap<Identifier, UUID>();
		
		for(Identifier identifier : local.keySet()) {
			ModifiersJson modifiersJson = local.get(identifier);
			modifiersJson.put(modifiers);
		}
		
		this.modifiers.clear();
		this.modifiers.putAll(modifiers);
	}
	
	@Override
	public Identifier getFabricId() {
		return ID;
	}
}
