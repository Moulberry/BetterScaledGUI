package io.github.moulberry.betterscaledgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class ScaledResolutionOverride {

    //What the current GUI is using. This is updated whenever a new GUI is opened
    // this is to ensure that the current scale and the GuiScreen#width/height fields
    // are consistent
    private static int currentScaleOverride = -1;

    //What we want the GUI scale to be, currentScaleOverride is set to this by
    // GuiScreen#setWorldAndRenderer
    private static int desiredScaleOverride = -1;

    //Scaling override for ScaledResolution. Should only be set to currentScaleOverride
    // during rendering
    private static int scaleOverride = -1;

    public static int getScaleOverride() {
        return scaleOverride;
    }

    public static void setScaleOverride(int scaleOverride) {
        ScaledResolutionOverride.scaleOverride = scaleOverride;
    }

    public static int getCurrentScaleOverride() {
        return currentScaleOverride;
    }

    public static void setCurrentScaleOverride(int currentScaleOverride) {
        ScaledResolutionOverride.currentScaleOverride = currentScaleOverride;
    }

    public static int getDesiredScaleOverride() {
        return desiredScaleOverride;
    }

    public static void setDesiredScaleOverride(int desiredScaleOverride) {
        ScaledResolutionOverride.desiredScaleOverride = desiredScaleOverride;
    }

}
