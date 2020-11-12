package io.github.moulberry.betterscaledgui.mixins;

import io.github.moulberry.betterscaledgui.ScaledResolutionOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

@Mixin(ForgeHooksClient.class)
public class MixinForgeHooksClient {

    private static final FloatBuffer projectionMatrixOld = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer modelviewMatrixOld = BufferUtils.createFloatBuffer(16);

    @Inject(method = "drawScreen", at=@At("HEAD"), remap = false)
    private static void drawScreen_HEAD(GuiScreen screen, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        //Don't apply tweak to main menu and such
        if(Minecraft.getMinecraft().thePlayer != null &&
                screen instanceof GuiContainer) {
            //Save current matrices. This is preferred to simply resetting the scaling
            // since other mods may want to interact with scaling in weird ways
            GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrixOld);
            GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelviewMatrixOld);

            ScaledResolutionOverride.setScaleOverride(ScaledResolutionOverride.getCurrentScaleOverride());

            //Get the new scaled resolution
            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

            //Set the viewport and proj/mv matrices, this is where the magic happens
            GlStateManager.viewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D,
                    scaledresolution.getScaledWidth_double(),
                    scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        }
    }

    @Inject(method = "drawScreen", at=@At("RETURN"), remap = false)
    private static void drawScreen_RETURN(GuiScreen screen, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        //Don't apply tweak to main menu and such
        if(Minecraft.getMinecraft().thePlayer != null &&
                screen instanceof GuiContainer) {
            //Disable the scale override
            ScaledResolutionOverride.setScaleOverride(-1);

            //Load old matrices
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GL11.glLoadMatrix(projectionMatrixOld);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadMatrix(modelviewMatrixOld);
        }
    }

    @ModifyVariable(method="drawScreen", ordinal = 0, at=@At(value = "HEAD"), remap = false)
    private static int drawScreen_mouseX(int mouseX) {
        //Don't apply tweak to main menu and such
        if(Minecraft.getMinecraft().thePlayer != null &&
                Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
            ScaledResolutionOverride.setScaleOverride(ScaledResolutionOverride.getCurrentScaleOverride());

            //Get new mouseX
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int width = scaledResolution.getScaledWidth();
            mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth;
        }

        return mouseX;
    }

    @ModifyVariable(method="drawScreen", ordinal = 1, at=@At(value = "HEAD"), remap = false)
    private static int drawScreen_mouseY(int mouseY) {
        //Don't apply tweak to main menu and such
        if(Minecraft.getMinecraft().thePlayer != null &&
                Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
            ScaledResolutionOverride.setScaleOverride(ScaledResolutionOverride.getCurrentScaleOverride());

            //Get new mouseY
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int height = scaledResolution.getScaledHeight();
            mouseY = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1;
        }

        return mouseY;
    }

}
