package net.labymod.addons.wector11211.vanillafixes.v1_8.mixins.betterf3fix;

import net.labymod.api.Laby;
import net.labymod.api.event.client.chat.ChatClearEvent;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

@Mixin(GuiNewChat.class)
public class F3DClearChatMixin implements IGuiNewChat {

	@Shadow
	@Final
	private List<ChatLine> drawnChatLines;

	@Shadow
	@Final
	private List<ChatLine> chatLines;


	@Override
	public void onlyClearChat()
	{
		if (Laby.fireEvent(new ChatClearEvent(true)).isCancelled()) {
			return;
		}
		this.drawnChatLines.clear();
		this.chatLines.clear();
	}

}

