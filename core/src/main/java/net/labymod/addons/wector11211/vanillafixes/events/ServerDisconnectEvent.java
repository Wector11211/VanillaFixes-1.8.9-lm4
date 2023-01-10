package net.labymod.addons.wector11211.vanillafixes.events;

import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddon;
import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.addons.wector11211.vanillafixes.controller.ClearTitlesController;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkDisconnectEvent;
import net.labymod.api.inject.LabyGuice;

public class ServerDisconnectEvent {

	private static VanillaFixesAddonConfiguration configuration() {
		return LabyGuice.getInstance(VanillaFixesAddon.class).configuration();
	}

	@Subscribe
	public void onDisconnect(NetworkDisconnectEvent event) {
		if(configuration().enabled().get() && configuration().infiniteTitleFix().get()) {
			LabyGuice.getInstance(ClearTitlesController.class).clearTitles();
//			LabyGuice.getInstance(ClearTitlesInterface.class).clearTitles();
		}
	}

}
