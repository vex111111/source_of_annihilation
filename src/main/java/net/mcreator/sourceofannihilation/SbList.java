package net.mcreator.sourceofannihilation;

import com.SourceofAnnihilation.mixin.ClientLevelProxy;
import com.SourceofAnnihilation.mixin.EntityTickListProxy;
import com.SourceofAnnihilation.mixin.ServerLevelProxy;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTickList;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SbList extends EntityTickList {
    private Int2ObjectMap<Entity> active = new Int2ObjectLinkedOpenHashMap<>();
    private Int2ObjectMap<Entity> passive = new Int2ObjectLinkedOpenHashMap<>();
    @Nullable
    private Int2ObjectMap<Entity> iterated;

    private void ensureActiveIsNotIterated() {
        if (this.iterated == this.active) {
            this.passive.clear();

            for (Int2ObjectMap.Entry<Entity> entry : Int2ObjectMaps.fastIterable(this.active)) {
                this.passive.put(entry.getIntKey(), entry.getValue());
            }

            Int2ObjectMap<Entity> int2objectmap = this.active;
            this.active = this.passive;
            this.passive = int2objectmap;
        }

    }

    public void add(Entity p_156909_) {
        this.ensureActiveIsNotIterated();
        this.active.put(p_156909_.getId(), p_156909_);
    }

    public void remove(Entity p_156913_) {
        this.ensureActiveIsNotIterated();
        this.active.remove(p_156913_.getId());
    }

    public boolean contains(Entity p_156915_) {
        return this.active.containsKey(p_156915_.getId());
    }

    public void forEach(Consumer<Entity> consumer) {
        this.iterated = this.active;
        try {
            var iterator = Int2ObjectMaps.fastIterable(this.active).iterator();
            while (iterator.hasNext()) {
                Int2ObjectMap.Entry<Entity> entry = iterator.next();
                Entity entity = entry.getValue();
                if (entity != null) {
                    consumer.accept(entity);
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {

        }
        finally {
            this.iterated = null;
        }
    }
}