package net.labymod.addons.wector11211.vanillafixes.v1_8.mixins;

import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddon;
import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.inject.LabyGuice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class FocusMovementFixMixin {
	private static VanillaFixesAddonConfiguration configuration() {
		return LabyGuice.getInstance(VanillaFixesAddon.class).configuration();
	}

	@Inject(method = "setIngameFocus", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MouseHelper;grabMouseCursor()V"))
	public void updateKeyBindState(CallbackInfo ci){
		if(configuration().enabled().get() && configuration().focusMovementFix().get()) {
			for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings) {
				try {
					KeyBinding.setKeyBindState(keyBinding.getKeyCode(),
						keyBinding.getKeyCode() < 256 && Keyboard.isKeyDown(
							keyBinding.getKeyCode()));
				} catch (IndexOutOfBoundsException ignored) {
					;
				}
			}
		}
	}

}
