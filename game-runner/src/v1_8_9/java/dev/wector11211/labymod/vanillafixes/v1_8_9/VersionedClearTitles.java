package dev.wector11211.labymod.vanillafixes.v1_8_9;


import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import dev.wector11211.labymod.vanillafixes.api.controllers.ClearTitles;
import java.util.logging.Logger;

@Implements(ClearTitles.class)
public class VersionedClearTitles implements ClearTitles {
	@Override
	public void clearTitles() {
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
        gui.displayTitle(null, null, 0, 0, 0);
        Logger.getGlobal().info("[VanillaFixes] clearing titles");
	}
}
