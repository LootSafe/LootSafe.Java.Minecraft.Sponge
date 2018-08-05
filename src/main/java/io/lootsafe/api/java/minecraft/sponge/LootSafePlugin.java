package io.lootsafe.api.java.minecraft.sponge;

import com.google.inject.Inject;
import io.lootsafe.api.Requests.NodeHandler;
import io.lootsafe.api.ServiceProvider;
import io.lootsafe.api.java.minecraft.sponge.Commands.CommandHandler;
import io.lootsafe.api.java.minecraft.sponge.Utils.CC;
import io.lootsafe.api.java.minecraft.sponge.Utils.ConfigHandler;
import io.lootsafe.api.java.minecraft.sponge.Utils.U;
import javafx.scene.control.TextFormatter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import javax.json.JsonObject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Adam Sanchez on 4/21/2018.
 */
@SuppressWarnings("unused")
@org.spongepowered.api.plugin.Plugin(id = "lootsafe", name = "LootSafe", version = "1.0", description = "LootSafe API Client")
public class LootSafePlugin {

    @Inject
    private Game game;

    public Game getGame() {
        return game;
    }

    @Inject
    private PluginContainer plugin;

    public PluginContainer getPlugin() {
        return plugin;
    }

    @Inject
    Logger logger;

    public Logger getLogger() {
        return logger;
    }

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;
    private CommentedConfigurationNode rootNode;


    private static LootSafePlugin instance;

    public static LootSafePlugin getInstance() {
        return instance;
    }

    private static ServiceProvider sv;

    public static ServiceProvider getServiceProvider() {
        return sv;
    }

    private NodeHandler nh;

    private boolean debug = true;

    private Set<Entity> trophyEntity = new HashSet<>();

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        instance = this;
        CommandHandler.registerCommands();
        ConfigHandler.initConfig(defaultConfig);
        reload();
    }

    @Listener
    public void onPostInitialization(GamePostInitializationEvent event) {
        instance = this;
    }

    @Listener
    public void onServerStart(GameInitializationEvent event) {

    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {

    }

    @Listener
    public void onEntityKilled(DestructEntityEvent.Death event) {
        Optional<EntityDamageSource> optDamageSource = event.getCause().first(EntityDamageSource.class);
        if (optDamageSource.isPresent() && trophyEntity.contains(event.getTargetEntity())) {
            trophyEntity.remove(event.getTargetEntity());
            EntityDamageSource damageSource = optDamageSource.get();
            Entity killer = damageSource.getSource();
            nh.test();
            if (killer instanceof Player) {
                U.info(CC.YELLOW + "Trophy Entity Has Been Killed by " + CC.CYAN + ((Player) killer).getName());
                try {
                    JsonObject response = nh.postSpawnItem(rootNode.getNode("config", "test-item").getString(),rootNode.getNode("config","test-item").getString());
                    if (response.getInt("status") == 200) {
                        ((Player) killer).sendMessage(
                                Text.builder("Congratulations! you have been awarded a potion in your ethereum wallet!")
                                        .color(TextColors.GREEN).build());
                        ((Player) killer).sendTitle(Title.of(
                                Text.builder("Monster Hunter Ethereum Trophy")
                                        .color(TextColors.GOLD)
                                        .build()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void reload() {
        try {
            rootNode = loader.load();
            debug = rootNode.getNode("config", "debug").getBoolean();
            sv = new ServiceProvider.ServiceBuilder()
                    .withVersion(rootNode.getNode("config", "version").getString())
                    .withHost(rootNode.getNode("config", "host").getString())
                    .withOTP(rootNode.getNode("config", "otp").getString())
                    .withPrivateKey(rootNode.getNode("config", "private-key").getString())
                    .withDebug(rootNode.getNode("config", "debug").getBoolean())
                    .build();
            sv.startService();
            if (sv.isWorking()) {
                nh = sv.getNodeHandler();
                nh.test();
            } else {
                throw new RuntimeException("Could not start up the LootSafe Service! Is your config in order?");
            }
            U.info(CC.CYAN + "LootSafe loaded correctly.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEntity(Entity entity) {
        trophyEntity.add(entity);
    }

    public boolean isDebug() {
        return debug;
    }

    public static Optional<UserStorageService> getUserStorage() {
        return Sponge.getServiceManager().provide(UserStorageService.class);
    }
}
