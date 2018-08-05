package io.lootsafe.api.java.minecraft.sponge.Utils;

import io.lootsafe.api.java.minecraft.sponge.LootSafePlugin;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by adam_ on 01/22/17.
 */
public class U {
    public static void info(String info) {
        LootSafePlugin.getInstance().getLogger().info(info + CC.RESET);
    }

    public static void debug(String debug) {
        if (LootSafePlugin.getInstance().isDebug()) {
            LootSafePlugin.getInstance().getLogger().info(debug + CC.RESET);
        }
    }

    public static void error(String error) {
        LootSafePlugin.getInstance().getLogger().error(error + CC.RESET);
    }

    public static void error(String error, Exception e) {
        LootSafePlugin.getInstance().getLogger().error(error + CC.RESET, e);
    }

    public static void warn(String warn) {
        LootSafePlugin.getInstance().getLogger().warn(warn + CC.RESET);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getName(UUID player) {
        Optional<UserStorageService> userStorage = LootSafePlugin.getUserStorage();
        return userStorage.get().get(player).get().getName();
    }

    public static UUID getIdFromName(String name) {
        Optional<UserStorageService> userStorage = LootSafePlugin.getUserStorage();
        if ((userStorage.get().get(name).isPresent())) {
            return userStorage.get().get(name).get().getUniqueId();
        } else {
            return null;
        }
    }


}
