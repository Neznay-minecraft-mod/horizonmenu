package com.horizonmenu.mixin;

import com.horizonmenu.Background;
import com.horizonmenu.Config;
import com.horizonmenu.HorizonTitleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Replaces the vanilla background on every menu screen shown outside a world (unless "panorama" is chosen). */
@Mixin(Screen.class)
public abstract class ScreenBackgroundMixin {
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true, require = 0)
    private void horizonmenu$background(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Config c = Config.get();
        if (!c.enabled || !c.allScreens) return;
        if (Minecraft.getInstance().level != null) return;
        if ((Object) this instanceof HorizonTitleScreen) return;
        String id = Background.activeId(c);
        if (Background.isPanorama(id)) return; // vanilla panorama
        Background.draw(g, this.width, this.height, Background.resolve(id), c);
        g.fill(0, 0, this.width, this.height, 0x55000000);
        ci.cancel();
    }
}
