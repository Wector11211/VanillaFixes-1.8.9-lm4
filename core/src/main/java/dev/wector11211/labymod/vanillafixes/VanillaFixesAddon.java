package dev.wector11211.labymod.vanillafixes;

import dev.wector11211.labymod.vanillafixes.listeners.ServerDisconnectListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class VanillaFixesAddon extends LabyAddon<VanillaFixesAddonConfiguration> {

    private static VanillaFixesAddon instance;

    public VanillaFixesAddon() {
        VanillaFixesAddon.instance = this;
    }

    public static VanillaFixesAddon get() {
        return instance;
    }

	@Override
	protected void enable() {
		registerSettingCategory();

        registerListener(new ServerDisconnectListener(configuration()));

        logger().info("[VanillaFixes] Addon enabled");
	}

	@Override
	protected Class<VanillaFixesAddonConfiguration> configurationClass() {
		return VanillaFixesAddonConfiguration.class;
	}

}
