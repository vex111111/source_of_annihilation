package com.SourceofAnnihilation.mixin;

import net.mcreator.sourceofannihilation.ProtectList;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Inject(method = "shouldShowDeathScreen",at = @At("HEAD"),cancellable = true)
    public void shouldShowDeathScreen(CallbackInfoReturnable<Boolean> cir){
        LocalPlayer localPlayer = (LocalPlayer) (Object) this;
        if(ProtectList.getInstance().containsEntityType(localPlayer)){
            cir.setReturnValue(false);
        }
    }
}
