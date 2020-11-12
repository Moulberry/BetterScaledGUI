package io.github.moulberry.betterscaledgui.mixins;

import io.github.moulberry.betterscaledgui.ScaledResolutionOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.nio.FloatBuffer;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @ModifyVariable(method="setWorldAndResolution", ordinal = 0, at=@At(value = "HEAD"))
    public int setWorldAndResolution_width(int width) {
        //Don't apply tweak to main menu and such
        if(Minecraft.getMinecraft().thePlayer != null && ((Object)this) instanceof GuiContainer) {
            int desiredScale = ScaledResolutionOverride.getDesiredScaleOverride();

            ScaledResolutionOverride.setCurrentScaleOverride(desiredScale);

            ScaledResolutionOverride.setScaleOverride(desiredScale);
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            ScaledResolutionOverride.setScaleOverride(-1);

            return scaledResolution.getScaledWidth();
        }

        return width;
    }

    @ModifyVariable(method="setWorldAndResolution", ordinal = 1, at=@At(value = "HEAD"))
    public int setWorldAndResolution_height(int height) {
        //Don't apply tweak to main menu and such
        if(Minecraft.getMinecraft().thePlayer != null && ((Object)this) instanceof GuiContainer) {
            int desiredScale = ScaledResolutionOverride.getDesiredScaleOverride();

            ScaledResolutionOverride.setCurrentScaleOverride(desiredScale);

            ScaledResolutionOverride.setScaleOverride(desiredScale);
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            ScaledResolutionOverride.setScaleOverride(-1);

            return scaledResolution.getScaledHeight();
        }

        return height;
    }

    //The following is not needed in vanilla, but is included in order to improve mod compatibility

    @Inject(method = "handleInput", at=@At("HEAD"))
    public void handleInput_HEAD(CallbackInfo ci) {
        //Don't apply tweak to main menu and such
        if(Minecraft.getMinecraft().thePlayer != null &&
                ((Object)this) instanceof GuiContainer) {
            ScaledResolutionOverride.setScaleOverride(ScaledResolutionOverride.getCurrentScaleOverride());
        }
    }

    @Inject(method = "handleInput", at=@At("RETURN"))
    public void handleInput_RETURN(CallbackInfo ci) {
        //Disable the scale override
        ScaledResolutionOverride.setScaleOverride(-1);
    }

}
