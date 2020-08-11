package clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.container.PlayerElementsContainer;
import clevernucleus.playerex.common.init.element.IElement;
import clevernucleus.playerex.common.network.AddPlayerElement;
import clevernucleus.playerex.common.network.SwitchScreens;
import clevernucleus.playerex.common.util.BiValue;
import clevernucleus.playerex.common.util.Calc;
import clevernucleus.playerex.common.util.TextDisplayPanel;
import clevernucleus.playerex.common.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Main screen for interacting with the player's element attributes.
 */
public class PlayerElementsScreen extends ContainerScreen<PlayerElementsContainer> {
	
	/** Main GUI resources. */
	public static final ResourceLocation GUI = new ResourceLocation(PlayerEx.MODID, "textures/gui/gui.png");
	
	private final DecimalFormat text = new DecimalFormat("##.##");
	private final TexturedButton[] attribute_button = new TexturedButton[5];
	private List<TextDisplayPanel> display = new ArrayList<TextDisplayPanel>();
	private PlayerEntity player = Minecraft.getInstance().player;
	
	public PlayerElementsScreen(final PlayerElementsContainer par0, final PlayerInventory par1, final ITextComponent par2) {
		super(par0, par1, par2);
	}
	
	/**
	 * Helper method to check if the mouse is hovering over a given position.
	 * @param par0 mouseX
	 * @param par1 mouseY
	 * @param par2 x-offset
	 * @param par3 y-offset
	 * @param par4 sizeX
	 * @param par5 sizeY
	 * @return True if the mouse is hovering over the input position.
	 */
	private boolean isMouseOver(int par0, int par1, int par2, int par3, int par4, int par5) {
		int var0 = this.guiLeft + par2;
		int var1 = ((this.height - this.ySize) / 2) + par3;
		
		return par0 >= var0 && par1 >= var1 && par0 < var0 + par4 && par1 < var1 + par5;
	}
	
