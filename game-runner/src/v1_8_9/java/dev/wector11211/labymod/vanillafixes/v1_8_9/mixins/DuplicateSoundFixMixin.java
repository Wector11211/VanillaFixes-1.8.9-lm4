package dev.wector11211.labymod.vanillafixes.v1_8_9.mixins;

import dev.wector11211.labymod.vanillafixes.VanillaFixesAddon;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(SoundManager.class)
public abstract class DuplicateSoundFixMixin {

    private static VanillaFixesAddonConfiguration configuration() {
        return VanillaFixesAddon.get().configuration();
    }

    @Shadow
    public abstract boolean isSoundPlaying(ISound sound);

	@Shadow @Final
    private Map<String, ISound> playingSounds;

	private final List<String> pausedSounds = new ArrayList<>();

	@Inject(
			method = "pauseAllSounds",
			at = @At(
					value = "INVOKE",
					target = "Lorg/apache/logging/log4j/Logger;debug(Lorg/apache/logging/log4j/Marker;Ljava/lang/String;[Ljava/lang/Object;)V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION,
			cancellable = true
	)
	private void patcher$pauseSound(CallbackInfo ci, Iterator playingSounds, String sound) {
		if (configuration().enabled().get() && configuration().dupeSoundFix().get()) {
			if (!isSoundPlaying(this.playingSounds.get(sound))) {
				ci.cancel();
				return;
			}
			pausedSounds.add(sound);
		}
	}

	@Redirect(
		method = "resumeAllSounds",
		at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;")
	)
	private Iterator<String> patcher$iterateOverPausedSounds(Set<String> keySet) {
		if(configuration().enabled().get() && configuration().dupeSoundFix().get()) {
			return pausedSounds.iterator();
		} else {
			return keySet.iterator();
		}
	}

	@Inject(method = "resumeAllSounds", at = @At("TAIL"))
	private void patcher$clearPausedSounds(CallbackInfo ci) {
		if(configuration().enabled().get()) {
			pausedSounds.clear();
		}
	}
}
