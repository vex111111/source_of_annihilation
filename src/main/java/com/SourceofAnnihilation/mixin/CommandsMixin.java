package com.SourceofAnnihilation.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.mcreator.sourceofannihilation.procedures.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class CommandsMixin {

    @Final
    @Shadow
    private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void registerCommands(Commands.CommandSelection selection, CommandBuildContext context, CallbackInfo ci) {
        source_of_annihilation$registerSourceCommands(this.dispatcher);
    }

    @Unique
    private void source_of_annihilation$registerSourceCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher
                .register(Commands.literal("sourcecommand").requires(s -> s.hasPermission(2)).then(Commands.literal("nospawnlist").then(Commands.literal("add").then(Commands.argument("addnospawn", EntityArgument.entities()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    AddNoSpawnCommandProcedure.execute(arguments);
                    return 0;
                }))).then(Commands.literal("clear").then(Commands.argument("clearnospawn", EntityArgument.entities()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    ClearNoSpawnCommandProcedure.execute(arguments);
                    return 0;
                }))).then(Commands.literal("clearAll").executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    ClearAllNoSpawnCommandProcedure.execute(entity.level());
                    return 0;
                })).then(Commands.literal("print").then(Commands.argument("player", EntityArgument.players()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    PrintNoSpawnProcedure.execute(arguments);
                    return 0;
                })))).then(Commands.literal("protectlist").then(Commands.literal("add").then(Commands.argument("addprotect", EntityArgument.entities()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    AddProtectCommandProcedure.execute(arguments);
                    return 0;
                }))).then(Commands.literal("clear").then(Commands.argument("clearprotect", EntityArgument.entities()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    ClearProtectCommandProcedure.execute(arguments);
                    return 0;
                }))).then(Commands.literal("clearAll").executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    ClearAllProtectCommandProcedure.execute(entity.level());
                    return 0;
                })).then(Commands.literal("print").then(Commands.argument("player", EntityArgument.players()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    PrintProtectProcedure.execute(arguments);
                    return 0;
                })))).then(Commands.literal("resetchunksrange").then(Commands.argument("range", DoubleArgumentType.doubleArg()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    ResetchunksRangeCommandProcedure.execute(world, arguments, entity);
                    return 0;
                }))).then(Commands.literal("stopthread").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    StopThreadCommandProcedure.execute(world, arguments, entity);
                    return 0;
                }))).then(Commands.literal("noeffect").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    NoEffectCommandProcedure.execute(world, arguments, entity);
                    return 0;
                }))).then(Commands.literal("nofire").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    NoFireCommandProcedure.execute(world, arguments, entity);
                    return 0;
                }))).then(Commands.literal("nocaps").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    NoCapsCommandProcedure.execute(world, arguments, entity);
                    return 0;
                }))).then(Commands.literal("clearcompoundtag").executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    ClearcompoundtagCommandProcedure.execute(entity);
                    return 0;
                })).then(Commands.literal("nohungry").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    NoHungryCommandProcedure.execute(world, arguments, entity);
                    return 0;
                }))).then(Commands.literal("event_bus").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    double x = arguments.getSource().getPosition().x();
                    double y = arguments.getSource().getPosition().y();
                    double z = arguments.getSource().getPosition().z();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    Direction direction = Direction.DOWN;
                    if (entity != null)
                        direction = entity.getDirection();

                    EventBusCommandProcedure.execute(arguments);
                    return 0;
                }))));
    }
}