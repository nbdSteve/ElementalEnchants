package gg.steve.elemental.ce.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Files {
    CONFIG("elemental-enchants.yml"),
    PERMISSIONS("permissions.yml"),
    DEBUG("lang" + File.separator + "debug.yml"),
    MESSAGES("lang" + File.separator + "messages.yml"),
    // gui
    TOKEN_GUI("gui" + File.separator + "token-enchants.yml"),
    PRESTIGE_GUI("gui" + File.separator + "prestige-enchants.yml");

    private final String path;

    Files(String path) {
        this.path = path;
    }

    public void load(FileManager fileManager) {
        fileManager.add(name(), this.path);
    }

    public YamlConfiguration get() {
        return FileManager.get(name());
    }

    public void save() {
        FileManager.save(name());
    }

    public static void reload() {
        FileManager.reload();
    }

    public static boolean doTokenDrop() {
        return Math.random() * 100 < CONFIG.get().getDouble("token-mine-chance");
    }
}
