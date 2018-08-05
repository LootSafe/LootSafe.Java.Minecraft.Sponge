package io.lootsafe.api.java.minecraft.sponge.Commands;

import com.flowpowered.math.vector.Vector3i;
import io.lootsafe.api.java.minecraft.sponge.LootSafePlugin;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adam Sanchez on 4/22/2018.
 */
public class StartCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        LootSafePlugin ls = LootSafePlugin.getInstance();
        if (src instanceof ConsoleSource)
            throw new CommandException(Text.of("This command can only be run by players"));

        Player player = ls.getGame().getServer().getPlayer(src.getName()).get();
        Vector3i blockLocation = player.getLocation().getBlockPosition();
        World world = player.getWorld();

        Entity skeleton = world.createEntity(EntityTypes.SKELETON, randomOffset(blockLocation));
        world.spawnEntity(skeleton);
        ls.addEntity(skeleton);


        return CommandResult.success();
    }

    public Vector3i randomOffset(Vector3i init) {
        return init.add(ThreadLocalRandom.current().nextInt(-3, 4),
                0,
                ThreadLocalRandom.current().nextInt(-3, 4));
    }
}
