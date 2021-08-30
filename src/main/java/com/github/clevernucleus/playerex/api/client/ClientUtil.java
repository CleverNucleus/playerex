package com.github.clevernucleus.playerex.api.client;

import java.text.DecimalFormat;
import java.util.List;

import com.github.clevernucleus.dataattributes.api.API;
import com.github.clevernucleus.dataattributes.api.attribute.AttributeBehaviour;
import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.dataattributes.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;


public final class ClientUtil {
	
	
	public static final DecimalFormat FORMATTING_2 = new DecimalFormat("###.##");
	
	
	public static final DecimalFormat FORMATTING_3 = new DecimalFormat("###.###");
	
	
	public static double displayValue(final IAttribute attributeIn, double valueIn) {
		if(attributeIn.hasProperty(ExAPI.PERCENTAGE_PROPERTY)) return valueIn * attributeIn.getProperty(ExAPI.PERCENTAGE_PROPERTY);
		if(attributeIn.hasProperty(ExAPI.MULTIPLIER_PROPERTY)) return valueIn * attributeIn.getProperty(ExAPI.MULTIPLIER_PROPERTY);
		return valueIn;
	}
	
	
	public static String formatValue(final IAttribute attributeIn, double valueIn) {
		String value = FORMATTING_3.format(valueIn);
		
		if(valueIn > 0) {
			value = "+" + value;
		}
		
		if(attributeIn.hasProperty(ExAPI.PERCENTAGE_PROPERTY)) {
			value = value + "%";
		}
		
		return value;
	}
	
	
	public static void appendFunctionsToTooltip(List<Text> tooltip, final EntityAttribute attributeIn) {
		if(attributeIn == null) return;
		
		IAttribute attribute = (IAttribute)attributeIn;
		
		for(IAttributeFunction function : attribute.functions()) {
			Identifier identifier = function.attribute();
			EntityAttribute subAttributeIn = API.getAttribute(identifier).get();
			
			if(subAttributeIn == null) continue;
			
			IAttribute subAttribute = (IAttribute)subAttributeIn;
			double displayValue = displayValue(subAttribute, function.multiplier());
			String formattedValue = formatValue(subAttribute, displayValue);
			MutableText mutableText = new LiteralText(formattedValue + " ");
			mutableText.append(new TranslatableText(subAttributeIn.getTranslationKey()));
			
			if(function.behaviour().equals(AttributeBehaviour.DIMINISHING)) {
				mutableText.append(new TranslatableText("gui.playerex.text.diminishing"));
			}
			
			tooltip.add(mutableText.formatted(Formatting.GRAY));
		}
	}
}
