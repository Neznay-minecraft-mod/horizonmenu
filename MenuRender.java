package com.horizonmenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Shared layout maths + drawing for the menu entries (title screen and layout editor). */
public final class MenuRender {
    public static final int ITEM_H = 14;

    private MenuRender() {}

    public static int visibleSlot(Config c, int index) {
        int s = 0;
        for (int i = 0; i < index; i++) if (c.items.get(i).visible) s++;
        return s;
    }

    public static int itemX(Config c, int w, Config.Item it) {
        int base;
        switch (c.align) {
            case "RIGHT": base = w - c.marginX - c.itemWidth; break;
            case "CENTER": base = (w - c.itemWidth) / 2; break;
            default: base = c.marginX;
        }
        return base + it.dx;
    }

    public static int itemY(Config c, int h, int slot, Config.Item it) {
        return h * c.startY / 100 + slot * c.spacing + it.dy;
    }

    public static List<ModContainer> visibleMods() {
        List<ModContainer> list = new ArrayList<>();
        for (ModContainer m : FabricLoader.getInstance().getAllMods()) {
            if (m.getContainingMod().isPresent()) continue;
            if ("builtin".equals(m.getMetadata().getType())) continue;
            list.add(m);
        }
        list.sort(Comparator.comparing((ModContainer m) -> m.getMetadata().getName().toLowerCase()));
        return list;
    }

    public static void drawItem(GuiGraphicsExtractor g, Font font, Config c, Config.Item it, int x, int y,
                                boolean hovered, boolean selected, boolean ghost) {
        int col = hovered ? 0xFFFFFFFF : 0xFFD8D8D8;
        if (ghost) col = 0xFFFF7070;
        Icons.draw(g, it.id, x, y + 2, col);
        String label = Config.label(it.id, c.russian);
        g.text(font, label, x + 16, y + 3, col, true);
        if ("mods".equals(it.id)) {
            g.text(font, String.valueOf(visibleMods().size()), x + 16 + font.width(label) + 6, y + 3, 0xFF9A9A9A, true);
        }
        if (hovered) {
            g.fill(x + 16, y + 13, x + 16 + font.width(label), y + 14, 0xCCFFFFFF);
        }
        if (selected) {
            Icons.box(g, x - 4, y - 2, c.itemWidth + 6, ITEM_H + 4, 0xFFFFA500);
        }
    }

    public static void drawLogo(GuiGraphicsExtractor g, Font font, int x, int y) {
        int cx = x + 10, cy = y + 10;
        for (double a = 0; a < Math.PI * 2; a += 0.12) {
            int px = cx + (int) Math.round(Math.cos(a) * 9);
            int py = cy + (int) Math.round(Math.sin(a) * 9);
            g.fill(px, py, px + 2, py + 2, 0xFFE8E8E8);
        }
        g.fill(cx + 6, cy - 10, cx + 10, cy - 6, 0xFFFF6FA8);
        g.text(font, "HorizonMenu", x + 26, y + 3, 0xFFFFFFFF, true);
        g.text(font, "JAVA", x + 26, y + 13, 0xFFFF6FA8, true);
    }

    public static String mcVersion() {
        try {
            return FabricLoader.getInstance().getModContainer("minecraft")
                    .map(m -> m.getMetadata().getVersion().getFriendlyString()).orElse("26.2");
        } catch (Throwable t) {
            return "26.2";
        }
    }
}
