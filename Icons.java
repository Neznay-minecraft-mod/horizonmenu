package com.horizonmenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Tiny 10x10 pixel icons drawn with fills (no textures needed). */
public final class Icons {
    private Icons() {}

    public static void box(GuiGraphicsExtractor g, int x, int y, int w, int h, int col) {
        g.fill(x, y, x + w, y + 1, col);
        g.fill(x, y + h - 1, x + w, y + h, col);
        g.fill(x, y, x + 1, y + h, col);
        g.fill(x + w - 1, y, x + w, y + h, col);
    }

    public static void draw(GuiGraphicsExtractor g, String id, int x, int y, int col) {
        switch (id) {
            case "play": {
                for (int i = 0; i < 7; i++) g.fill(x + 8 - i, y + i, x + 10 - i, y + i + 1, col);
                g.fill(x + 1, y + 6, x + 5, y + 7, col);
                g.fill(x + 2, y + 5, x + 3, y + 9, col);
                g.fill(x, y + 9, x + 2, y + 10, col);
                break;
            }
            case "multiplayer": {
                for (int k = 0; k < 4; k++) {
                    int hh = 2 + k * 2;
                    g.fill(x + k * 3, y + 10 - hh, x + k * 3 + 2, y + 10, col);
                }
                break;
            }
            case "realms": {
                box(g, x + 1, y, 8, 10, col);
                g.fill(x + 4, y + 4, x + 6, y + 6, col);
                break;
            }
            case "mods": {
                g.fill(x, y, x + 10, y + 10, col);
                for (int i = 0; i < 8; i++) {
                    g.fill(x + 1 + i, y + 1 + i, x + 2 + i, y + 2 + i, 0xFF101010);
                    g.fill(x + 8 - i, y + 1 + i, x + 9 - i, y + 2 + i, 0xFF101010);
                }
                break;
            }
            case "options": {
                box(g, x, y, 10, 10, col);
                g.fill(x + 3, y + 3, x + 7, y + 7, col);
                break;
            }
            case "quit": {
                box(g, x, y + 1, 10, 9, col);
                g.fill(x + 4, y, x + 6, y + 5, col);
                break;
            }
            case "gear": {
                box(g, x, y, 10, 10, col);
                g.fill(x + 3, y + 3, x + 7, y + 7, col);
                g.fill(x + 4, y - 2, x + 6, y, col);
                g.fill(x + 4, y + 10, x + 6, y + 12, col);
                g.fill(x - 2, y + 4, x, y + 6, col);
                g.fill(x + 10, y + 4, x + 12, y + 6, col);
                break;
            }
            default:
                g.fill(x + 2, y + 2, x + 8, y + 8, col);
        }
    }
}
