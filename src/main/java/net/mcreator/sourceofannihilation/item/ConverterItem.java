package net.mcreator.sourceofannihilation.item;

import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ConverterItem extends Item {
    public ConverterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide) {return super.use(level, player, hand);}
        SourceOfAnnihilationModVariables.MapVariables vars = SourceOfAnnihilationModVariables.MapVariables.get(level);
        vars.attackmode = (vars.attackmode + 1) % 2;
        vars.syncData(level);
        switch (vars.attackmode) {
            case 0:
                player.sendSystemMessage(Component.translatable("text.removedmode"));
                break;
            case 1:
                player.sendSystemMessage(Component.translatable("text.killedmode"));
                break;
            default:
                vars.attackmode = 0;
                vars.syncData(level);
                player.sendSystemMessage(Component.translatable("text.resetmode"));
                break;
        }
        return super.use(level, player, hand);
    }
}
