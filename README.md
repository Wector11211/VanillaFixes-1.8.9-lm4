# VanillaFixes
This Labymod 4 addon provides you with fixes to some of annoying 1.8 bugs:
- **Translucent skin fix**
  - Makes skin's second layer render semi-transparent pixels correctly
  - <img src="https://user-images.githubusercontent.com/19906474/211441396-77eeea4f-42b4-4773-9d27-ba3c64bb33c4.png" width="600">
- **Focus movement fix**
  - In 1.8 there is a bug which chokes keyboard input, say if you are in inventory and hold movement keys (WASD) after closing it you have to press those keys again to make them register. Same happens if you're holding keys and you get teleported (switched worlds) on server. This module bings the fix from newer versions so that no longer happens.
- **Better F3 commands**
  - Registers F3 combinations (ex. `F3+A`) before handling `F3` and keys separately. As the result, you no longer end up with dubug screen opened as well as chat (in case of `F3+T`) or moving (`F3+A`, `F3+D`)
- **Sound duplication fix**
  - If a sound plays while you are opening a GUI (inventory), it gets played once again after closing the GUI. This module fixs the issue.
- **Infinite title fix**
  - Minecraft never checks if a title is present on a screen while disconnecting from a server and you might end up with a left over titles (sometimes infinite) even when joining another server or a single player world.
  - <img src="https://user-images.githubusercontent.com/19906474/211459560-7ac30f24-841f-431d-8d18-5fe16a37eb1b.png" width="600">
- **Hide effects from inventory**
  - As Labymod provides you with effects HUD there might be no reason to have them displyed in inventory as well. Since effecs do shift your inventory, you might want to disable them.
