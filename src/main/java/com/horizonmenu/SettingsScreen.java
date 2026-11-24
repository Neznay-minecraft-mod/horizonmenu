package com.horizonmenu;

import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends Screen {
    private static final int[] COUNTS = {20, 40, 60, 100, 160, 250};
    private final Screen parent;

    public SettingsScreen(Screen parent) {
        super(Component.literal("HorizonMenu"));
        this.parent = parent;
    }

    private static String onOff(boolean v) {
        return v ? "\u00a7a\u0412\u041a\u041b" : "\u00a7c\u0412\u042b\u041a\u041b";
    }

    private void refresh() {
        this.clearWidgets();
        this.init();
    }

    private void add(int x, int y, int w, String label, Consumer<Button> press) {
        this.addRenderableWidget(Button.builder(Component.literal(label), btn -> press.accept(btn))
                .bounds(x, y, w, 20).build());
    }

    @Override
    protected void init() {
        Config c = Config.get();
        int bw = 150, gap = 6;
        int x1 = this.width / 2 - bw - gap / 2;
        int x2 = this.width / 2 + gap / 2;
        int y = Math.max(30, this.height / 2 - 96);
        int step = 24;

        add(x1, y, bw, "Заменять меню: " + onOff(c.enabled), b -> { c.enabled = !c.enabled; Config.save(); refresh(); });
        add(x2, y, bw, "Фон (биомы) >", b -> Nav.open(new BiomeScreen(this)));
        y += step;

        add(x1, y, bw, "Расположение кнопок >", b -> Nav.open(new LayoutScreen(this)));
        add(x2, y, bw, "Язык кнопок: " + (c.russian ? "RU" : "EN"), b -> { c.russian = !c.russian; Config.save(); refresh(); });
        y += step;

        add(x1, y, bw, "Частицы: " + onOff(c.particles), b -> { c.particles = !c.particles; Config.save(); refresh(); });
        add(x2, y, bw, "Кол-во частиц: " + c.particleCount, b -> {
            int next = COUNTS[0];
            for (int i = 0; i < COUNTS.length; i++) {
                if (COUNTS[i] > c.particleCount) { next = COUNTS[i]; break; }
            }
            c.particleCount = next;
            Config.save();
            refresh();
        });
        y += step;

        add(x1, y, bw, "Движение фона: " + onOff(c.drift), b -> { c.drift = !c.drift; Config.save(); refresh(); });
        add(x2, y, bw, "Логотип: " + onOff(c.logo), b -> { c.logo = !c.logo; Config.save(); refresh(); });
        y += step;

        add(x1, y, bw, "Версия внизу: " + onOff(c.showVersion), b -> { c.showVersion = !c.showVersion; Config.save(); refresh(); });
        add(x2, y, bw, "Фон на всех меню: " + onOff(c.allScreens), b -> { c.allScreens = !c.allScreens; Config.save(); refresh(); });
        y += step;

        add(this.width / 2 - 75, y, 150, "Сбросить расположение", b -> { c.resetLayout(); Config.save(); refresh(); });
        y += step + 6;

        add(this.width / 2 - 75, y, 150, "Готово", b -> Nav.open(parent));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        String t = "HorizonMenu \u2014 настройки";
        g.text(this.font, t, (this.width - this.font.width(t)) / 2, 14, 0xFFFFFFFF, true);
    }

    @Override
    public void onClose() {
        Nav.open(parent);
    }
}
