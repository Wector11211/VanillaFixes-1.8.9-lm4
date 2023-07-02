package dev.wector11211.labymod.vanillafixes.listeners;

import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import dev.wector11211.labymod.vanillafixes.api.controllers.ClearTitles;

public class ServerDisconnectListener {

    private final VanillaFixesAddonConfiguration configuration;
    private final ClearTitles controller;

    public ServerDisconnectListener(VanillaFixesAddonConfiguration configuration, ClearTitles controller) {
        this.configuration = configuration;
        this.controller = controller;
    }

    @Subscribe
	public void onDisconnect(ServerDisconnectEvent event) {
		if(configuration.enabled().get() && configuration.infiniteTitleFix().get()) {
            controller.clearTitles();
		}
	}

}
