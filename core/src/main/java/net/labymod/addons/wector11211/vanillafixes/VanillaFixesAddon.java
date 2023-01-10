package net.labymod.addons.wector11211.vanillafixes;

import com.google.inject.Singleton;
import net.labymod.addons.wector11211.vanillafixes.events.ServerDisconnectEvent;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;

@Singleton
@AddonListener
public class VanillaFixesAddon extends LabyAddon<VanillaFixesAddonConfiguration> {

	@Override
	protected void enable() {
		this.registerSettingCategory();
		this.registerListener(ServerDisconnectEvent.class);
		logger().info("[VanillaFixes] Addon enabled");
	}

	@Override
	protected Class<VanillaFixesAddonConfiguration> configurationClass() {
		return VanillaFixesAddonConfiguration.class;
	}

}