	@Override
	public void render(int par0, int par1, float par2) {
		this.renderBackground();
		
		super.render(par0, par1, par2);
		
		this.renderHoveredToolTip(par0, par1);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par0, int par1) {
		this.font.drawString(this.title.getFormattedText(), 9F, 9F, 4210752);
		
		this.player = Minecraft.getInstance().player;
		this.display = Util.list(var0 -> {
			Registry.ELEMENTS.apply(player).ifPresent(var1 -> {
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.level", (int)var1.get(player, Registry.LEVEL), (int)(100 * Calc.expCoeff((float)var1.get(player, Registry.LEVEL), (float)var1.get(player, Registry.EXPERIENCE))), "%"), 14F, 36F, var2 -> {
					var2.add(new TranslationTextComponent("desc.level0", TextFormatting.GRAY));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.skillpoints", (int)var1.get(player, Registry.SKILLPOINTS)), 14F, 45F, var2 -> {
					var2.add(new TranslationTextComponent("desc.skillpoints0", TextFormatting.GRAY));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.constitution", (int)var1.get(player, Registry.CONSTITUTION)), 14F, 63F, var2 -> {
					var2.add(new TranslationTextComponent("desc.constitution0", TextFormatting.GRAY));
					var2.add(new TranslationTextComponent("desc.constitution1", TextFormatting.GRAY));
					var2.add(new TranslationTextComponent("desc.constitution2", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.constitution3", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.constitution4", TextFormatting.GRAY, "%"));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.strength", (int)var1.get(player, Registry.STRENGTH)), 14F, 81F, var2 -> {
					var2.add(new TranslationTextComponent("desc.strength0", TextFormatting.GRAY));
					var2.add(new TranslationTextComponent("desc.strength1", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.strength2", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.strength3", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.strength4", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.strength5", TextFormatting.GRAY));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.dexterity", (int)var1.get(player, Registry.DEXTERITY)), 14F, 99F, var2 -> {
					var2.add(new TranslationTextComponent("desc.dexterity0", TextFormatting.GRAY));
					var2.add(new TranslationTextComponent("desc.dexterity1", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.dexterity2", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.dexterity3", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.dexterity4", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.dexterity5", TextFormatting.GRAY));
					var2.add(new TranslationTextComponent("desc.dexterity6", TextFormatting.GRAY));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.intelligence", (int)var1.get(player, Registry.INTELLIGENCE)), 14F, 117F, var2 -> {
					var2.add(new TranslationTextComponent("desc.intelligence0", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.intelligence1", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.intelligence2", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.intelligence3", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.intelligence4", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.intelligence5", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.intelligence6", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.intelligence7", TextFormatting.GRAY, "%"));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.luck", (int)var1.get(player, Registry.LUCK)), 14F, 135F, var2 -> {
					var2.add(new TranslationTextComponent("desc.luck0", TextFormatting.GRAY));
					var2.add(new TranslationTextComponent("desc.luck1", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.luck2", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.luck3", TextFormatting.GRAY, "%"));
					var2.add(new TranslationTextComponent("desc.luck4", TextFormatting.GRAY, "%"));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.health", text.format(player.getHealth()), text.format(player.getMaxHealth())), 84F, 36F, var2 -> {
					var2.add(new TranslationTextComponent("desc.health0", TextFormatting.GRAY));
					var2.add(new StringTextComponent(""));
					var2.add(new TranslationTextComponent("desc.health1", TextFormatting.GRAY, text.format(20D * var1.get(player, Registry.HEALTH_REGEN))));
					var2.add(new TranslationTextComponent("desc.health2", TextFormatting.GRAY, text.format(100D * var1.get(player, Registry.HEALTH_REGEN_AMP)), "%"));
				}));
				
				String var3 = text.format(player.getAttribute(SharedMonsterAttributes.ARMOR).getValue()) + " (+" + text.format(player.getAttribute(SharedMonsterAttributes.ARMOR).getValue() - player.getAttribute(SharedMonsterAttributes.ARMOR).getBaseValue()) + ")";
				String var4 = text.format(player.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue()) + " (+" + text.format(player.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue() - player.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue()) + ")";
				
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.armour", var3), 84F, 45F, var2 -> {
					var2.add(new TranslationTextComponent("desc.armour0", TextFormatting.GRAY));
					var2.add(new StringTextComponent(""));
					var2.add(new TranslationTextComponent("desc.armour1", TextFormatting.GRAY, var4));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.knockback", text.format(100F * var1.get(player, Registry.KNOCKBACK_RESISTANCE)), "%"), 84F, 54F, var2 -> {
					var2.add(new TranslationTextComponent("attribute.resistance", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.DAMAGE_RESISTANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.fire", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.FIRE_RESISTANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.lava", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.LAVA_RESISTANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.explosion", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.EXPLOSION_RESISTANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.falling", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.FALL_RESISTANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.poison", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.POISON_RESISTANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.wither", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.WITHER_RESISTANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.drowning", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.DROWNING_RESISTANCE)), "%"));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.meleedamage", text.format(var1.get(player, Registry.MELEE_DAMAGE))), 84F, 72F, var2 -> {
					var2.add(new TranslationTextComponent("attribute.meleecrit", TextFormatting.GRAY, text.format(100F + (100F * var1.get(player, Registry.MELEE_CRIT_DAMAGE))), "%"));
					var2.add(new TranslationTextComponent("attribute.meleecritchance", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.MELEE_CRIT_CHANCE)), "%"));
					var2.add(new TranslationTextComponent("attribute.attackspeed", TextFormatting.GRAY, text.format(var1.get(player, Registry.ATTACK_SPEED))));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.rangeddamage", text.format(var1.get(player, Registry.RANGED_DAMAGE))), 84F, 81F, var2 -> {
					var2.add(new TranslationTextComponent("attribute.rangedcrit", TextFormatting.GRAY, text.format(100F + (100F * var1.get(player, Registry.RANGED_CRIT_DAMAGE))), "%"));
					var2.add(new TranslationTextComponent("attribute.rangedcritchance", TextFormatting.GRAY, text.format(100F * var1.get(player, Registry.RANGED_CRIT_CHANCE)), "%"));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.evasion", text.format(100D * var1.get(player, Registry.EVASION_CHANCE)), "%"), 84F, 90F, var2 -> {
					var2.add(new TranslationTextComponent("desc.evasion0", TextFormatting.GRAY));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.lifesteal", text.format(100D * var1.get(player, Registry.LIFESTEAL)), "%"), 84F, 108F, var2 -> {
					var2.add(new TranslationTextComponent("desc.lifesteal0", TextFormatting.GRAY));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.luckiness", var1.get(player, Registry.LUCKINESS)), 84F, 126F, var2 -> {
					var2.add(new TranslationTextComponent("desc.luckiness0", TextFormatting.GRAY));
				}));
				var0.add(new TextDisplayPanel(new TranslationTextComponent("attribute.movement", text.format(100D * var1.get(player, Registry.MOVEMENT_SPEED_AMP)), "%"), 84F, 144F, var2 -> {
					var2.add(new TranslationTextComponent("desc.movement0", TextFormatting.GRAY));
				}));
			});
		});
		
		float var0 = 0.75F;
		
		GL11.glPushMatrix();
		GL11.glScalef(var0, var0, var0);
		
		for(TextDisplayPanel var : display) {
			this.font.drawString(var.getDisplayText(), var.posX() / var0, var.posY() / var0, 4210752);
		}
		
		GL11.glPopMatrix();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par0, int par1, int par2) {
		int var0 = this.guiLeft;
		int var1 = (this.height - this.ySize) / 2;
		
		this.minecraft.getTextureManager().bindTexture(GUI);
		this.blit(var0, var1, 0, 0, this.xSize, this.ySize);
		this.buttons.forEach(var -> var.render(par1, par2, par0));
		
		Registry.ELEMENTS.apply(player).ifPresent(var -> {
			for(TexturedButton var2 : this.attribute_button) {
				var2.active = (int)var.get(player, Registry.SKILLPOINTS) > 0;
			}
		});
	}
	
	@Override
	protected void renderHoveredToolTip(int par0, int par1) {
		super.renderHoveredToolTip(par0, par1);
		
		for(TextDisplayPanel var : display) {
			if(isMouseOver(par0, par1, (int)var.posX(), (int)var.posY(), (int)(this.font.getStringWidth(var.getDisplayText()) * 0.75F), 7)) {
				renderTooltip(var.getHoverText().stream().map(ITextComponent::getFormattedText).collect(Collectors.toList()), par0, par1);
			}
		}
	}
	
	@Override
	protected void init() {
		this.buttons.clear();
		super.init();
		
		for(int var0 = 0; var0 < 5; var0++) {
			this.addButton(this.attribute_button[var0] = new TexturedButton(this, 2, 61 + (var0 * 18), 11, 10, 204, 0, var0, (var1, var2) -> {
				IElement[] var3 = new IElement[] {Registry.CONSTITUTION, Registry.STRENGTH, Registry.DEXTERITY, Registry.INTELLIGENCE, Registry.LUCK};
				CompoundNBT var4 = Util.fromElements(BiValue.make(var3[var2], 1D), BiValue.make(Registry.SKILLPOINTS, -1D));
				
				Registry.NETWORK.sendToServer(new AddPlayerElement(var4));
			}));
		}
		
		this.addButton(new TexturedButton(this, 155, 7, 14, 13, 190, 0, 0, (var0, var1) -> {
			Registry.NETWORK.sendToServer(new SwitchScreens(true));
			InventoryScreen var2 = new InventoryScreen(Minecraft.getInstance().player);
			Minecraft.getInstance().displayGuiScreen(var2);
		}));
	}
}
