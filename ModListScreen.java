package com.horizonmenu;

import java.util.List;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Fallback "Mods" screen, used when Mod Menu is not installed. */
public class ModListScreen extends Screen {
    private final Screen parent;
    private int page = 0;

    public ModListScreen(Screen parent) {
        super(Component.literal("Mods"));
        this.parent = parent;
    }

    private int perPage() {
        return Math.max(4, (this.height - 80) / 12);
    }

    private void refresh() {
        this.clearWidgets();
        this.init();
    }

    @Override
    protected void init() {
        int by = this.height - 28;
        int cx = this.width / 2;
        this.addRenderableWidget(Button.builder(Component.literal("<"), b -> {
            if (page > 0) { page--; refresh(); }
        }).bounds(cx - 120, by, 40, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Назад"), b -> Nav.open(parent))
                .bounds(cx - 75, by, 150, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), b -> {
            int pages = (MenuRender.visibleMods().size() + perPage() - 1) / perPage();
            if (page < pages - 1) { page++; refresh(); }
        }).bounds(cx + 80, by, 40, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        List<ModContainer> mods = MenuRender.visibleMods();
        String t = "Mods: " + mods.size();
        g.text(this.font, t, (this.width - this.font.width(t)) / 2, 12, 0xFFFFFFFF, true);
        int pp = perPage();
        int start = page * pp;
        int y = 34;
        for (int i = start; i < Math.min(mods.size(), start + pp); i++) {
            ModContainer m = mods.get(i);
            String line = m.getMetadata().getName() + "  " + m.getMetadata().getVersion().getFriendlyString();
            g.text(this.font, line, this.width / 2 - 140, y, 0xFFE0E0E0, true);
            y += 12;
        }
    }

    @Override
    public void onClose() {
        Nav.open(parent);
    }
}
