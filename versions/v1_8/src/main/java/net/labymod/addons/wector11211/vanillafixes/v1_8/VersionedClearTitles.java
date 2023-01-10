package net.labymod.addons.wector11211.vanillafixes.v1_8;

import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddon;
import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.addons.wector11211.vanillafixes.controller.ClearTitlesController;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.models.Implement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

@Implement(ClearTitlesController.class)
public class VersionedClearTitles implements ClearTitlesController{

	private static VanillaFixesAddonConfiguration configuration() {
		return LabyGuice.getInstance(VanillaFixesAddon.class).configuration();
	}
	@Override
	public void clearTitles() {
		if(configuration().enabled().get() && configuration().infiniteTitleFix().get()) {
			GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
			gui.displayTitle("", "", 0,0, 0);
		}
	}
}
