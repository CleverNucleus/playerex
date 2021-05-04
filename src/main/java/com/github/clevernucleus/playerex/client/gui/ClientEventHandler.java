package com.github.clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.function.BiFunction;

import org.lwjgl.opengl.GL11;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import com.github.clevernucleus.playerex.client.ClientConfig;
import com.github.clevernucleus.playerex.client.ClientRegistry;
import com.github.clevernucleus.playerex.init.Registry;
import com.github.clevernucleus.playerex.init.container.SwitchScreens;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
	private static final Set<ElementType> UTILS_BAR = Sets.immutableEnumSet(ElementType.EXPERIENCE, ElementType.JUMPBAR, ElementType.ARMOR, ElementType.FOOD, ElementType.AIR);
	private static final Set<ElementType> HEALTH_BAR = Sets.immutableEnumSet(ElementType.HEALTH, ElementType.HEALTHMOUNT);
	private static final BiFunction<String, Float, String> FORMAT = (par0, par1) -> (new DecimalFormat(par0)).format(par1);
	
	private static boolean isFoodItem(ItemStack par0) {
		return par0.getItem().getFood() != null;
	}
	
	private static boolean isRotten(ItemStack par0) {
		if(!isFoodItem(par0)) return false;
		
		for(Pair<EffectInstance, Float> var : par0.getItem().getFood().getEffects()) {
			if(var.getFirst() != null && var.getFirst().getPotion() != null && var.getFirst().getPotion().getEffectType() == EffectType.HARMFUL) return true;
		}
		
		return false;
	}
	
	private static int healthLat(final PlayerEntity par0) {
		if(par0.isPotionActive(Effects.WITHER)) return 213;
		if(par0.isPotionActive(Effects.POISON)) return 205;
		if(par0.isPotionActive(Effects.ABSORPTION)) return 197;
		
		return 189;
	}
	
	private static void drawHealthBar(MatrixStack par0, Minecraft par1, boolean par2) {
		if(par1 == null) return;
		
		ClientPlayerEntity var0 = par1.player;
		
		if(var0 == null) return;
		
		MainWindow var1 = par1.getMainWindow();
		int varX = var1.getScaledWidth();
		int varY = var1.getScaledHeight();
		
		if(par2) {
			int var2 = (int)(78F / var0.getMaxHealth() * var0.getHealth());
			
			par1.getTextureManager().bindTexture(PlayerAttributesScreen.GUI);
			par1.ingameGUI.blit(par0, (varX / 2) - 91, varY - 37, 0, 181, 78, 8);
			par1.ingameGUI.blit(par0, (varX / 2) - 91, varY - 37, 0, healthLat(var0), var2, 8);
			
			return;
		}
		
		FontRenderer var2 = par1.fontRenderer;
		String var3 = FORMAT.apply("#.##", var0.getHealth() + var0.getAbsorptionAmount()) + "/" + FORMAT.apply("#.##", var0.getMaxHealth());
		
		int var4 = (varX - var2.getStringWidth(var3)) / 2;
		
		GL11.glPushMatrix();
		GL11.glScalef(0.8F, 0.8F, 0.8F);
		
		var2.drawString(par0, var3, 1.25F * (var4 - 48), 1.25F * (varY - 36F), 0xFFFFFF);
		
		GL11.glPopMatrix();
	}
	
	//assumes we;re riding a horse
	private static void drawHorseHealthBar(MatrixStack par0, Minecraft par1, boolean par2) {
		if(par1 == null) return;
		
		ClientPlayerEntity var0 = par1.player;
		
		if(var0 == null) return;
		
		MainWindow var1 = par1.getMainWindow();
		int varX = var1.getScaledWidth();
		int varY = var1.getScaledHeight();
		
		Entity var2 = var0.getRidingEntity();
		
		if(var2 instanceof LivingEntity) {
			LivingEntity var3 = (LivingEntity)var2;
			
			if(par2) {
				int var4 = (int)(78F / var3.getMaxHealth() * var3.getHealth());
				
				par1.getTextureManager().bindTexture(PlayerAttributesScreen.GUI);
				par1.ingameGUI.blit(par0, (varX / 2) + 13, varY - 37, 0, 181, 78, 8);
				par1.ingameGUI.blit(par0, (varX / 2) + 13, varY - 37, 0, 189, var4, 8);
				
				return;
			}
			
			FontRenderer var4 = par1.fontRenderer;
			String var5 = FORMAT.apply("#.##", var0.getHealth() + var0.getAbsorptionAmount()) + "/" + FORMAT.apply("#.##", var0.getMaxHealth());
			
			int var6 = (varX - var4.getStringWidth(var5)) / 2;
			
			GL11.glPushMatrix();
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			
			var4.drawString(par0, var5, 1.25F * (var6 + 55), 1.25F * (varY - 36F), 0xFFFFFF);
			
			GL11.glPopMatrix();
		}
	}
	
	//assumes we're riding a horse
	private static void drawHorseJumpBar(MatrixStack par0, Minecraft par1) {
		if(par1 == null) return;
		
		ClientPlayerEntity var0 = par1.player;
		
		if(var0 == null) return;
		
		MainWindow var1 = par1.getMainWindow();
		int varX = var1.getScaledWidth();
		int varY = var1.getScaledHeight();
		
		Entity var2 = var0.getRidingEntity();
		
		par1.getTextureManager().bindTexture(PlayerAttributesScreen.GUI);
		par1.ingameGUI.blit(par0, (varX / 2) - 91, varY - 27, 0, 175, 182, 3);
		
		if(var2 instanceof LivingEntity) {
			float var3 = var0.getHorseJumpPower();
			int var4 = (int)(var3 * 183.0F);
			
			if(var4 > 0) {
				par1.ingameGUI.blit(par0, (varX / 2) - 91, varY - 27, 0, 178, var4, 3);
			}
		}
	}
	
	private static void drawLevelBar(MatrixStack par0, Minecraft par1, boolean par2) {
		if(par1 == null) return;
		
		ClientPlayerEntity var0 = par1.player;
		
		if(var0 == null) return;
		
		MainWindow var1 = par1.getMainWindow();
		int varX = var1.getScaledWidth();
		int varY = var1.getScaledHeight();
		
		if(par2) {
			par1.getTextureManager().bindTexture(PlayerAttributesScreen.GUI);
			par1.ingameGUI.blit(par0, (varX / 2) - 91, varY - 27, 0, 166, 182, 3);
			
			ExAPI.playerAttributes(var0).ifPresent(var -> {
				int var2 = 0, var3 = 166;
				
				if(ClientRegistry.HUD.isKeyDown()) {
					var2 = (int)(182F * var.expCoeff(var0));
					var3 = 169;
				} else {
					int var4 = var0.xpBarCap();
					
					if(var4 > 0) {
						int var5 = (int)(var0.experience * 183.0F);
						
						if(var5 > 0) {
							var2 = var5;
							var3 = 172;
						}
					}
				}
				
				par1.ingameGUI.blit(par0, (varX / 2) - 91, varY - 27, 0, var3, var2, 3);
			});
			
			return;
		}
		
		FontRenderer var2 = par1.fontRenderer;
		ExAPI.playerAttributes(var0).ifPresent(var -> {
			int var3 = 0, var4 = 0, var5 = varY - 36;
			
			if(ClientRegistry.HUD.isKeyDown()) {
				var3 = (int)var.get(var0, PlayerAttributes.LEVEL);
				var4 = 16759296;
			} else {
				var3 = var0.experienceLevel;
				var4 = 8453920;
			}
			
			if(var3 <= 0) return;
			
			String var6 = "" + var3;
			int var7 = (varX - var2.getStringWidth(var6)) / 2;
			
			var2.drawString(par0, var6, (float)(var7 + 1), (float)var5, 0);
			var2.drawString(par0, var6, (float)(var7 - 1), (float)var5, 0);
			var2.drawString(par0, var6, (float)var7, (float)(var5 + 1), 0);
			var2.drawString(par0, var6, (float)var7, (float)(var5 - 1), 0);
			var2.drawString(par0, var6, (float)var7, (float)var5, var4);
		});
	}
	
	//assumes we're not riding a horse
	private static void drawUtilsBar(MatrixStack par0, Minecraft par1, boolean par2) {
		if(par1 == null) return;
		
		ClientPlayerEntity var0 = par1.player;
		
		if(var0 == null) return;
		
		MainWindow var1 = par1.getMainWindow();
		int varX = var1.getScaledWidth();
		int varY = var1.getScaledHeight();
		int var2 = var0.getFoodStats().getFoodLevel();
		int var3 = (int)(100F * Math.max((float)var0.getAir(), 0F) / (float)var0.getMaxAir());
		float var4 = var0.getFoodStats().getSaturationLevel();
		boolean var5 = var0.isPotionActive(Effects.HUNGER);
		boolean var6 = ClientConfig.CLIENT.enableFoodInfo.get().booleanValue();
		
		ItemStack var7 = var0.getHeldItemMainhand();
		ItemStack var8 = var0.getHeldItemOffhand();
		
		final int var9 = isFoodItem(var7) ? var7.getItem().getFood().getHealing() : (isFoodItem(var8) ? var8.getItem().getFood().getHealing() : 0);
		final float var10 = isFoodItem(var7) ? var7.getItem().getFood().getSaturation() : (isFoodItem(var8) ? var8.getItem().getFood().getSaturation() : 0F);
		final boolean var11 = isFoodItem(var7) ? isRotten(var7) : (isFoodItem(var8) ? isRotten(var8) : false);
		
		if(par2) {
			boolean var12 = var5 || (var9 > 0 && var2 < 20 && var11 && var6);
			
			par1.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
			par1.ingameGUI.blit(par0, (varX / 2) + 12, varY - 38, var12 ? 133 : 16, 27, 9, 9);
			par1.ingameGUI.blit(par0, (varX / 2) + 12, varY - 38, var12 ? 88 : 52, 27, 9, 9);
			par1.ingameGUI.blit(par0, (varX / 2) + (var3 < 100 ? 44 : 50), varY - 38, 34, 9, 9, 9);
			
			ExAPI.playerAttributes(var0).ifPresent(var -> {
				int var13 = Math.round((float)var.get(var0, PlayerAttributes.ARMOR));
				
				if(var3 < 100) {
					par1.ingameGUI.blit(par0, (varX / 2) + (var13 < 10 ? 66 : (var13 < 100 ? 70 : 76)), varY - 38, 16, 18, 9, 9);
				}
			});
			
			par1.getTextureManager().bindTexture(PlayerAttributesScreen.GUI);
			
			if(ClientRegistry.HUD.isKeyDown() && var6) {
				par1.ingameGUI.blit(par0, (varX / 2) + 12, varY - 38, 215, 0, 9, 9);
			}
			
			return;
		}
		
		FontRenderer var12 = par1.fontRenderer;
		ExAPI.playerAttributes(var0).ifPresent(var -> {
			int var13 = Math.round((float)var.get(var0, PlayerAttributes.ARMOR));
			
			int var14 = Math.min(var2 + var9, 20);
			float var15 = Math.min(var14, var4 + (10F * var10));
			float var16 = Math.min(var14, var4);
			boolean var17 = var9 > 0 && var2 < 20;
			boolean var18 = var10 > 0F && var4 < 20F;
			
			String var19 = "x" + var13;
			String var20 = (int)(100F * ((ClientRegistry.HUD.isKeyDown() && var6) ? (((var17 && var18) ? var15 : var16) / (float)var14) : (float)(var17 ? var14 : var2) / 20F)) + "%";
			int var21 = (int)(100F * Math.max((float)var0.getAir(), 0F) / (float)var0.getMaxAir());
			int var22 = (int)((System.currentTimeMillis() / 50L) % 20L);
			int var23 = (int)((float)((255 * Math.sin(Math.toRadians(18 * var22))) + 255F) / 2F);
			
			GL11.glPushMatrix();
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			
			var12.drawString(par0, var19, 1.25F * ((varX / 2) + (var21 < 100 ? 54 : 60)), 1.25F * (varY - 36F), 0xFFFFFF);//+54 min left, + 60 max left
			
			if(var17) {
				GlStateManager.enableBlend();
				
				if(var23 > 8) {
					int var24 = 0xFFFFFF;
					int var25 = var23 << 24 & -var24;
					
					var12.func_243248_b(par0, new StringTextComponent(var20), 1.25F * ((varX / 2) + 22), 1.25F * (varY - 36F), var24 | var25);
				}
				
				GlStateManager.disableBlend();
			} else {
				var12.drawString(par0, var20, 1.25F * ((varX / 2) + 22), 1.25F * (varY - 36F), 0xFFFFFF);
			}
			
			if(var21 < 100) {
				var12.drawString(par0, var21 + "%", 1.25F * ((varX / 2) + (var13 < 10 ? 76 : (var13 < 100 ? 80 : 86))), 1.25F * (varY - 36F), 0xFFFFFF);
			}
			
			GL11.glPopMatrix();
		});
	}
	
	/**
	 * Event for adding to a gui container.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onGuiInitPost(final net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post par0) {
		Screen var0 = par0.getGui();
		
		if(var0 instanceof InventoryScreen) {
			ContainerScreen<?> var1 = (ContainerScreen<?>)var0;
			
			if(par0.getWidgetList() != null) {
				par0.addWidget(new TexturedButton(var1, ClientConfig.CLIENT.guiButtonX.get().intValue(), ClientConfig.CLIENT.guiButtonY.get().intValue(), 14, 13, 176, 0, 0, (var2, var3) -> {
					if(var2 instanceof InventoryScreen) {
						Registry.NETWORK.sendToServer(new SwitchScreens(false));
					}
				}));
			}
		}
	}
	
	/**
	 * Event for adding ingame HUD elements (pre-render).
	 * @param par0
	 */
	@SubscribeEvent
	public static void onHUDRenderPre(final net.minecraftforge.client.event.RenderGameOverlayEvent.Pre par0) {
		if(!ClientConfig.CLIENT.enableHUD.get().booleanValue()) return;
		
		ElementType var0 = par0.getType();
		MatrixStack var1 = par0.getMatrixStack();
		ClientPlayerEntity var2 = Minecraft.getInstance().player;
		
		if(UTILS_BAR.contains(var0)) {
			par0.setCanceled(true);
		}
		
		if(var0 == ElementType.HOTBAR) return;
		if(var2.isCreative() || var2.isSpectator()) return;
		
		if(var2.isRidingHorse()) {
			drawHorseJumpBar(var1, Minecraft.getInstance());
		} else {
			drawLevelBar(var1, Minecraft.getInstance(), true);
			drawUtilsBar(var1, Minecraft.getInstance(), true);
		}
		
		if(ClientConfig.CLIENT.enableHealthBar.get().booleanValue()) {
			if(HEALTH_BAR.contains(var0)) {
				par0.setCanceled(true);
			}
			
			drawHealthBar(var1, Minecraft.getInstance(), true);
			
			if(var2.isRidingHorse()) {
				drawHorseHealthBar(var1, Minecraft.getInstance(), true);
			}
		}
	}
	
	/**
	 * Event for adding ingame HUD elements (post-render).
	 * @param par0
	 */
	@SubscribeEvent
	public static void onHUDRenderPost(final net.minecraftforge.client.event.RenderGameOverlayEvent.Post par0) {
		if(!ClientConfig.CLIENT.enableHUD.get().booleanValue()) return;
		
		MatrixStack var0 = par0.getMatrixStack();
		ClientPlayerEntity var1 = Minecraft.getInstance().player;
		
		if(par0.getType() == ElementType.HOTBAR) return;
		if(var1.isCreative() || var1.isSpectator()) return;
		
		if(!var1.isRidingHorse()) {
			drawLevelBar(var0, Minecraft.getInstance(), false);
			drawUtilsBar(var0, Minecraft.getInstance(), false);
		}
		
		if(ClientConfig.CLIENT.enableHealthBar.get().booleanValue()) {
			drawHealthBar(var0, Minecraft.getInstance(), false);
			
			if(var1.isRidingHorse()) {
				drawHorseHealthBar(var0, Minecraft.getInstance(), false);
			}
		}
	}
}
