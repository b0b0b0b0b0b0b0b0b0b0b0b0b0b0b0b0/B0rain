package com.black.minecraft.b0rain.config;

import com.black.minecraft.b0rain.B0rain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LanguageManager {
    private final B0rain plugin;
    private final ConfigManager configManager;
    private FileConfiguration languageConfig;
    private String currentLanguage;

    public LanguageManager(B0rain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        initializeLanguageFiles();
        loadLanguage();
    }

    private void initializeLanguageFiles() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        String[] languages = {"ru", "en"};
        for (String lang : languages) {
            String langFile = "lang_" + lang + ".yml";
            File langFileObj = new File(langFolder, langFile);
            
            if (!langFileObj.exists()) {
                try (InputStream inputStream = plugin.getResource(langFile)) {
                    if (inputStream != null) {
                        try (FileOutputStream outputStream = new FileOutputStream(langFileObj)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to copy language file: " + langFile);
                }
            }
        }
    }

    public void loadLanguage() {
        String langFromConfig = configManager.getLanguage();
        String normalizedLang = langFromConfig.toLowerCase(Locale.ROOT);
        
        if (!normalizedLang.equals("ru") && !normalizedLang.equals("en")) {
            normalizedLang = "en";
        }
        
        this.currentLanguage = normalizedLang;
        
        String langFile = "lang_" + currentLanguage + ".yml";
        File langFolder = new File(plugin.getDataFolder(), "lang");
        File langFileObj = new File(langFolder, langFile);

        if (!langFileObj.exists()) {
            try (InputStream inputStream = plugin.getResource(langFile)) {
                if (inputStream != null) {
                    try (FileOutputStream outputStream = new FileOutputStream(langFileObj)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to copy language file: " + langFile);
            }
        }

        languageConfig = YamlConfiguration.loadConfiguration(langFileObj);
        InputStream defaultStream = plugin.getResource(langFile);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            languageConfig.setDefaults(defaultConfig);
        }
    }

    public String getMessage(String key) {
        return languageConfig.getString("messages." + key, key);
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
