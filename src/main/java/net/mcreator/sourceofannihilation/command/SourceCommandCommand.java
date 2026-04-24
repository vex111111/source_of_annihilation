
package net.mcreator.sourceofannihilation.command;

import org.checkerframework.checker.units.qual.s;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

import net.mcreator.sourceofannihilation.procedures.StopThreadCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.ResetchunksRangeCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.PrintProtectProcedure;
import net.mcreator.sourceofannihilation.procedures.PrintNoSpawnProcedure;
import net.mcreator.sourceofannihilation.procedures.NoHungryCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.NoFireCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.NoEffectCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.NoCapsCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.EventBusCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.ClearcompoundtagCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.ClearProtectCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.ClearNoSpawnCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.ClearAllProtectCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.ClearAllNoSpawnCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.AddProtectCommandProcedure;
import net.mcreator.sourceofannihilation.procedures.AddNoSpawnCommandProcedure;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

//@Mod.EventBusSubscriber
public class SourceCommandCommand {
//    @SubscribeEvent
//    public static void registerCommand(RegisterCommandsEvent event) {
//        event.getDispatcher()
//                .register(Commands.literal("sourcecommand").requires(s -> s.hasPermission(2)).then(Commands.literal("nospawnlist").then(Commands.literal("add").then(Commands.argument("addnospawn", EntityArgument.entities()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    AddNoSpawnCommandProcedure.execute(arguments);
//                    return 0;
//                }))).then(Commands.literal("clear").then(Commands.argument("clearnospawn", EntityArgument.entities()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    ClearNoSpawnCommandProcedure.execute(arguments);
//                    return 0;
//                }))).then(Commands.literal("clearAll").executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    ClearAllNoSpawnCommandProcedure.execute(entity.level());
//                    return 0;
//                })).then(Commands.literal("print").then(Commands.argument("player", EntityArgument.players()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    PrintNoSpawnProcedure.execute(arguments);
//                    return 0;
//                })))).then(Commands.literal("protectlist").then(Commands.literal("add").then(Commands.argument("addprotect", EntityArgument.entities()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    AddProtectCommandProcedure.execute(arguments);
//                    return 0;
//                }))).then(Commands.literal("clear").then(Commands.argument("clearprotect", EntityArgument.entities()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    ClearProtectCommandProcedure.execute(arguments);
//                    return 0;
//                }))).then(Commands.literal("clearAll").executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    ClearAllProtectCommandProcedure.execute(entity.level());
//                    return 0;
//                })).then(Commands.literal("print").then(Commands.argument("player", EntityArgument.players()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    PrintProtectProcedure.execute(arguments);
//                    return 0;
//                })))).then(Commands.literal("resetchunksrange").then(Commands.argument("range", DoubleArgumentType.doubleArg()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    ResetchunksRangeCommandProcedure.execute(world, arguments, entity);
//                    return 0;
//                }))).then(Commands.literal("stopthread").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    StopThreadCommandProcedure.execute(world, arguments, entity);
//                    return 0;
//                }))).then(Commands.literal("noeffect").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    NoEffectCommandProcedure.execute(world, arguments, entity);
//                    return 0;
//                }))).then(Commands.literal("nofire").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    NoFireCommandProcedure.execute(world, arguments, entity);
//                    return 0;
//                }))).then(Commands.literal("nocaps").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    NoCapsCommandProcedure.execute(world, arguments, entity);
//                    return 0;
//                }))).then(Commands.literal("clearcompoundtag").executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    ClearcompoundtagCommandProcedure.execute(entity);
//                    return 0;
//                })).then(Commands.literal("nohungry").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    NoHungryCommandProcedure.execute(world, arguments, entity);
//                    return 0;
//                }))).then(Commands.literal("event_bus").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(arguments -> {
//                    Level world = arguments.getSource().getUnsidedLevel();
//                    double x = arguments.getSource().getPosition().x();
//                    double y = arguments.getSource().getPosition().y();
//                    double z = arguments.getSource().getPosition().z();
//                    Entity entity = arguments.getSource().getEntity();
//                    if (entity == null && world instanceof ServerLevel _servLevel)
//                        entity = FakePlayerFactory.getMinecraft(_servLevel);
//                    Direction direction = Direction.DOWN;
//                    if (entity != null)
//                        direction = entity.getDirection();
//
//                    EventBusCommandProcedure.execute(arguments);
//                    return 0;
//                }))));
//    }
}
