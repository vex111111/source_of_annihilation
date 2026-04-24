package net.mcreator.sourceofannihilation;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class HackUtil {
    private static Boolean obfuscatedCache = null;
    public final static Unsafe UNSAFE = UnsafeAccess.UNSAFE;

    public static Field getField(Class<?> clazz, String mcpName, String srgName) {
        String finalFieldName;
        finalFieldName = isObfuscatedEnvironment() ? srgName : mcpName;
        Field field = ObfuscationReflectionHelper.findField(clazz, finalFieldName);
        return field;
    }

    public static boolean isObfuscatedEnvironment() {
        if (obfuscatedCache != null) {
            return obfuscatedCache;
        }
        try {
            Class<?> worldClass = Class.forName("net.minecraft.world.World");
            Field[] fields = worldClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().startsWith("field_")) {
                    obfuscatedCache = true;
                    return true;
                }
            }
            obfuscatedCache = false;
            return false;
        } catch (Exception e) {
            obfuscatedCache = true;
            return true;
        }
    }

    public static void unsafeSetFieldValue(Class<?> clazz, String mcpName, String srgName, Object object, Object value) {
        Field field = getField(clazz, mcpName, srgName);
        long offset = UNSAFE.objectFieldOffset(field);
        UNSAFE.putObject(object, offset, value);
    }

    public static Object unsafeGetFieldValue(Class<?> clazz, String mcpName, String srgName, Object object) {
        Field field = getField(clazz, mcpName, srgName);
        long offset = UNSAFE.objectFieldOffset(field);
        return UNSAFE.getObject(object, offset);
    }
}
