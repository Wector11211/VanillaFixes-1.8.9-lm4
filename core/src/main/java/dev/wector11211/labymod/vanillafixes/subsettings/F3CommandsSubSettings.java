package dev.wector11211.labymod.vanillafixes.subsettings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class F3CommandsSubSettings extends Config {

	@ParentSwitch
	@SwitchSetting
	private ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

	@SwitchSetting
	private ConfigProperty<Boolean> debugF3CommandMessage = new ConfigProperty<>(true);

	public ConfigProperty<Boolean> enabled() {
		return this.enabled;
	}

	public ConfigProperty<Boolean> debugF3CommandMessage() {
		return this.debugF3CommandMessage;
	}
}
