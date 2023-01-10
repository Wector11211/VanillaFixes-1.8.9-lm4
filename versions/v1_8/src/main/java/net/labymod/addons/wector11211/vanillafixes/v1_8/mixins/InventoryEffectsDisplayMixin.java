package net.labymod.addons.wector11211.vanillafixes.v1_8.mixins;

import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddon;
import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.inject.LabyGuice;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.Collection;

@Mixin(InventoryEffectRenderer.class)
public class InventoryEffectsDisplayMixin {
	private static VanillaFixesAddonConfiguration configuration() {
		return LabyGuice.getInstance(VanillaFixesAddon.class).configuration();
	}

	@Redirect(method = "updateActivePotionEffects", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z"))
	public boolean displayEffects(Collection instance){
		if(configuration().displayInventoryEffets().get()) return true;
		return instance.isEmpty();
	}
}
