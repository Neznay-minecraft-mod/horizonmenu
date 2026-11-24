package com.horizonmenu.mixin;

import com.horizonmenu.Nav;
import com.horizonmenu.SettingsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.screens.options.OptionsScreen")
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"), require = 0)
    private void horizonmenu$addSettingsButton(CallbackInfo ci) {
        int w = 130;
        int x = Math.max(4, this.width / 2 - 100 - 6 - w);
        int y = this.height - 27;
        this.addRenderableWidget(
                Button.builder(Component.literal("HorizonMenu"), b -> Nav.open(new SettingsScreen(this)))
                        .bounds(x, y, w, 20)
                        .build());
    }
}
