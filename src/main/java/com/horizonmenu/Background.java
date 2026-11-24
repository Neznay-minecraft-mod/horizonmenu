package com.horizonmenu;

import java.util.Random;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Procedural, soft "blurred landscape" backdrop. */
public final class Background {
    private static final int STEP = 4;
    private static final int RANDOM_PICK = new Random().nextInt(Biomes.ALL.size());

    public static final String PANORAMA = "panorama";
    /** Set by the biome picker while hovering a biome (live preview); null otherwise. */
    public static String preview = null;

    private Background() {}

    public static boolean isPanorama(String id) {
        return PANORAMA.equals(id);
    }

    public static String activeId(Config c) {
        return preview != null ? preview : c.biome;
    }

    public static void drawParticles(GuiGraphicsExtractor g, int w, int h, Config c) {
        if (c.particles) particles(g, w, h, 'S', c.particleCount, now());
    }

    public static Biomes.Biome resolve(String id) {
        if ("random".equals(id)) return Biomes.ALL.get(RANDOM_PICK);
        if ("cycle".equals(id)) {
            int i = (int) ((System.currentTimeMillis() / 20000L) % Biomes.ALL.size());
            return Biomes.ALL.get(i);
        }
        return Biomes.byId(id);
    }

    private static double now() {
        return (System.currentTimeMillis() % 100000000L) / 1000.0;
    }

    public static void draw(GuiGraphicsExtractor g, int w, int h, Biomes.Biome b, Config c) {
        double t = now();
        int horizonY = (int) (h * 0.66);

        g.fillGradient(0, 0, w, horizonY + 1, b.sky(), b.horizon());
        g.fill(0, horizonY, w, h, b.near());

        int sr = (b.sky() >> 16) & 0xFF, sg = (b.sky() >> 8) & 0xFF, sb = b.sky() & 0xFF;
        if ((sr + sg + sb) / 3 < 64) stars(g, w, horizonY, t);

        layer(g, w, h, b, c, t, 0.50, 0.05, 0.0040, 1.5, b.far(), 11);
        layer(g, w, h, b, c, t, 0.60, 0.04, 0.0060, 3.5, b.mid(), 29);

        int hz = b.horizon() & 0xFFFFFF;
        g.fillGradient(0, (int) (h * 0.42), w, (int) (h * 0.66), hz, (0x55 << 24) | hz);

        layer(g, w, h, b, c, t, 0.72, 0.03, 0.0090, 7.0, b.near(), 53);

        g.fillGradient(0, 0, w, (int) (h * 0.22), 0x66000000, 0x00000000);
        g.fillGradient(0, (int) (h * 0.70), w, h, 0x00000000, 0x88000000);
        g.fill(0, 0, w, h, 0x22000000);

        if (c.particles && b.fx() != 'N') particles(g, w, h, b.fx(), c.particleCount, t);
    }

    private static void layer(GuiGraphicsExtractor g, int w, int h, Biomes.Biome b, Config c, double t,
                              double baseF, double ampF, double freq, double speed, int color, int seed) {
        double off = c.drift ? t * speed : 0;
        int amp = b.amp();
        double a = h * (ampF * (0.25 + amp * 0.55));
        int base = (int) (h * baseF);
        for (int x = 0; x < w; x += STEP) {
            double u = (x + off) * freq + seed;
            double n;
            if (amp >= 3) {
                double p = 1.0 - Math.abs(Math.sin(u * 1.2));
                n = 0.65 * p * p + 0.35 * (0.5 + 0.5 * Math.sin(u * 3.1 + 1.7));
            } else {
                n = 0.5 + 0.5 * (0.62 * Math.sin(u) + 0.28 * Math.sin(u * 2.3 + 1.7) + 0.10 * Math.sin(u * 5.1 + 0.4));
            }
            int top = base - (int) (a * n);
            g.fill(x, top, x + STEP, h, color);
        }
    }

    private static double hash(int i, int k) {
        double v = Math.sin(i * 12.9898 + k * 78.233) * 43758.5453;
        return v - Math.floor(v);
    }

    private static void stars(GuiGraphicsExtractor g, int w, int maxY, double t) {
        for (int i = 0; i < 90; i++) {
            int x = (int) (hash(i, 1) * w);
            int y = (int) (hash(i, 2) * maxY * 0.9);
            double tw = 0.5 + 0.5 * Math.sin(t * (0.6 + hash(i, 3)) + i);
            int a = (int) (70 + 150 * tw);
            g.fill(x, y, x + 1, y + 1, (a << 24) | 0xFFFFFF);
        }
    }

    private static void particles(GuiGraphicsExtractor g, int w, int h, char fx, int n, double t) {
        for (int i = 0; i < n; i++) {
            double sx = hash(i, 1), sy = hash(i, 2), sp = 0.4 + hash(i, 3);
            double x, y;
            int size = 1;
            int col;
            switch (fx) {
                case 'S':
                    y = (sy * h + t * (18 + 30 * sp)) % h;
                    x = (sx * w + Math.sin(t * 0.7 + i) * 14 + t * 6) % w;
                    size = sp > 1.2 ? 2 : 1;
                    col = 0xCCFFFFFF;
                    break;
                case 'A':
                    y = (sy * h + t * (8 + 14 * sp)) % h;
                    x = (sx * w + Math.sin(t * 0.5 + i) * 24 + t * 10) % w;
                    col = 0x88B8B8B8;
                    break;
                case 'E':
                    y = h - ((sy * h + t * (20 + 30 * sp)) % h);
                    x = (sx * w + Math.sin(t * 0.9 + i) * 16) % w;
                    col = 0xCCFF8A2A;
                    break;
                case 'G':
                    y = h - ((sy * h + t * (6 + 10 * sp)) % h);
                    x = (sx * w + Math.sin(t * 0.6 + i) * 20) % w;
                    col = 0x99B8F060;
                    break;
                case 'P':
                    y = (sy * h + t * (12 + 16 * sp)) % h;
                    x = (sx * w + Math.sin(t * 0.8 + i) * 30 + t * 8) % w;
                    size = 2;
                    col = 0xCCF8B8D0;
                    break;
                default: // 'M' motes
                    y = (sy * h + Math.sin(t * 0.4 + i) * 30 + t * 4) % h;
                    x = (sx * w + Math.cos(t * 0.5 + i) * 30) % w;
                    col = 0x99C880FF;
                    break;
            }
            int px = (int) x, py = (int) y;
            g.fill(px, py, px + size, py + size, col);
        }
    }
}
