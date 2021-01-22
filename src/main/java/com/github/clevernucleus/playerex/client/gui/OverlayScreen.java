package com.github.clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

/**
 * Overlay screen object.
 */
public class OverlayScreen extends AbstractGui {
	private final BiFunction<String, Double, String> format = (par0, par1) -> (new DecimalFormat(par0)).format(par1);
	private Supplier<Minecraft> mc;
	
	public OverlayScreen(Supplier<Minecraft> par0) {
		this.mc = par0;
	}
	
	/**
	 * @param par0 The player.
	 * @return The y-location of the health bar that should be rendered.
	 */
	private int healthLat(final PlayerEntity par0) {
		if(par0.isPotionActive(Effects.WITHER)) return 210;
		if(par0.isPotionActive(Effects.POISON)) return 202;
		if(par0.isPotionActive(Effects.ABSORPTION)) return 194;
		
		return 186;
	}
	
	/**
	 * Draws this screen object.
	 * @param par0 Is pre-text? True to draw before text, false to draw text/after text.
	 */
	public void draw(MatrixStack par0, final boolean par1) {
		if(this.mc.get() == null) return;
		
		ClientPlayerEntity var0 = this.mc.get().player;
		
		if(var0 == null) return;
		
		MainWindow var1 = this.mc.get().getMainWindow();
		
		int varX = var1.getScaledWidth();
		int varY = var1.getScaledHeight();
		
		if(par1) {
			int var2 = (int)(78F / var0.getMaxHealth() * var0.getHealth());
			
			this.mc.get().getTextureManager().bindTexture(PlayerAttributesScreen.GUI);
			this.blit(par0, (varX / 2) - 91, varY - 37, 0, 178, 78, 8);
			this.blit(par0, (varX / 2) - 91, varY - 37, 0, healthLat(var0), var2, 8);
			this.blit(par0, (varX / 2) - 91, varY - 27, 0, 166, 182, 3);
			
			ExAPI.playerAttributes(var0).ifPresent(var -> {
				int var3 = (int)(182F * var.expCoeff(var0));
				
				if(Screen.hasAltDown()) {
					this.blit(par0, (varX / 2) - 91, varY - 27, 0, 169, var3, 3);
				} else {
					int var4 = var0.xpBarCap();
					
					if(var4 > 0) {
						int var5 = (int)(var0.experience * 183.0F);
						
						if(var5 > 0) {
							this.blit(par0, (varX / 2) - 91, varY - 27, 0, 172, var5, 3);
						}
					}
				}
			});
			
			if(var0.isRidingHorse()) {
				Entity var3 = var0.getRidingEntity();
				
				if(var3 instanceof LivingEntity) {
					LivingEntity var4 = (LivingEntity)var3;
					
					float var5 = var0.getHorseJumpPower();
					int var6 = (int)(var5 * 183.0F);
					int var7 = (int)(78F / var4.getMaxHealth() * var4.getHealth());
					
					this.blit(par0, (varX / 2) - 91, varY - 46, 0, 178, 78, 8);
					this.blit(par0, (varX / 2) - 91, varY - 46, 0, 186, var7, 8);
					
					if(var6 > 0) {
						this.blit(par0, (varX / 2) - 91, varY - 27, 0, 175, var6, 3);
					}
				}
			}
			
			this.mc.get().getTextureManager().bindTexture(GUI_ICONS_LOCATION);
			
			boolean var3 = var0.isPotionActive(Effects.HUNGER);
			int var4 = (int)(100F * Math.max((float)var0.getAir(), 0F) / (float)var0.getMaxAir());
			
			this.blit(par0, (varX / 2) + 12, varY - 38, var3 ? 133 : 16, 27, 9, 9);
			this.blit(par0, (varX / 2) + 12, varY - 38, var3 ? 88 : 52, 27, 9, 9);
			this.blit(par0, (varX / 2) + (var4 < 100 ? 44 : 50), varY - 38, 34, 9, 9, 9);//+44 min left, +50 max left
			
			ExAPI.playerAttributes(var0).ifPresent(var -> {
				int p = Math.round((float)var.get(var0, PlayerAttributes.ARMOR));
				
				if(var4 < 100) {
					this.blit(par0, (varX / 2) + (p < 10 ? 66 : (p < 100 ? 70 : 76)), varY - 38, 16, 18, 9, 9);//66 min, 70 med, 
				}
			});
		} else {
			FontRenderer var2 = this.mc.get().fontRenderer;
			String var3 = this.format.apply("#.##", (double)var0.getHealth() + var0.getAbsorptionAmount()) + "/" + this.format.apply("#.##", (double)var0.getMaxHealth());
			ExAPI.playerAttributes(var0).ifPresent(var -> {
				int p = Math.round((float)var.get(var0, PlayerAttributes.ARMOR));
				
				String var4 = "x" + p;
				String var5 = (int)(100F * (float)var0.getFoodStats().getFoodLevel() / 20F) + "%";
				int var6 = (int)(100F * Math.max((float)var0.getAir(), 0F) / (float)var0.getMaxAir());
				int var7 = (varX - var2.getStringWidth(var3)) / 2;
				
				GL11.glPushMatrix();
				GL11.glScalef(0.8F, 0.8F, 0.8F);
				
				var2.drawString(par0, var3, 1.25F * (var7 - 48), 1.25F * (varY - 36F), 0xFFFFFF);
				var2.drawString(par0, var4, 1.25F * ((varX / 2) + (var6 < 100 ? 54 : 60)), 1.25F * (varY - 36F), 0xFFFFFF);//+54 min left, + 60 max left
				var2.drawString(par0, var5, 1.25F * ((varX / 2) + 22), 1.25F * (varY - 36F), 0xFFFFFF);
				
				if(var6 < 100) {
					var2.drawString(par0, var6 + "%", 1.25F * ((varX / 2) + (p < 10 ? 76 : (p < 100 ? 80 : 86))), 1.25F * (varY - 36F), 0xFFFFFF);//76 min, 80 med, 
				}
				
				GL11.glPopMatrix();
			});
			if(var0.isRidingHorse()) {
				Entity var8 = var0.getRidingEntity();
				
				if(var8 instanceof LivingEntity) {
					LivingEntity var9 = (LivingEntity)var8;
					String var10 = this.format.apply("#.##", (double)var9.getHealth()) + "/" + this.format.apply("#.##", (double)var9.getMaxHealth());
					int var11 = (varX - var2.getStringWidth(var10)) / 2;
					
					GL11.glPushMatrix();
					GL11.glScalef(0.8F, 0.8F, 0.8F);
					
					var2.drawString(par0, var10, 1.25F * (var11 - 48), 1.25F * (varY - 45F), 0xFFFFFF);
					
					GL11.glPopMatrix();
				}
			}
			
			ExAPI.playerAttributes(var0).ifPresent(var -> {
				int var8 = 0, var9 = 0, var10 = varY - 36;
				
				if(Screen.hasAltDown()) {
					var8 = (int)var.get(var0, PlayerAttributes.LEVEL);
					var9 = 16759296;
				} else {
					var8 = var0.experienceLevel;
					var9 = 8453920;
				}
				
				if(var8 <= 0) return;
				
				String var11 = "" + var8;
				int var12 = (varX - var2.getStringWidth(var11)) / 2;
				
				var2.drawString(par0, var11, (float)(var12 + 1), (float)var10, 0);
				var2.drawString(par0, var11, (float)(var12 - 1), (float)var10, 0);
				var2.drawString(par0, var11, (float)var12, (float)(var10 + 1), 0);
				var2.drawString(par0, var11, (float)var12, (float)(var10 - 1), 0);
				var2.drawString(par0, var11, (float)var12, (float)var10, var9);
			});
		}
	}
}