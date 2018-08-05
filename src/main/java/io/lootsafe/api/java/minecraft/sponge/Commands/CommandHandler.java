package io.lootsafe.api.java.minecraft.sponge.Commands;

import io.lootsafe.api.java.minecraft.sponge.LootSafePlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * Created by Adam Sanchez on 4/22/2018.
 */
public class CommandHandler {

    public static void registerCommands(){
        CommandSpec start = CommandSpec.builder()
                .description(Text.of("Run to begin  the trial"))
                .permission("lootsafe.admin.start")
                .executor(new StartCommand())
                .build();
        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reload the LootSafe Service"))
                .permission("lootsafe.admin.reload")
                .executor(new ReloadCommand())
                .build();

        Sponge.getCommandManager().register(LootSafePlugin.getInstance(), start, "start", "lootsafestart");
        Sponge.getCommandManager().register(LootSafePlugin.getInstance(), reload, "lsreload" , "lootsafereload");
    }
}
