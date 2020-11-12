package io.github.moulberry.betterscaledgui.mixins;

import io.github.moulberry.betterscaledgui.ScaledResolutionOverride;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScaledResolution.class)
public class MixinScaledResolution {

    @Redirect(method="<init>",
        at=@At(
            value="FIELD",
            target="Lnet/minecraft/client/settings/GameSettings;guiScale:I",
            opcode = Opcodes.GETFIELD
        ))
    public int modifyGuiScale(GameSettings settings) {
        int scale = ScaledResolutionOverride.getScaleOverride();
        return scale >= 0 ? scale : settings.guiScale;
    }

}
