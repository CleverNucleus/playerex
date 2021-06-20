package com.github.clevernucleus.playerex.impl.attribute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.impl.ExRegistryImpl;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class AttributeManager implements SimpleSynchronousResourceReloadListener {
	private static final Gson GSON = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
	private static final int PATH_SUFFIX_LENGTH = ".json".length();
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DIRECTORY = "attributes";
	private boolean initialised;
	
	public AttributeManager() {
		this.initialised = false;
	}
	
	private EntityAttribute getOrDefault(IPlayerAttribute keyIn) {
		Identifier registryKey = keyIn.registryKey();
		EntityAttribute value = Registry.ATTRIBUTE.get(registryKey);
		
		if(value == null) {
			value = Registry.register(Registry.ATTRIBUTE, registryKey, (new ClampedEntityAttribute(keyIn.translationKey(), keyIn.defaultValue(), keyIn.minValue(), keyIn.maxValue())).setTracked(true));
		} else {
			value.setTracked(true);
		}
		
		return ((IClampedEntityAttribute)value).withLimits(keyIn.minValue(), keyIn.maxValue());
	}
	
	@Override
	public void apply(ResourceManager manager) {
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
				
				resourceStream.close();
			} catch (IOException exception) {
				LOGGER.error("Couldn't parse data file {} from {}", id, resource, exception);
			}
		}
		
		if(this.initialised) return;
		
		DefaultAttributeContainer.Builder builder = DefaultAttributeContainer.builder();
		
		for(Map.Entry<Identifier, PlayerAttribute> entry : map.entrySet()) {
			Identifier attributeKey = entry.getKey();
			PlayerAttribute attributeEntry = entry.getValue().build(attributeKey);
			
			((ExRegistryImpl)ExAPI.REGISTRY.get()).put(attributeKey, attributeEntry);
			
			EntityAttribute attribute = this.getOrDefault(attributeEntry);
			builder.add(attribute, attributeEntry.defaultValue());
		}
		
		FabricDefaultAttributeRegistry.register(EntityType.PLAYER, builder);
	}
	
	@Override
	public Identifier getFabricId() {
		return new Identifier(ExAPI.MODID, "attributes");
	}
}
