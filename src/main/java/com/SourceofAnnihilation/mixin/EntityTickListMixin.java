package com.SourceofAnnihilation.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(EntityTickList.class)
public abstract class EntityTickListMixin {

    @Shadow
    private Int2ObjectMap<Entity> passive;
    @Shadow
    private Int2ObjectMap<Entity> active;
    @Shadow
    @Nullable
    private Int2ObjectMap<Entity> iterated;
    @Unique
    private Int2ObjectMap<Entity> iterated2;

    @Unique
    public void forEach2(Consumer<Entity> p_156911_) {
        if (this.iterated2 != null) {
            throw new UnsupportedOperationException("Only one concurrent iteration supported");
        } else {
            this.iterated2 = this.active;

            try {
                for (Entity entity : this.active.values()) {
                    p_156911_.accept(entity);
                }
            } finally {
                this.iterated2 = null;
            }

        }
    }

    @Unique
    private void ensureActiveIsNotIterated2() {
        if (this.iterated2 == this.active) {
            this.passive.clear();

            for (Int2ObjectMap.Entry<Entity> entry : Int2ObjectMaps.fastIterable(this.active)) {
                this.passive.put(entry.getIntKey(), entry.getValue());
            }

            Int2ObjectMap<Entity> int2objectmap = this.active;
            this.active = this.passive;
            this.passive = int2objectmap;
        }
    }
    @Inject(method = "forEach",at = @At("HEAD"),cancellable = true)
    public void forEach(Consumer<Entity> p_156911_, CallbackInfo ci) {
        if (this.iterated != null) {
            this.forEach2(p_156911_);
            ci.cancel();
        }
    }
    @Inject(method = "add",at = @At("HEAD"),cancellable = true)
    public void add(Entity entity, CallbackInfo ci) {
        this.ensureActiveIsNotIterated2();
    }
    @Inject(method = "remove",at = @At("HEAD"))
    public void remove(Entity entity, CallbackInfo ci) {
        this.ensureActiveIsNotIterated2();
    }
}