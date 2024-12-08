package com.mohistmc.plugins.ban;

import com.mohistmc.plugins.config.MohistPluginConfig;
import java.io.File;
import java.util.List;

public class BanConfig extends MohistPluginConfig {

    public static BanConfig MOSHOU;

    public BanConfig(File file) {
        super(file);
    }

    public static void init() {
        MOSHOU = new BanConfig(new File("mohist-config/bans", "item-moshou.yml"));
    }

    public void addMoShou(String name) {
        if (!has("ITEMS")) {
            put("ITEMS", List.of());
        }
        List<String> list = MOSHOU.yaml.getStringList("ITEMS");
        list.add(name);
        put("ITEMS", list);
    }

    public List<String> getMoShouList() {
        return (!has("ITEMS")) ? List.of() : MOSHOU.yaml.getStringList("ITEMS");
    }
}
