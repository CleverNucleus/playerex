package git.clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.api.attribute.PlayerAttributes;
import git.clevernucleus.playerex.api.client.ClientReg;
import git.clevernucleus.playerex.api.client.Page;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ResistancePage extends Page {
	
	/** Registry Name for this page. */
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(ExAPI.MODID, "player_resistances");
	private final List<DynamicTextComponent> dynamicTextComponents = new ArrayList<DynamicTextComponent>();
	private final DecimalFormat text = new DecimalFormat("##.##");
	
	private final DynamicTextComponent knockback = new DynamicTextComponent(67, 50, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.knockback_resistance", text.format(10D * par1.get(par0, PlayerAttributes.KNOCKBACK_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.knockback_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.KNOCKBACK_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.KNOCKBACK_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent damage = new DynamicTextComponent(67, 62, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.damage_reduction", text.format(10D * par1.get(par0, PlayerAttributes.DAMAGE_REDUCTION)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.damage_reduction.alt") + text.format(100D * par1.get(par0, PlayerAttributes.DAMAGE_REDUCTION)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.DAMAGE_REDUCTION.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent fire = new DynamicTextComponent(67, 86, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.fire_resistance", text.format(10D * par1.get(par0, PlayerAttributes.FIRE_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.fire_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.FIRE_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.FIRE_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent lava = new DynamicTextComponent(67, 98, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.lava_resistance", text.format(10D * par1.get(par0, PlayerAttributes.LAVA_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.lava_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.LAVA_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.LAVA_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent explosion = new DynamicTextComponent(67, 110, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.explosion_resistance", text.format(10D * par1.get(par0, PlayerAttributes.EXPLOSION_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.explosion_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.EXPLOSION_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.EXPLOSION_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent poison = new DynamicTextComponent(67, 134, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.poison_resistance", text.format(10D * par1.get(par0, PlayerAttributes.POISON_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.poison_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.POISON_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.POISON_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent wither = new DynamicTextComponent(67, 146, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.wither_resistance", text.format(10D * par1.get(par0, PlayerAttributes.WITHER_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.wither_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.WITHER_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.WITHER_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent falling = new DynamicTextComponent(67, 170, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.falling_resistance", text.format(10D * par1.get(par0, PlayerAttributes.FALLING_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.falling_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.FALLING_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.FALLING_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	private final DynamicTextComponent drowning = new DynamicTextComponent(67, 182, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.drowning_resistance", text.format(10D * par1.get(par0, PlayerAttributes.DROWNING_RESISTANCE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.drowning_resistance.alt") + text.format(100D * par1.get(par0, PlayerAttributes.DROWNING_RESISTANCE)) + "%"));
		
		ClientReg.getTooltips(PlayerAttributes.DROWNING_RESISTANCE.registryName()).forEach(var -> var0.add(new StringTextComponent(var.apply(par0, par1))));
		
		return var0;
	});
	public ResistancePage() {
		super(new TranslationTextComponent(ExAPI.MODID + ".page.player_resistances"));
		
		this.dynamicTextComponents.add(this.knockback);
		this.dynamicTextComponents.add(this.damage);
		this.dynamicTextComponents.add(this.fire);
		this.dynamicTextComponents.add(this.lava);
		this.dynamicTextComponents.add(this.explosion);
		this.dynamicTextComponents.add(this.falling);
		this.dynamicTextComponents.add(this.poison);
		this.dynamicTextComponents.add(this.wither);
		this.dynamicTextComponents.add(this.drowning);
	}
	
	@Override
	public ItemStack displayStack() {
		return new ItemStack(Items.SHIELD);
	}
	
	@Override
	public void render(MatrixStack par0, int par1, int par2, float par3) {
		super.render(par0, par1, par2, par3);
		
		this.dynamicTextComponents.forEach(var -> var.drawAlt(par0, this.font, this.minecraft.player, this.width, this.height, par1, par2));
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(MatrixStack par0, int par1, int par2) {
		this.font.drawString(par0, this.title.getString(), 9F, 9F, 4210752);
		this.dynamicTextComponents.forEach(var -> var.draw(par0, this.font, this.minecraft.player));
	}
}
