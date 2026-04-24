
package net.mcreator.sourceofannihilation.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.mcreator.sourceofannihilation.*;
import net.mcreator.sourceofannihilation.network.ListNetwork;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.mcreator.sourceofannihilation.procedures.SourceOfAnnihilationDangWuPinZaiBeiBaoZhongMeiKeFaShengProcedure;
import net.mcreator.sourceofannihilation.procedures.SourceOfAnnihilationDangYouJianDianJiKongQiShiShiTiDeWeiZhiProcedure;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SourceOfAnnihilationItem extends Item {
    static final Pair<String[], String[]> ANNIHILATION_SOURCE = new Pair<>(
            new String[]{
                    // 英文（晨雾·森林）
                    "Morning mist weaves through ancient trees. Dew drips from fern fronds. When the blade passes, the haze scatters into drifting lights—like the forest itself is breathing.",
                    // 日文（晨霧・森林）
                    "朝靄が古木の間を縫う。シダの葉から雫が落ちる。刃が通ると、靄は漂う光の粒に砕ける——森そのものが呼吸しているかのように。",
                    // 中文（晨雾·森林）
                    "晨雾缠绕古木，蕨叶上露珠滑落。剑锋掠过时，雾霭碎成飘浮的光点——仿佛森林本身在呼吸。",

                    // 英文（午后·原野）
                    "Afternoon light filters through a canopy of leaves, dappling the mossy ground. The hand gripping the sword feels a breeze from far hills, carrying scents of grass and cool earth.",
                    // 日文（午後・野原）
                    "午後の光が葉の天蓋を抜け、苔むした地面にまだら模様を描く。剣を握る手に、遠い丘から吹く風が届く——草と冷たい土の香りを乗せて。",
                    // 中文（午后·原野）
                    "午后阳光穿过叶幕，在青苔地上投下斑驳光影。握剑的手感到远方山丘吹来的微风，带着草木与清凉泥土的气息。",

                    // 英文（月夜·湖）
                    "Moonlight spills across a still lake. Cherry petals float on the water. As you swing, ripples spread—the moon shatters and reforms, like a dream that never truly wakes.",
                    // 日文（月夜・湖）
                    "月光が静かな湖に注ぐ。水面に桜の花びらが浮かぶ。振りかざすと、波紋が広がる——月は砕け、また再び結ばれる。決して覚めない夢のように。",
                    // 中文（月夜·湖）
                    "月光倾泻在寂静的湖面，樱瓣漂浮水上。挥剑时涟漪扩散——月影碎裂又重圆，像一场从未真正醒来的梦。"
            },
            new String[]{
                    "Ephemeral Edge",   // 英文
                    "幻夢の欠片",        // 日文
                    "虚境之翳"           // 中文
            }
    );
    public SourceOfAnnihilationItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        SourceOfAnnihilationDangYouJianDianJiKongQiShiShiTiDeWeiZhiProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity);
        return ar;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity entity, int i) {
        SourceOfAnnihilationDangYouJianDianJiKongQiShiShiTiDeWeiZhiProcedure.releaseUsing(world, entity.getX(), entity.getY(), entity.getZ(), entity);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        SourceOfAnnihilationDangWuPinZaiBeiBaoZhongMeiKeFaShengProcedure.execute(world, entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull Font getFont(@NotNull ItemStack stack, IClientItemExtensions.FontContext context) {
                return new SpecialFont();
            }
        });
    }

    @Override
    public Component getName(ItemStack cnm) {
        String[] names = ANNIHILATION_SOURCE.getSecond();
        int nameIndex = (int) (System.currentTimeMillis() / 5000) % names.length;
        return Az.makeColour2(Component.literal(names[nameIndex]));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);

        String[] descriptions = ANNIHILATION_SOURCE.getFirst();
        int descriptionIndex = (int) (System.currentTimeMillis() / 5000) % descriptions.length;

        Component descriptionText = Component.literal(descriptions[descriptionIndex]);
        Component coloredDescription = Az.makeColour2(descriptionText);

        list.add(Component.translatable(""));
        list.add(coloredDescription);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return Util.getUseAnim();
    }


    @Override
    public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
        return !p_43294_.isCreative();
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        int attackMode = SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).attackmode;
        if (entity != null) {
            if (entity instanceof Player _entity) {
                Remove.killPlayer(_entity, player);
                return false;
            }
            if(entity instanceof PartEntity<?> _entity) {
                if(_entity.getParent() instanceof LivingEntity living){
                    Remove.kill(living,player);
                    ListNetwork.syncToAllClients(player.level(), ListNetwork.killList);
                }
                Remove.Remove_Entity(_entity.getParent());
            }
            if (player.isShiftKeyDown()) {
                NoSpawnList.getInstance().addEntityToList(entity);
            }
            if(attackMode == 0) {
                Remove.Remove_Entity(entity);
            }else if(attackMode == 1){
                if(entity instanceof LivingEntity _entity) {
                    Remove.kill(_entity,player);
                    ListNetwork.syncToAllClients(player.level(), ListNetwork.killList);
                }
            }
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND) {
            if (modifiers.isEmpty()) {
                modifiers = HashMultimap.create();
            }
            modifiers.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(UUID.fromString("12398478-1d34-1a34-1234-123456789abc"),
                            "",
                            Double.POSITIVE_INFINITY,
                            AttributeModifier.Operation.ADDITION));
            try {
                Attribute reachAttr = ForgeMod.ENTITY_REACH.get();
                modifiers.put(reachAttr,
                        new AttributeModifier(UUID.fromString("12399378-1d34-1a34-1234-123456789abc"),
                                "",
                                Double.POSITIVE_INFINITY,
                                AttributeModifier.Operation.ADDITION));
            } catch (Exception e) {
                System.out.println("注意：ForgeMod.ENTITY_REACH属性不可用");
            }
        }

        return modifiers;
    }
}
