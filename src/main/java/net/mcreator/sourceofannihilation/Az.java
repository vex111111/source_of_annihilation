/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.sourceofannihilation as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.sourceofannihilation;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Az {
    private static final ChatFormatting[] colour = new ChatFormatting[] {
        ChatFormatting.LIGHT_PURPLE,ChatFormatting.DARK_PURPLE,ChatFormatting.DARK_PURPLE,ChatFormatting.DARK_PURPLE};
    private static final ChatFormatting[] colour2 = new ChatFormatting[] {
        ChatFormatting.GOLD,ChatFormatting.YELLOW,ChatFormatting.GREEN,ChatFormatting.DARK_GREEN,
        ChatFormatting.DARK_AQUA,ChatFormatting.AQUA,ChatFormatting.BLUE,ChatFormatting.DARK_BLUE,
        ChatFormatting.DARK_PURPLE,ChatFormatting.LIGHT_PURPLE,ChatFormatting.RED,ChatFormatting.DARK_RED};

    public static Component formatting(Component input, ChatFormatting[] colours, double delay) {
        long currentTime = System.currentTimeMillis();
        int offset = (int) Math.floor((currentTime & 0x3FFF) / delay) % colours.length;
        MutableComponent result = Component.literal("");
        for (int i = 0; i < input.getString().length(); i++) {
            char c = input.getString().charAt(i);
            result.append(Component.literal(String.valueOf(c))
                     .withStyle(colours[(colours.length + i - offset) % colours.length]));
        }
        return result;
    }

    public static Component makeColour(Component input) {
        return formatting(input, colour, 80.0D);
    }

    public static Component makeColour2(Component input) {
        return formatting(input, colour2, 80.0D);
    }
}