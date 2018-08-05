package io.lootsafe.api.java.minecraft.sponge.Utils;

import io.lootsafe.api.java.minecraft.sponge.LootSafePlugin;
import org.spongepowered.api.asset.Asset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Adam Sanchez on 4/21/2018.
 */
public class ConfigHandler {

    public static void initConfig(Path pluginDirectory){
        Asset configAsset = LootSafePlugin.getInstance().getPlugin().getAsset("lootsafe.conf").orElse(null);
        if (Files.notExists(pluginDirectory)) {
            if (configAsset != null) {
                try {
                    LootSafePlugin.getInstance().getLogger().info("Copying Default Config");
                    LootSafePlugin.getInstance().getLogger().info(configAsset.readString());
                    LootSafePlugin.getInstance().getLogger().info(pluginDirectory.toString());
                    configAsset.copyToFile(pluginDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                    LootSafePlugin.getInstance().getLogger().error("Could not unpack the default config from the jar! Maybe your Minecraft server doesn't have write permissions?");
                    return;
                }
            } else {
                LootSafePlugin.getInstance().getLogger().error("Could not find the default config file in the jar! Did you open the jar and delete it?");
                return;
            }
        }
    }
}
