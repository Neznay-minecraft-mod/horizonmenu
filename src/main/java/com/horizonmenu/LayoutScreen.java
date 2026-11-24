package com.horizonmenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LayoutScreen extends Screen {
    private static final int[] STEPS = {1, 5, 10, 20};
    private final Screen parent;
    private int selected = 0;
    private int stepIdx = 1;
    private final List<Button> hits = new ArrayList<>();
    private final List<int[]> pos = new ArrayList<>();

    private record Btn(String label, int w, Consumer<Button> action) {}

    public LayoutScreen(Screen parent) {
        super(Component.literal("HorizonMenu"));
        this.parent = parent;
    }

    private void refresh() {
        this.clearWidgets();
        this.init();
    }

    private void addRow(int y, Btn... bs) {
        int total = -4;
        for (Btn bt : bs) total += bt.w() + 4;
        int x = (this.width - total) / 2;
        for (Btn bt : bs) {
            Consumer<Button> act = bt.action();
            this.addRenderableWidget(Button.builder(Component.literal(bt.label()), btn -> act.accept(btn))
                    .bounds(x, y, bt.w(), 20).build());
            x += bt.w() + 4;
        }
    }

    private static String alignName(String a) {
        switch (a) {
            case "CENTER": return "Центр";
            case "RIGHT": return "Справа";
            default: return "Слева";
        }
    }

    @Override
    protected void init() {
        hits.clear();
        pos.clear();
        Config c = Config.get();
        if (selected >= c.items.size()) selected = 0;

        for (int i = 0; i < c.items.size(); i++) {
            Config.Item it = c.items.get(i);
            int slot = MenuRender.visibleSlot(c, i);
            int x = MenuRender.itemX(c, this.width, it);
            int y = MenuRender.itemY(c, this.height, slot, it);
            int idx = i;
            Button hb = Button.builder(Component.literal(Config.label(it.id, c.russian)), btn -> {
                        selected = idx;
                        refresh();
                    })
                    .bounds(x - 2, y - 1, c.itemWidth, MenuRender.ITEM_H + 2)
                    .build();
            Nav.hide(hb);
            this.addRenderableWidget(hb);
            hits.add(hb);
            pos.add(new int[] {x, y});
        }

        Config.Item sel = c.items.get(selected);
        int step = STEPS[stepIdx];

        addRow(this.height - 52,
                new Btn("Кнопка: " + Config.label(sel.id, c.russian) + " >", 140, b -> {
                    selected = (selected + 1) % c.items.size();
                    refresh();
                }),
                new Btn("<", 22, b -> { sel.dx -= step; Config.save(); refresh(); }),
                new Btn(">", 22, b -> { sel.dx += step; Config.save(); refresh(); }),
                new Btn("^", 22, b -> { sel.dy -= step; Config.save(); refresh(); }),
                new Btn("v", 22, b -> { sel.dy += step; Config.save(); refresh(); }),
                new Btn("Шаг: " + step, 64, b -> { stepIdx = (stepIdx + 1) % STEPS.length; refresh(); }),
                new Btn("Показ: " + (sel.visible ? "\u00a7aДА" : "\u00a7cНЕТ"), 84, b -> {
                    sel.visible = !sel.visible;
                    Config.save();
                    refresh();
                }));

        addRow(this.height - 28,
                new Btn("Выравн.: " + alignName(c.align), 120, b -> {
                    c.align = "LEFT".equals(c.align) ? "CENTER" : ("CENTER".equals(c.align) ? "RIGHT" : "LEFT");
                    Config.save();
                    refresh();
                }),
                new Btn("Инт. -", 50, b -> { c.spacing = Math.max(10, c.spacing - 2); Config.save(); refresh(); }),
                new Btn("Инт. +", 50, b -> { c.spacing = Math.min(40, c.spacing + 2); Config.save(); refresh(); }),
                new Btn("Выше", 50, b -> {
                    if (selected > 0) {
                        Collections.swap(c.items, selected, selected - 1);
                        selected--;
                        Config.save();
                        refresh();
                    }
                }),
                new Btn("Ниже", 50, b -> {
                    if (selected < c.items.size() - 1) {
                        Collections.swap(c.items, selected, selected + 1);
                        selected++;
                        Config.save();
                        refresh();
                    }
                }),
                new Btn("Сброс", 52, b -> { c.resetLayout(); selected = 0; Config.save(); refresh(); }),
                new Btn("Готово", 60, b -> Nav.open(parent)));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        Config c = Config.get();
        for (int i = 0; i < c.items.size() && i < hits.size(); i++) {
            Config.Item it = c.items.get(i);
            int[] p = pos.get(i);
            MenuRender.drawItem(g, this.font, c, it, p[0], p[1],
                    Nav.hovered(hits.get(i)), i == selected, !it.visible);
        }
        String t = "Расположение кнопок — выбери пункт и двигай стрелками";
        g.text(this.font, t, (this.width - this.font.width(t)) / 2, 10, 0xFFFFFFFF, true);
    }

    @Override
    public void onClose() {
        Nav.open(parent);
    }
}
