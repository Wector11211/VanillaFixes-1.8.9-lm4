package dev.wector11211.labymod.vanillafixes;

import dev.wector11211.labymod.vanillafixes.subsettings.F3CommandsSubSettings;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("settings")
public class VanillaFixesAddonConfiguration extends AddonConfig {
	@SwitchSetting
	private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

	@SettingSection("modules")
	@SwitchSetting
	private final ConfigProperty<Boolean> translucentSkinFix = new ConfigProperty<>(true);
	@SwitchSetting
	private final ConfigProperty<Boolean> focusMovementFix = new ConfigProperty<>(true);
	@SwitchSetting
	private final ConfigProperty<Boolean> dupeSoundFix = new ConfigProperty<>(true);
	private final F3CommandsSubSettings f3CommandsSubSettings = new F3CommandsSubSettings();
	@SwitchSetting
	private final ConfigProperty<Boolean> infiniteTitleFix = new ConfigProperty<>(true);
	@SwitchSetting
	private final ConfigProperty<Boolean> displayInventoryEffets = new ConfigProperty<>(true);

	@Override
	public ConfigProperty<Boolean> enabled() {
		return this.enabled;
	}

	public ConfigProperty<Boolean> translucentSkinFix() {
		return translucentSkinFix;
	}

	public ConfigProperty<Boolean> focusMovementFix() {
		return this.focusMovementFix;
	}

	public ConfigProperty<Boolean> dupeSoundFix() {
		return this.dupeSoundFix;
	}

	public F3CommandsSubSettings f3CommandsSubSettings() {
		return this.f3CommandsSubSettings;
	}

	public ConfigProperty<Boolean> infiniteTitleFix() {
		return infiniteTitleFix;
	}

	public ConfigProperty<Boolean> displayInventoryEffets() {
		return displayInventoryEffets;
	}

}
