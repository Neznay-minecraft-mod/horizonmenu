package com.horizonmenu;

import java.util.ArrayList;
import java.util.List;

/** Every Minecraft biome with a procedural backdrop (sky, three hill layers, particle type, roughness). */
public final class Biomes {
    /** fx: N none, S snow, A ash, E embers, G spores, M motes, P petals. amp: 0 flat .. 3 peaks. */
    public record Biome(String id, String name, int sky, int horizon, int far, int mid, int near, char fx, int amp) {}

    public static final List<Biome> ALL = new ArrayList<>();

    private static int c(String hex) {
        return 0xFF000000 | Integer.parseInt(hex, 16);
    }

    private static void add(String id, String name, String sky, String hor, String far, String mid, String near, char fx, int amp) {
        ALL.add(new Biome(id, name, c(sky), c(hor), c(far), c(mid), c(near), fx, amp));
    }

    public static Biome byId(String id) {
        for (Biome b : ALL) if (b.id().equals(id)) return b;
        return ALL.get(2);
    }

    static {
        add("plains", "Равнины", "5B9BEA", "BFE0FF", "7FA86A", "5E9446", "3F7A2F", 'N', 1);
        add("sunflower_plains", "Подсолнечные равнины", "5B9BEA", "C9E6FF", "86AA5C", "7FAE3D", "5C9A2A", 'N', 1);
        add("snowy_plains", "Снежные равнины", "7DA9DD", "E4F1FF", "C8D8E6", "DCE8F2", "F2F7FB", 'S', 1);
        add("ice_spikes", "Ледяные шипы", "8FB8E8", "EAF6FF", "A8D4F2", "C6E6FA", "E6F6FF", 'S', 3);
        add("desert", "Пустыня", "6FB0F0", "F6E6B8", "D9C27A", "E2CB82", "EBD68E", 'N', 1);
        add("swamp", "Болото", "6E8F78", "A9BFA0", "4F6B48", "3E5A3C", "2F4A33", 'G', 1);
        add("mangrove_swamp", "Мангровые заросли", "6FA08E", "B7D0A8", "4C6F44", "3A5B34", "2D4A2B", 'G', 1);
        add("forest", "Лес", "5D9AE0", "BCDDF5", "4F7F44", "3C6B34", "2C5726", 'N', 2);
        add("flower_forest", "Цветочный лес", "63A2EA", "C9E3F7", "5A8F4E", "4E9444", "3E7F35", 'P', 2);
        add("birch_forest", "Берёзовый лес", "63A0E6", "C4E2F8", "7C9A5A", "6A8E4A", "58803E", 'N', 2);
        add("dark_forest", "Тёмный лес", "3F5F8F", "7F9BB5", "2E4A2A", "243C22", "1A2E19", 'N', 2);
        add("old_growth_birch_forest", "Старый берёзовый лес", "63A0E6", "C0E0F4", "7A9858", "66884A", "547A3C", 'N', 2);
        add("old_growth_pine_taiga", "Старая сосновая тайга", "5E8FC8", "AFCDE0", "4A6B4A", "3A5A3C", "2A4A2E", 'N', 2);
        add("old_growth_spruce_taiga", "Старая еловая тайга", "5A8AC2", "A9C6DB", "456648", "35553A", "27452D", 'N', 2);
        add("taiga", "Тайга", "5E90CC", "B4D3E8", "4E7050", "3D5E42", "2D4D33", 'N', 2);
        add("snowy_taiga", "Снежная тайга", "7AA2D2", "DCEAF6", "8FA8A0", "AFC4BE", "DCE8E4", 'S', 2);
        add("savanna", "Саванна", "5FA8E8", "F2E2A6", "B5A850", "A39A3E", "8C8A32", 'N', 1);
        add("savanna_plateau", "Плато саванны", "5FA8E8", "F0DDA0", "BDA950", "AE9A40", "968A34", 'N', 2);
        add("windswept_hills", "Ветреные холмы", "6F98C8", "C3D6E6", "7A8A7A", "6A7C6A", "5A6E5A", 'N', 3);
        add("windswept_gravelly_hills", "Ветреные гравийные холмы", "6F98C8", "C3D6E6", "8A8E8E", "7A7E7E", "6A6E6A", 'N', 3);
        add("windswept_forest", "Ветреный лес", "6B94C4", "BED2E4", "5E7A5E", "4C6A50", "3A5A40", 'N', 3);
        add("windswept_savanna", "Ветреная саванна", "5FA2E0", "ECDCA4", "A89C50", "968C3E", "7E7A32", 'N', 3);
        add("jungle", "Джунгли", "4F9AE0", "B5E0C8", "2F7A32", "1F6A2A", "135A20", 'G', 2);
        add("sparse_jungle", "Редкие джунгли", "55A0E4", "BDE2CC", "3E8A3A", "2E7A32", "1F6A28", 'N', 2);
        add("bamboo_jungle", "Бамбуковые джунгли", "52A0E2", "BCE4C6", "5C9A3A", "3F8A32", "2C7A28", 'G', 2);
        add("badlands", "Бесплодные земли", "6FA8E4", "F0C9A0", "C4693A", "B25A2E", "9A4A26", 'N', 2);
        add("eroded_badlands", "Эродированные бесплодные земли", "6FA8E4", "F0C39A", "C9703E", "B65E30", "9E4C27", 'N', 3);
        add("wooded_badlands", "Лесистые бесплодные земли", "6FA8E4", "EEC8A0", "B8683A", "7A8A3A", "5E7A2E", 'N', 2);
        add("meadow", "Луга", "63A8F0", "D4ECFF", "8CBF6A", "74B058", "5CA048", 'P', 1);
        add("cherry_grove", "Вишнёвая роща", "78B4F4", "F6DDE8", "E8A8C4", "D890B0", "74AA58", 'P', 1);
        add("grove", "Роща", "7EAAE0", "E0EEFA", "9AB0A0", "BCD0C8", "E2EEEA", 'S', 2);
        add("snowy_slopes", "Снежные склоны", "7EAAE0", "E6F2FC", "AFC4D4", "D0E0EA", "F0F6FA", 'S', 3);
        add("frozen_peaks", "Замёрзшие пики", "7AA6DE", "E2F0FA", "9AB4CC", "BCD4E4", "E8F2FA", 'S', 3);
        add("jagged_peaks", "Зубчатые пики", "78A4DC", "E0EEF8", "98A8B8", "B8C8D6", "E6EEF6", 'S', 3);
        add("stony_peaks", "Каменистые пики", "76A2DA", "D8E6F2", "8A8E92", "A0A4A8", "B6B8BA", 'N', 3);
        add("river", "Река", "5FA4EC", "C4E4FA", "5A8FC8", "4A7FBA", "6A9A4A", 'N', 0);
        add("frozen_river", "Замёрзшая река", "7AA8DC", "E0F0FC", "9CC4E4", "BCDCF0", "DCF0FA", 'S', 0);
        add("beach", "Пляж", "60A8EE", "CDEBFA", "4A8FD0", "E2D49A", "F0E4B0", 'N', 0);
        add("snowy_beach", "Снежный пляж", "7AA8DC", "E0F0FC", "7AA6CE", "D8E8F2", "F0F6FA", 'S', 0);
        add("stony_shore", "Каменистый берег", "6A9AD0", "C4D8EA", "5A86BC", "7A7E82", "8E9296", 'N', 1);
        add("warm_ocean", "Тёплый океан", "4EB4EE", "BDF0F4", "2FB8D8", "23A8CC", "1B94B8", 'N', 0);
        add("lukewarm_ocean", "Умеренный океан", "4EAAEA", "BCE6F4", "2C9CD0", "2088C0", "1A74AC", 'N', 0);
        add("deep_lukewarm_ocean", "Глубокий умеренный океан", "4AA2E4", "B4DCEE", "2688BC", "1C74AC", "14609A", 'N', 0);
        add("ocean", "Океан", "4C9CE4", "B8DAF0", "2E74C0", "2262B0", "1A509E", 'N', 0);
        add("deep_ocean", "Глубокий океан", "4890DC", "B0D0E8", "2860AC", "1E509C", "16408A", 'N', 0);
        add("cold_ocean", "Холодный океан", "5E96D6", "C4DAEC", "3A78B8", "2E68A8", "245A98", 'N', 0);
        add("deep_cold_ocean", "Глубокий холодный океан", "5A90D0", "BCD2E6", "3468A8", "2A5898", "204888", 'N', 0);
        add("frozen_ocean", "Ледяной океан", "7AA2D6", "DCE8F4", "7CA6D0", "B0CCE4", "D8E8F4", 'S', 0);
        add("deep_frozen_ocean", "Глубокий ледяной океан", "7A9ED0", "D4E2F0", "7098C4", "A0BEDA", "CADCEE", 'S', 0);
        add("mushroom_fields", "Грибные поля", "7A9CE8", "D8C8F0", "8C6E8C", "A0809A", "7A6A80", 'M', 1);
        add("dripstone_caves", "Карстовые пещеры", "2A2420", "4A3E34", "5A4A3A", "463A2E", "342A22", 'N', 2);
        add("lush_caves", "Пышные пещеры", "1E2A22", "3A5A3A", "3E6A3A", "2E5A2E", "1E4A22", 'G', 2);
        add("deep_dark", "Глубокая тьма", "060A10", "10202A", "10222C", "0A1820", "060E14", 'M', 2);
        add("nether_wastes", "Пустоши Незера", "3A0E0E", "8A2A1A", "5A1A16", "481410", "34100C", 'E', 2);
        add("soul_sand_valley", "Долина душ", "0E1A2A", "2A5A7A", "2A3A4A", "1E2E3C", "141E2A", 'M', 2);
        add("crimson_forest", "Багровый лес", "3A0A12", "8A1A2A", "6A1424", "520E1C", "3A0A14", 'E', 2);
        add("warped_forest", "Искажённый лес", "0A2A2E", "1A6A66", "1A5A5A", "124A4A", "0C3838", 'G', 2);
        add("basalt_deltas", "Базальтовые дельты", "2A2A30", "6A5A5A", "3A3A42", "2C2C34", "1E1E26", 'A', 2);
        add("the_end", "Энд", "0A0614", "2A1A40", "3A3A2A", "2E2E22", "22221A", 'M', 1);
        add("end_highlands", "Внешние острова Энда", "0A0614", "2A1A40", "5A5A3A", "48482E", "363622", 'M', 2);
        add("end_midlands", "Средние острова Энда", "0A0614", "2A1A40", "50503A", "40402E", "303022", 'M', 1);
        add("small_end_islands", "Малые острова Энда", "0A0614", "2A1A40", "3E3E30", "30302A", "24241E", 'M', 0);
        add("end_barrens", "Пустоши Энда", "0A0614", "2A1A40", "46463A", "38382E", "2A2A22", 'M', 1);
        add("pale_garden", "Бледный сад", "8A9AA6", "C8D2D6", "7A8480", "6A7470", "586260", 'A', 2);
    }

    private Biomes() {}
}
