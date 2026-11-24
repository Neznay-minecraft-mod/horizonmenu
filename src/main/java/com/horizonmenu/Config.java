package com.horizonmenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;

public final class Config {
    public static final String[] IDS = {"play", "multiplayer", "realms", "mods", "options", "quit"};

    public static final class Item {
        public String id;
        public boolean visible = true;
        public int dx = 0;
        public int dy = 0;

        public Item() {}

        public Item(String id) {
            this.id = id;
        }
    }

    public boolean enabled = true;
    public boolean allScreens = true;
    public String biome = "panorama";
    public boolean particles = true;
    public int particleCount = 60;
    public boolean drift = true;
    public boolean logo = true;
    public boolean showVersion = true;
    public boolean russian = false;
    public String align = "LEFT";
    public int spacing = 18;
    public int marginX = 18;
    public int startY = 48;
    public int itemWidth = 120;
    public List<Item> items = new ArrayList<>();

    private static Config instance;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("horizonmenu.json");
    }

    public static Config get() {
        if (instance == null) load();
        return instance;
    }

    public static void load() {
        Config c = null;
        try {
            Path p = file();
            if (Files.exists(p)) {
                try (Reader r = Files.newBufferedReader(p)) {
                    c = GSON.fromJson(r, Config.class);
                }
            }
        } catch (Throwable t) {
            HorizonMenuClient.LOGGER.warn("Config load failed: {}", t.toString());
        }
        if (c == null) c = new Config();
        c.normalize();
        instance = c;
        save();
    }

    public static void save() {
        if (instance == null) return;
        try (Writer w = Files.newBufferedWriter(file())) {
            GSON.toJson(instance, w);
        } catch (Throwable t) {
            HorizonMenuClient.LOGGER.warn("Config save failed: {}", t.toString());
        }
    }

    public void normalize() {
        if (items == null) items = new ArrayList<>();
        List<Item> fixed = new ArrayList<>();
        for (Item it : items) {
            if (it == null || it.id == null) continue;
            boolean known = false;
            for (String id : IDS) if (id.equals(it.id)) known = true;
            boolean dup = false;
            for (Item f : fixed) if (f.id.equals(it.id)) dup = true;
            if (known && !dup) fixed.add(it);
        }
        for (String id : IDS) {
            boolean has = false;
            for (Item f : fixed) if (f.id.equals(id)) has = true;
            if (!has) fixed.add(new Item(id));
        }
        items = fixed;
        if (biome == null) biome = "panorama";
        if (!"LEFT".equals(align) && !"CENTER".equals(align) && !"RIGHT".equals(align)) align = "LEFT";
        spacing = Math.max(10, Math.min(40, spacing));
        marginX = Math.max(0, Math.min(300, marginX));
        startY = Math.max(5, Math.min(85, startY));
        itemWidth = Math.max(60, Math.min(220, itemWidth));
        particleCount = Math.max(0, Math.min(400, particleCount));
    }

    public void resetLayout() {
        align = "LEFT";
        spacing = 18;
        marginX = 18;
        startY = 48;
        itemWidth = 120;
        items = new ArrayList<>();
        normalize();
    }

    public static String label(String id, boolean ru) {
        switch (id) {
            case "play": return ru ? "Играть" : "Play";
            case "multiplayer": return ru ? "Сетевая игра" : "Multiplayer";
            case "realms": return "Realms";
            case "mods": return ru ? "Моды" : "Mods";
            case "options": return ru ? "Настройки" : "Options";
            case "quit": return ru ? "Выход" : "Quit";
            default: return id;
        }
    }
}
