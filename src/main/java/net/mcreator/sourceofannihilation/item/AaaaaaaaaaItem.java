
package net.mcreator.sourceofannihilation.item;

import net.mcreator.sourceofannihilation.Backtrack;
import net.mcreator.sourceofannihilation.Remove;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import net.mcreator.sourceofannihilation.procedures.AaaaaaaaaaDangYouJianDianJiKongQiShiShiTiDeWeiZhiProcedure;
import net.mcreator.sourceofannihilation.procedures.AaaaaaaaaaDangShiTiBeiGaiWuPinJiZhongProcedure;

import java.util.List;

public class AaaaaaaaaaItem extends Item {
	public AaaaaaaaaaItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		AaaaaaaaaaDangYouJianDianJiKongQiShiShiTiDeWeiZhiProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity);
		Backtrack.save(world);
//		{
//            if (!entity.level().isClientSide() && entity.getServer() != null) {
//				entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
//						entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "kick @s");
//			}
//		}
		return ar;
	}

//	@Override
//	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
//		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
//		AaaaaaaaaaDangShiTiBeiGaiWuPinJiZhongProcedure.execute(entity.getY(), entity.getZ(), entity);
//		System.out.println("hurt"+entity.level());
//		return retval;
//	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
//		if(entity instanceof LivingEntity living) {
//			Remove.kill(living,player);
//		}
		if(player.level() instanceof ServerLevel serverLevel){
			Backtrack.serverRestore(serverLevel);
		}
		if(player.level() instanceof ClientLevel clientLevel){
			Backtrack.clientRrestore(clientLevel);
		}
//		{
//			if (!entity.level().isClientSide() && entity.getServer() != null) {
//				entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
//						entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "kick @s");
//			}
//		}
//		System.out.println("click" + entity.level());
		return false;
	}
}
