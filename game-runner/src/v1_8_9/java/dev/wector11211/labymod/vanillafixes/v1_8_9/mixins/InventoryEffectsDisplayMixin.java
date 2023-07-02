package dev.wector11211.labymod.vanillafixes.v1_8_9.mixins;

import java.util.Collection;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddon;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryEffectRenderer.class)
public class InventoryEffectsDisplayMixin {

    private static VanillaFixesAddonConfiguration configuration() {
        return VanillaFixesAddon.get().configuration();
    }

    @Redirect(
        method = "updateActivePotionEffects",
        at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z")
    )
	public boolean displayEffects(Collection instance){
		if(configuration().displayInventoryEffets().get()) return true;
		return instance.isEmpty();
	}
}
