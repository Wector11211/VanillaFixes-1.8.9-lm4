package net.labymod.addons.wector11211.vanillafixes.v1_8.mixins.translucentskinfix;

import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddon;
import net.labymod.addons.wector11211.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.inject.LabyGuice;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderPlayer.class)
public abstract class PlayerRenderMixin {

	private static VanillaFixesAddonConfiguration configuration() {
		return LabyGuice.getInstance(VanillaFixesAddon.class).configuration();
	}
	@Shadow
	protected abstract void setModelVisibilities(AbstractClientPlayer p_setModelVisibilities_1_);

	@Redirect(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;setModelVisibilities(Lnet/minecraft/client/entity/AbstractClientPlayer;)V"))
	public void enableAlpha(RenderPlayer instance, AbstractClientPlayer p_setModelVisibilities_1_){
		if(configuration().enabled().get() && configuration().translucentSkinFix().get()) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			this.setModelVisibilities(p_setModelVisibilities_1_);
			GlStateManager.disableBlend();
		}else{
			this.setModelVisibilities(p_setModelVisibilities_1_);
		}
	}

}
