package dev.wector11211.labymod.vanillafixes.v1_8_9.mixins.translucentskinfix;

import com.mojang.authlib.GameProfile;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddon;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntitySkullRenderer.class)
public class SkullBlockRenderMixin {

    private static VanillaFixesAddonConfiguration configuration() {
        return VanillaFixesAddon.get().configuration();
    }

	@Inject(method = "renderSkull", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableAlpha()V"))
	public void enableAlpha(float p_renderSkull_1_, float p_renderSkull_2_, float p_renderSkull_3_,
		EnumFacing p_renderSkull_4_, float p_renderSkull_5_, int p_renderSkull_6_,
		GameProfile p_renderSkull_7_, int p_renderSkull_8_, CallbackInfo ci){
		if(configuration().enabled().get() && configuration().translucentSkinFix().get()) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		}
	}
}
