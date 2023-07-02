package dev.wector11211.labymod.vanillafixes.v1_8_9.mixins;

import dev.wector11211.labymod.vanillafixes.VanillaFixesAddon;
import dev.wector11211.labymod.vanillafixes.VanillaFixesAddonConfiguration;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumDifficulty;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class F3CommandsFixMixin {

    private boolean actionKeyF3;
	@Shadow
	public EffectRenderer effectRenderer;
    @Shadow
	public abstract void dispatchKeypresses();
	@Shadow
	public GuiScreen currentScreen;
	@Shadow
	public abstract void displayInGameMenu();
	@Shadow
	public GameSettings gameSettings;
	@Shadow
	protected abstract void updateDebugProfilerName(int par1);
	@Shadow
	public abstract void refreshResources();
	@Shadow
	public GuiIngame ingameGUI;
	@Shadow
	public PlayerControllerMP playerController;

    private static VanillaFixesAddonConfiguration configuration() {
        return VanillaFixesAddon.get().configuration();
    }

	private void debugFeedbackTranslated(String untranslatedTemplate, Object... objs)
	{
		if(configuration().f3CommandsSubSettings().debugF3CommandMessage().get()) {
			this.ingameGUI.getChatGUI().printChatMessage((new ChatComponentText("")).appendSibling(
					(new ChatComponentTranslation("vanillafixes.debug.prefix")).setChatStyle(
						(new ChatStyle()).setColor(EnumChatFormatting.YELLOW).setBold(true)))
				.appendText(" ")
				.appendSibling(new ChatComponentTranslation(untranslatedTemplate, objs)));
		}
	}

	private void runTickKeyboard()
	{
		while (Keyboard.next())
		{
			int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

			if (this.debugCrashKeyPressTime > 0L)
			{
				if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L)
				{
					throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
				}

				if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61))
				{
					this.debugCrashKeyPressTime = -1L;
				}
			}
			else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61))
			{
				this.actionKeyF3 = true;
				this.debugCrashKeyPressTime = getSystemTime();
			}

			this.dispatchKeypresses();

			if (this.currentScreen != null)
			{
				this.currentScreen.handleKeyboardInput();
			}

			boolean flag = Keyboard.getEventKeyState();

			if (flag)
			{
				if (i == 62 && this.entityRenderer != null)
				{
					this.entityRenderer.switchUseShader();
				}

				boolean flag1 = false;

				if (this.currentScreen == null)
				{
					if (i == 1)
					{
						this.displayInGameMenu();
					}

					flag1 = Keyboard.isKeyDown(61) && this.processKeyF3(i);
					this.actionKeyF3 |= flag1;

					if (i == 59)
					{
						this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
					}
				}

				if (flag1)
				{
					KeyBinding.setKeyBindState(i, false);
				}
				else
				{
					KeyBinding.setKeyBindState(i, true);
					KeyBinding.onTick(i);
				}

				if (this.gameSettings.showDebugProfilerChart)
				{
					if (i == 11)
					{
						this.updateDebugProfilerName(0);
					}

					for (int j = 0; j < 9; ++j)
					{
						if (i == 2 + j)
						{
							this.updateDebugProfilerName(j + 1);
						}
					}
				}
			}
			else
			{
				KeyBinding.setKeyBindState(i, false);

				if (i == 61)
				{
					if (this.actionKeyF3)
					{
						this.actionKeyF3 = false;
					}
					else
					{
						this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
						this.gameSettings.showDebugProfilerChart = this.gameSettings.showDebugInfo && GuiScreen.isShiftKeyDown();
						this.gameSettings.showLagometer = this.gameSettings.showDebugInfo && GuiScreen.isAltKeyDown();
					}
				}
			}
		}

		this.processKeyBinds();
	}

	private boolean processKeyF3(int auxKey)
	{
		if (auxKey == 30)
		{
			this.renderGlobal.loadRenderers();
			this.debugFeedbackTranslated("vanillafixes.debug.reload_chunks.message");
			return true;
		}
		else if (auxKey == 48)
		{
			boolean flag1 = !this.renderManager.isDebugBoundingBox();
			this.renderManager.setDebugBoundingBox(flag1);
			this.debugFeedbackTranslated(flag1 ? "vanillafixes.debug.show_hitboxes.on" : "vanillafixes.debug.show_hitboxes.off");
			return true;
		}
		else if (auxKey == 32)
		{
			if (this.ingameGUI != null)
			{
				this.ingameGUI.getChatGUI().clearChatMessages();
			}

			return true;
		}
		else if (auxKey == 33)
		{
			this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
			this.debugFeedbackTranslated("vanillafixes.debug.cycle_renderdistance.message", this.gameSettings.renderDistanceChunks);
			return true;
		}
		else if (auxKey == 34)
		{
//			boolean flag = this.debugRenderer.toggleChunkBorders();
			boolean flag = true;
			this.debugFeedbackTranslated(flag ? "vanillafixes.debug.chunk_boundaries.on" : "vanillafixes.debug.chunk_boundaries.off");
			return true;
		}
		else if (auxKey == 35)
		{
			this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
			this.debugFeedbackTranslated(this.gameSettings.advancedItemTooltips ? "vanillafixes.debug.advanced_tooltips.on" : "vanillafixes.debug.advanced_tooltips.off");
			this.gameSettings.saveOptions();
			return true;
		}
		else if (auxKey == 49)
		{
			if (this.playerController.isInCreativeMode())
			{
				this.thePlayer.sendChatMessage("/gamemode spectator");
			}
			else if (this.thePlayer.isSpectator())
			{
				this.thePlayer.sendChatMessage("/gamemode creative");
			}

			return true;
		}
		else if (auxKey == 25)
		{
			this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
			this.gameSettings.saveOptions();
			this.debugFeedbackTranslated(this.gameSettings.pauseOnLostFocus ? "vanillafixes.debug.pause_focus.on" : "vanillafixes.debug.pause_focus.off");
			return true;
		}
		else if (auxKey == 16)
		{
			this.debugFeedbackTranslated("vanillafixes.debug.help.message");
			GuiNewChat guinewchat = this.ingameGUI.getChatGUI();
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.reload_chunks.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.show_hitboxes.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.clear_chat.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.cycle_renderdistance.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.chunk_boundaries.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.advanced_tooltips.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.creative_spectator.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.pause_focus.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.help.help"));
			guinewchat.printChatMessage(new ChatComponentTranslation("vanillafixes.debug.reload_resourcepacks.help"));
			return true;
		}
		else if (auxKey == 20)
		{
			this.debugFeedbackTranslated("vanillafixes.debug.reload_resourcepacks.message");
			this.refreshResources();
			return true;
		}
		else
		{
			return false;
		}
	}

	@Shadow
	public abstract Entity getRenderViewEntity();
	@Shadow
	public abstract NetHandlerPlayClient getNetHandler();
	@Shadow
	public abstract void displayGuiScreen(GuiScreen par1);
	@Shadow
	protected abstract void clickMouse();
	@Shadow
	protected abstract void rightClickMouse();
	@Shadow
	protected abstract void middleClickMouse();
	@Shadow
	private int rightClickDelayTimer;
	@Shadow
	public boolean inGameHasFocus;
	@Shadow
	protected abstract void sendClickBlockToController(boolean par1);
	private void processKeyBinds()
	{
		if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
			++this.gameSettings.thirdPersonView;
			if (this.gameSettings.thirdPersonView > 2) {
				this.gameSettings.thirdPersonView = 0;
			}

			if (this.gameSettings.thirdPersonView == 0) {
				this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
			} else if (this.gameSettings.thirdPersonView == 1) {
				this.entityRenderer.loadEntityShader((Entity)null);
			}

			this.renderGlobal.setDisplayListEntitiesDirty();
		}

		if (this.gameSettings.keyBindSmoothCamera.isPressed()) {
			this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
		}

		for(int i = 0; i < 9; ++i) {
			if (this.gameSettings.keyBindsHotbar[i].isPressed()) {
				if (this.thePlayer.isSpectator()) {
					this.ingameGUI.getSpectatorGui().func_175260_a(i);
				} else {
					this.thePlayer.inventory.currentItem = i;
				}
			}
		}

		while(this.gameSettings.keyBindInventory.isPressed()) {
			if (this.playerController.isRidingHorse()) {
				this.thePlayer.sendHorseInventory();
			} else {
				this.getNetHandler().addToSendQueue(new C16PacketClientStatus(
					EnumState.OPEN_INVENTORY_ACHIEVEMENT));
				this.displayGuiScreen(new GuiInventory(this.thePlayer));
			}
		}

		while(this.gameSettings.keyBindDrop.isPressed()) {
			if (!this.thePlayer.isSpectator()) {
				this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
			}
		}

		boolean flag2 = this.gameSettings.chatVisibility != EnumChatVisibility.HIDDEN;

		if (flag2)
		{
			while (this.gameSettings.keyBindChat.isPressed())
			{
				this.displayGuiScreen(new GuiChat());
			}

			if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed())
			{
				this.displayGuiScreen(new GuiChat("/"));
			}
		}

		if (this.thePlayer.isUsingItem()) {
			if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
				this.playerController.onStoppedUsingItem(this.thePlayer);
			}

			while(true) {
				if (!this.gameSettings.keyBindAttack.isPressed()) {
					while(this.gameSettings.keyBindUseItem.isPressed()) {
					}

					while(this.gameSettings.keyBindPickBlock.isPressed()) {
					}
					break;
				}
			}
		} else {
			while(this.gameSettings.keyBindAttack.isPressed()) {
				this.clickMouse();
			}

			while(this.gameSettings.keyBindUseItem.isPressed()) {
				this.rightClickMouse();
			}

			while(this.gameSettings.keyBindPickBlock.isPressed()) {
				this.middleClickMouse();
			}
		}

		if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem()) {
			this.rightClickMouse();
		}

		this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
	}

	@Shadow
	public WorldClient theWorld;

	@Shadow
	public EntityPlayerSP thePlayer;

	@Shadow
	private int joinPlayerCounter;

	@Shadow
	@Final
	public Profiler mcProfiler;

	@Shadow
	public EntityRenderer entityRenderer;

	@Shadow
	public RenderGlobal renderGlobal;

	@Shadow
	private MusicTicker mcMusicTicker;

	@Shadow
	private SoundHandler mcSoundHandler;

	@Shadow
	private NetworkManager myNetworkManager;

	@Shadow
	private long systemTime;

	@Shadow
	private boolean isGamePaused;

	@Shadow
	public static long getSystemTime() {
		return 0;
	}

	@Shadow
	private long debugCrashKeyPressTime;


	@Shadow
	private RenderManager renderManager;


	public void displayChatMessage(String msg){
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
	}


	// injecting before keyboard is handled in runTick()
	@Inject(
        method = "runTick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = Shift.AFTER),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=keyboard")),
        cancellable = true
    )
	public void onF3(CallbackInfo ci){
		if(!configuration().enabled().get() || !configuration().f3CommandsSubSettings().enabled().get()) return;
		// injecting fixed F3 keyboard handling (ported from 1.12)
		if(this.thePlayer != null) this.runTickKeyboard();
		// the following code is copy-paste that goes after keyboard handling, required for cancelling
		if (this.theWorld != null) {
			if (this.thePlayer != null) {
				++this.joinPlayerCounter;
				if (this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			this.mcProfiler.endStartSection("gameRenderer");
			if (!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			this.mcProfiler.endStartSection("levelRenderer");
			if (!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			this.mcProfiler.endStartSection("level");
			if (!this.isGamePaused) {
				if (this.theWorld.getLastLightningBolt() > 0) {
					this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
				}

				this.theWorld.updateEntities();
			}
		} else if (this.entityRenderer.isShaderActive()) {
			this.entityRenderer.stopUseShader();
		}

		if (!this.isGamePaused) {
			this.mcMusicTicker.update();
			this.mcSoundHandler.update();
		}

		if (this.theWorld != null) {
			if (!this.isGamePaused) {
				this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);

				try {
					this.theWorld.tick();
				} catch (Throwable var8) {
					CrashReport var2 = CrashReport.makeCrashReport(var8, "Exception in world tick");
					if (this.theWorld == null) {
						CrashReportCategory var3 = var2.makeCategory("Affected level");
						var3.addCrashSection("Problem", "Level is null!");
					} else {
						this.theWorld.addWorldInfoToCrashReport(var2);
					}

					throw new ReportedException(var2);
				}
			}

			this.mcProfiler.endStartSection("animateTick");
			if (!this.isGamePaused && this.theWorld != null) {
				this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			this.mcProfiler.endStartSection("particles");
			if (!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		} else if (this.myNetworkManager != null) {
			this.mcProfiler.endStartSection("pendingConnection");
			this.myNetworkManager.processReceivedPackets();
		}

		this.mcProfiler.endSection();
		this.systemTime = getSystemTime();

		// returning
		ci.cancel();
	}

}
