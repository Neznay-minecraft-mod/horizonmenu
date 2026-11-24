package com.horizonmenu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BiomeScreen extends Screen {
    private static final int COLS = 4;
    private final Screen parent;
    private int page = 0;
    private int pages = 1;
    private final List<Button> grid = new ArrayList<>();
    private final List<String> gridIds = new ArrayList<>();

    public BiomeScreen(Screen parent) {
        super(Component.literal("HorizonMenu"));
        this.parent = parent;
    }

    private void refresh() {
        this.clearWidgets();
        this.init();
    }

    @Override
    protected void init() {
        grid.clear();
        gridIds.clear();
        Config c = Config.get();

        List<String> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        ids.add(Background.PANORAMA);
        names.add("Панорама Minecraft");
        ids.add("random");
        names.add("Случайный");
        ids.add("cycle");
        names.add("По кругу (смена)");
        for (Biomes.Biome bm : Biomes.ALL) {
            ids.add(bm.id());
            names.add(bm.name());
        }

        int bw = 118, bh = 20, gx = 6, gy = 4, top = 30;
        int rows = Math.max(2, Math.min(7, (this.height - top - 40) / (bh + gy)));
        int perPage = rows * COLS;
        pages = (ids.size() + perPage - 1) / perPage;
        page = Math.max(0, Math.min(page, pages - 1));
        int totalW = COLS * bw + (COLS - 1) * gx;
        int x0 = (this.width - totalW) / 2;

        for (int i = 0; i < perPage; i++) {
            int idx = page * perPage + i;
            if (idx >= ids.size()) break;
            int col = i % COLS, row = i / COLS;
            String id = ids.get(idx);
            boolean sel = id.equals(c.biome);
            Button btnW = Button.builder(Component.literal((sel ? "\u00a7a" : "") + names.get(idx)), btn -> {
                        c.biome = id;
                        Config.save();
                        refresh();
                    })
                    .bounds(x0 + col * (bw + gx), top + row * (bh + gy), bw, bh)
                    .build();
            this.addRenderableWidget(btnW);
            grid.add(btnW);
            gridIds.add(id);
        }

        int by = this.height - 28;
        int cx = this.width / 2;
        this.addRenderableWidget(Button.builder(Component.literal("<"), b -> {
            if (page > 0) { page--; refresh(); }
        }).bounds(cx - 120, by, 40, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Назад"), b -> Nav.open(parent))
                .bounds(cx - 75, by, 150, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), b -> {
            if (page < pages - 1) { page++; refresh(); }
        }).bounds(cx + 80, by, 40, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        String id = null;
        for (int i = 0; i < grid.size(); i++) {
            if (Nav.hovered(grid.get(i))) id = gridIds.get(i);
        }
        Background.preview = id; // live preview of the hovered biome (used by the background mixin)
        super.extractRenderState(g, mouseX, mouseY, delta);
        String t = "Биомы — стр. " + (page + 1) + "/" + pages + " (наведи — предпросмотр, клик — выбрать)";
        g.text(this.font, t, (this.width - this.font.width(t)) / 2, 14, 0xFFFFFFFF, true);
    }

    public void removed() {
        Background.preview = null;
    }

    @Override
    public void onClose() {
        Background.preview = null;
        Nav.open(parent);
    }
}
