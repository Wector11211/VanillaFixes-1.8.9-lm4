package dev.wector11211.labymod.vanillafixes;

import dev.wector11211.labymod.vanillafixes.api.controllers.ClearTitles;
import dev.wector11211.labymod.vanillafixes.listeners.ServerDisconnectListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.reference.annotation.Referenceable;
import javax.inject.Inject;

@AddonMain
@Referenceable
public class VanillaFixesAddon extends LabyAddon<VanillaFixesAddonConfiguration> {

    private final ClearTitles clearTitlesController;
    private static VanillaFixesAddon instance;

    @Inject
    public VanillaFixesAddon(ClearTitles clearTitlesController) {
        VanillaFixesAddon.instance = this;
        this.clearTitlesController = clearTitlesController;
    }

    public static VanillaFixesAddon get() {
        return instance;
    }

	@Override
	protected void enable() {
		registerSettingCategory();

        registerListener(new ServerDisconnectListener(configuration(), clearTitlesController));

        logger().info("[VanillaFixes] Addon enabled");
	}

	@Override
	protected Class<VanillaFixesAddonConfiguration> configurationClass() {
		return VanillaFixesAddonConfiguration.class;
	}

}
