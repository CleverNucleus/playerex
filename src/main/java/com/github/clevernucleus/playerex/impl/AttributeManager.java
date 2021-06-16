package com.github.clevernucleus.playerex.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

public final class AttributeManager implements ResourceReloadListener, IdentifiableResourceReloadListener {
	private static final Gson GSON = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
	private static final int PATH_SUFFIX_LENGTH = ".json".length();
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DIRECTORY = "attributes";
	
	public AttributeManager() {}
	
	private CompletableFuture<Map<Identifier, PlayerAttribute>> prepare(ResourceManager manager, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			Map<Identifier, PlayerAttribute> map = Maps.newHashMap();
			
			int length = DIRECTORY.length() + 1;
			
			for(Identifier resource : manager.findResources(DIRECTORY, file -> file.endsWith(".json"))) {
				String path = resource.getPath();
				Identifier id = new Identifier(resource.getNamespace(), path.substring(length, path.length() - PATH_SUFFIX_LENGTH));
				
				try (
					Resource resourceStream = manager.getResource(resource);
					InputStream inputStream = resourceStream.getInputStream();
					Reader readerStream = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				) {
					PlayerAttribute attributeResource = JsonHelper.deserialize(GSON, readerStream, PlayerAttribute.class);
					
					if(attributeResource != null) {
						PlayerAttribute attribute = map.put(id, attributeResource);
						
						if(attribute != null) throw new IllegalStateException("Duplicate data file ignored with ID " + id);
					} else {
						LOGGER.error("Couldn't load data file {} from {} as it's null or empty", id, resource);
					}
					
					IOUtils.closeQuietly(resourceStream);
				} catch (IOException exception) {
					LOGGER.error("Couldn't parse data file {} from {}", id, resource, exception);
				}
			}
			
			return map;
		}, executor);
	}
	
	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		CompletableFuture<Map<Identifier, PlayerAttribute>> completablefuture = this.prepare(manager, prepareExecutor);
		
		return CompletableFuture.allOf(completablefuture).thenCompose(synchronizer::whenPrepared).thenAcceptAsync(var -> {
			Map<Identifier, PlayerAttribute> collectedMap = completablefuture.join();
			
			DefaultAttributeContainer.Builder container = DefaultAttributeContainer.builder();
			
			for(Map.Entry<Identifier, PlayerAttribute> entry : collectedMap.entrySet()) {
				Identifier identifier = entry.getKey();
				PlayerAttribute attribute = entry.getValue().build(identifier);
				
				if(attribute.type() == AttributeType.GAME) {
					container.add(attribute.get(), attribute.defaultValue());
				}
				
				PlayerEx.ATTRIBUTES.put(identifier, attribute);
			}
			
			FabricDefaultAttributeRegistry.register(EntityType.PLAYER, container);
		}, applyExecutor);
	}
	
	@Override
	public Identifier getFabricId() {
		return new Identifier(ExAPI.MODID, "attributes");
	}
}
