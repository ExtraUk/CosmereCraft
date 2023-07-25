package com.extra.cosmerecraft.command;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.network.ModMessages;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.NonNullConsumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class PowerCommand {
    private static final DynamicCommandExceptionType ERROR_CANT_ADD = new DynamicCommandExceptionType(s -> Component.translatable("commands.cosmerecraft.err_add", s));
    private static final DynamicCommandExceptionType ERROR_CANT_REMOVE = new DynamicCommandExceptionType(s -> Component.translatable("commands.cosmerecraft.err_remove", s));

    private static Predicate<CommandSourceStack> permissions(int level) {
        return (player) -> player.hasPermission(level);
    }

    private static Collection<ServerPlayer> sender(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return Collections.singleton(ctx.getSource().getPlayerOrException());
    }

    private static Collection<ServerPlayer> target(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return EntityArgument.getPlayers(ctx, "targets");
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("cosmere").requires(permissions(0));
        root.then(Commands
                .literal("get")
                .requires(permissions(0))
                .executes(ctx -> handleMultiPlayer(ctx, sender(ctx), PowerCommand::getPowers))
                .then(Commands.argument("targets", EntityArgument.players()).executes(ctx -> handleMultiPlayer(ctx, target(ctx), PowerCommand::getPowers))));

        root.then(Commands
                .literal("add")
                .requires(permissions(2))
                .then(Commands
                        .argument("type", FeruchemyPowerType.INSTANCE)
                        .executes(ctx -> handleMultiPlayer(ctx, sender(ctx), PowerCommand::addPower))
                        .then(Commands
                                .argument("targets", EntityArgument.players())
                                .executes(ctx -> handleMultiPlayer(ctx, target(ctx), PowerCommand::addPower)))));

        root.then(Commands
                .literal("remove")
                .requires(permissions(2))
                .then(Commands
                        .argument("type", FeruchemyPowerType.INSTANCE)
                        .executes(ctx -> handleMultiPlayer(ctx, sender(ctx), PowerCommand::removePower))
                        .then(Commands
                                .argument("targets", EntityArgument.players())
                                .executes(ctx -> handleMultiPlayer(ctx, target(ctx), PowerCommand::removePower)))));


        LiteralCommandNode<CommandSourceStack> command = dispatcher.register(root);

        dispatcher.register(Commands.literal("ap").requires(permissions(0)).redirect(command));
    }


    private static int handleMultiPlayer(CommandContext<CommandSourceStack> ctx,
                                         Collection<ServerPlayer> players,
                                         CheckedBiCon<CommandContext<CommandSourceStack>, ServerPlayer> toApply) throws CommandSyntaxException {
        int i = 0;
        for (ServerPlayer p : players) {
            toApply.accept(ctx, p);
            i++;
        }

        return i;
    }

    private static void getPowers(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        MutableComponent component = Component.translatable("commands.cosmerecraft.getpowers").append(player.getDisplayName().getString() + ": ");
        player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if (data.isFeruchemist()) {
                component.append(Component.translatable("commands.cosmerecraft.all"));
            } else if (data.isUninvested()) {
                component.append(Component.translatable("commands.cosmerecraft.none"));
            } else {
                for (Metal mt : Metal.values()) {
                    if (data.hasPower(mt)) {
                        component.append(Component.translatable("metals.cosmerecraft."+mt.getName()).append(", "));
                    }
                }
            }
        });

        ctx.getSource().sendSuccess(component, true);
    }

    private static void addPower(CommandContext<CommandSourceStack> ctx, ServerPlayer player) throws CommandSyntaxException {
        handlePowerChange(ctx, player, IFeruchemyData::setFeruchemist, data -> Predicate.not(data::hasPower), mt -> (data -> data.addPower(mt)), ERROR_CANT_ADD::create,
                "commands.cosmerecraft.addpower");
    }

    private static void removePower(CommandContext<CommandSourceStack> ctx, ServerPlayer player) throws CommandSyntaxException {
        handlePowerChange(ctx, player, IFeruchemyData::setUninvested, (data) -> data::hasPower, (mt) -> (data -> data.revokePower(mt)), ERROR_CANT_REMOVE::create,
                "commands.cosmerecraft.removepower");
    }

    private static void handlePowerChange(CommandContext<CommandSourceStack> ctx,
                                          ServerPlayer player,
                                          NonNullConsumer<IFeruchemyData> all,
                                          Function<IFeruchemyData, Predicate<Metal>> filterFunction,
                                          Function<Metal, NonNullConsumer<IFeruchemyData>> single,
                                          Function<String, CommandSyntaxException> exception,
                                          String success) throws CommandSyntaxException {

        String type = ctx.getArgument("type", String.class);

        if (type.equalsIgnoreCase("all")) {
            player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(all);
        } else {
            Predicate<Metal> filter = player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).map(filterFunction::apply).orElse((m) -> false);

            if (type.equalsIgnoreCase("random")) {
                List<Metal> metalList = Arrays.asList(Metal.values());
                Collections.shuffle(metalList);
                Metal mt = metalList.stream().filter(filter).findFirst().orElseThrow(() -> exception.apply(type));
                player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(single.apply(mt));
            } else {
                Metal mt = Metal.valueOf(type.toUpperCase());
                if (filter.test(mt)) {
                    player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(single.apply(mt));
                } else {
                    throw exception.apply(type);
                }
            }
        }
        ModMessages.sync(player);

        ctx.getSource().sendSuccess(Component.translatable(success, player.getDisplayName(), type), true);

    }

    @FunctionalInterface
    private interface CheckedBiCon<T, U> {
        void accept(T t, U u) throws CommandSyntaxException;
    }

}
