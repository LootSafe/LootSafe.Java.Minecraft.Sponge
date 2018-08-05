package io.lootsafe.api.java.minecraft.sponge.Commands;

import io.lootsafe.api.java.minecraft.sponge.LootSafePlugin;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * Created by Adam Sanchez on 4/22/2018.
 */
public class ReloadCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        LootSafePlugin.getInstance().reload();
        src.sendMessage(Text.of("Reloaded Successfully"));
        return CommandResult.success();
    }
}
