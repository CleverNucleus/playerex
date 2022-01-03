package com.github.clevernucleus.playerex.api.client;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttribute;
import com.github.clevernucleus.dataattributes.api.util.Maths;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PacketType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;


@Environment(EnvType.CLIENT)
public final class ClientUtil {
	/** Format for two decimal places. */
	public static final DecimalFormat FORMATTING_2 = new DecimalFormat("###.##");
	/** Format for three decimal places. */
	public static final DecimalFormat FORMATTING_3 = new DecimalFormat("###.###");
	
	/**
	 * Takes the input attribute and looks for the {@link ExAPI#PERCENTAGE_PROPERTY} property; if present, multiplies the input 
	 * value by the property's multiplier. If not, then looks for the {@link ExAPI#MULTIPLIER_PROPERTY} property; if present, 
	 * multiplies the input value by the property's multiplier. Else returns the input value.
	 * @param attributeIn
	 * @param valueIn
	 * @return
	 */
	public static double displayValue(final Supplier<EntityAttribute> attributeIn, double valueIn) {
		IEntityAttribute attribute = (IEntityAttribute)attributeIn.get();
		
		if(attribute == null) return valueIn;
		if(attribute.hasProperty(ExAPI.PERCENTAGE_PROPERTY)) return valueIn * Maths.parse(attribute.getProperty(ExAPI.PERCENTAGE_PROPERTY));
		if(attribute.hasProperty(ExAPI.MULTIPLIER_PROPERTY)) return valueIn * Maths.parse(attribute.getProperty(ExAPI.MULTIPLIER_PROPERTY));
		return valueIn;
	}
	
	/**
	 * If the input value is positive, adds the "+" prefix. If the input attribute has the {@link ExAPI#PERCENTAGE_PROPERTY}
	 * property, appends the "%" suffix.
	 * @param attributeIn
	 * @param valueIn
	 * @return
	 */
	public static String formatValue(final Supplier<EntityAttribute> attributeIn, double valueIn) {
		String value = FORMATTING_3.format(valueIn);
		
		if(valueIn > 0.0D) {
			value = "+" + value;
		}
		
		IEntityAttribute attribute = (IEntityAttribute)attributeIn.get();
		
		if(attribute == null) return value;
		if(attribute.hasProperty(ExAPI.PERCENTAGE_PROPERTY)) {
			value = value + "%";
		}
		
		return value;
	}
	
	/**
	 * Looks at the input attribute's functions, and if there are any, formats them and adds them to the input list as tooltips.
	 * @param tooltip
	 * @param attributeIn
	 */
	public static void appendChildrenToTooltip(List<Text> tooltip, final Supplier<EntityAttribute> attributeIn) {
		IEntityAttribute attribute = (IEntityAttribute)attributeIn.get();
		
		if(attribute == null) return;
		
		for(var child : attribute.children().entrySet()) {
			IEntityAttribute attribute2 = child.getKey();
			double value = child.getValue();
			double displ = displayValue(() -> (EntityAttribute)attribute2, value);
			String formt = formatValue(() -> (EntityAttribute)attribute2, displ);
			MutableText mutableText = new LiteralText(formt + " ");
			mutableText.append(new TranslatableText(attribute2.getTranslationKey()));
			tooltip.add(mutableText.formatted(Formatting.GRAY));
		}
	}
	
	
	@SafeVarargs
	public static void modifyAttributes(final PacketType type, Consumer<BiConsumer<Supplier<EntityAttribute>, Double>> ... consumers) {
		if(consumers != null) {
			PacketType packetId = type == null ? PacketType.DEFAULT : type;
			com.github.clevernucleus.playerex.client.NetworkHandlerClient.modifyAttributes(packetId, consumers);
		}
	}
}
