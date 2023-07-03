package dev.wector11211.labymod.vanillafixes.v1_8_9.mixins.betterf3fix;

import java.util.List;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddon;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.Laby;
import net.labymod.api.event.client.chat.ChatClearEvent;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public class F3DClearChatMixin {

    private static VanillaFixesAddonConfiguration configuration() {
        return VanillaFixesAddon.get().configuration();
    }

	@Shadow
	@Final
	private List<ChatLine> drawnChatLines;

	@Inject(method = "clearChatMessages", at = @At("HEAD"), cancellable = true)
	public void onlyClearChat(CallbackInfo ci) {
        if(!configuration().enabled().get() || !configuration().f3CommandsSubSettings().enabled().get()) return;
        Laby.fireEvent(new ChatClearEvent(false));
		this.drawnChatLines.clear();
        ci.cancel();
	}

}

