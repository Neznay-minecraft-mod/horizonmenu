package com.horizonmenu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorizonMenuClient implements ClientModInitializer {
    public static final String ID = "horizonmenu";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitializeClient() {
        Config.load();
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
        LOGGER.info("HorizonMenu loaded");
    }

    private void onTick(Minecraft mc) {
        if (!Config.get().enabled) return;
        Screen s = mc.gui.screen();
        if (s != null && "net.minecraft.client.gui.screens.TitleScreen".equals(s.getClass().getName())) {
            mc.gui.setScreen(new HorizonTitleScreen());
        }
    }
}
