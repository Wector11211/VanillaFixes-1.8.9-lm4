package dev.wector11211.labymod.vanillafixes.v1_8_9.mixins;

import dev.wector11211.labymod.vanillafixes.VanillaFixesAddon;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
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
        return VanillaFixesAddon.get().configuration();
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
