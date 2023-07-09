package dev.wector11211.labymod.vanillafixes.listeners;

import dev.wector11211.labymod.vanillafixes.VanillaFixesAddon;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class ServerDisconnectListener {

    private final VanillaFixesAddonConfiguration configuration;

    public ServerDisconnectListener(VanillaFixesAddonConfiguration configuration) {
        this.configuration = configuration;
    }

    @Subscribe
	public void onDisconnect(ServerDisconnectEvent event) {
		if(configuration.enabled().get() && configuration.infiniteTitleFix().get()) {
            Laby.labyAPI().minecraft().clearTitle();
            VanillaFixesAddon.get().logger().info("[VanillaFixes] clearing titles");
		}
	}

}
