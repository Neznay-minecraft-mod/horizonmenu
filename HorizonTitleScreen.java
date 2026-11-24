package com.horizonmenu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HorizonTitleScreen extends Screen {
    private final List<Button> hits = new ArrayList<>();
    private final List<int[]> pos = new ArrayList<>();
    private final List<Config.Item> hitItems = new ArrayList<>();
    private Button gear;

    public HorizonTitleScreen() {
        super(Component.literal("HorizonMenu"));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        hits.clear();
        pos.clear();
        hitItems.clear();
        Config c = Config.get();
        int slot = 0;
        for (Config.Item it : c.items) {
            if (!it.visible) continue;
            int x = MenuRender.itemX(c, this.width, it);
            int y = MenuRender.itemY(c, this.height, slot, it);
            Button b = Button.builder(Component.literal(Config.label(it.id, c.russian)), btn -> run(it.id))
                    .bounds(x - 2, y - 1, c.itemWidth, MenuRender.ITEM_H + 2)
                    .build();
            Nav.hide(b);
            this.addRenderableWidget(b);
            hits.add(b);
            pos.add(new int[] {x, y});
            hitItems.add(it);
            slot++;
        }
        gear = Button.builder(Component.literal("Settings"), btn -> Nav.open(new SettingsScreen(this)))
                .bounds(this.width - 30, 8, 22, 22)
                .build();
        Nav.hide(gear);
        this.addRenderableWidget(gear);
    }

    private void run(String id) {
        switch (id) {
            case "play":
                Nav.openVanilla("net.minecraft.client.gui.screens.worldselection.SelectWorldScreen", this);
                break;
            case "multiplayer":
                Nav.openVanilla("net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen", this);
                break;
            case "realms":
                Nav.openVanilla("com.mojang.realmsclient.RealmsMainScreen", this);
                break;
            case "mods":
                if (!Nav.openVanilla("com.terraformersmc.modmenu.gui.ModsScreen", this)) {
                    Nav.open(new ModListScreen(this));
                }
                break;
            case "options":
                Nav.openVanilla("net.minecraft.client.gui.screens.options.OptionsScreen", this);
                break;
            case "quit":
                Nav.quit();
                break;
            default:
                break;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        Config c = Config.get();
        if (Background.isPanorama(c.biome)) {
            // vanilla rotating panorama is drawn by the base screen; just soften it a little
            g.fill(0, 0, this.width, this.height, 0x30000000);
            Background.drawParticles(g, this.width, this.height, c);
        } else {
            Background.draw(g, this.width, this.height, Background.resolve(c.biome), c);
        }
        for (int i = 0; i < hits.size(); i++) {
            int[] p = pos.get(i);
            MenuRender.drawItem(g, this.font, c, hitItems.get(i), p[0], p[1],
                    Nav.hovered(hits.get(i)), false, false);
        }
        if (c.logo) MenuRender.drawLogo(g, this.font, 14, 12);
        Icons.draw(g, "gear", this.width - 24, 14, Nav.hovered(gear) ? 0xFFFFFFFF : 0xFFB8B8B8);
        if (c.showVersion) {
            g.text(this.font, "Minecraft " + MenuRender.mcVersion() + " \u00a9 Mojang AB", 10, this.height - 22, 0xFFB0B0B0, true);
            g.text(this.font, "HorizonMenu v1.0.0", 10, this.height - 12, 0xFFB0B0B0, true);
        }
    }
}
