package net.labymod.addons.wector11211.vanillafixes.v1_8.mixins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddon;
import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.inject.LabyGuice;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public abstract class DuplicateSoundFixMixin {

	private static VanillaFixesAddonConfiguration configuration() {
		return LabyGuice.getInstance(VanillaFixesAddon.class).configuration();
	}
	@Shadow	public abstract boolean isSoundPlaying(ISound sound);

	@Shadow @Final private Map<String, ISound> playingSounds;

	private final List<String> pausedSounds = new ArrayList<>();

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@Redirect(
		method = "pauseAllSounds",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundManager$SoundSystemStarterThread;pause(Ljava/lang/String;)V")
	)
	private void pauseSound(@Coerce SoundSystem soundSystem, String sound) {
		if(configuration().enabled().get() && configuration().dupeSoundFix().get()) {
			if (isSoundPlaying(playingSounds.get(sound))) {
				soundSystem.pause(sound);
				pausedSounds.add(sound);
			}
		} else {
			soundSystem.pause(sound);
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
