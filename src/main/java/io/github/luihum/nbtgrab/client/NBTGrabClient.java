package io.github.luihum.nbtgrab.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class NBTGrabClient implements ClientModInitializer {

    private static KeyBinding copyNBTBinding;

    @Override
    public void onInitializeClient() {
        copyNBTBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nbtgrab.grab",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.nbtgrab"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (copyNBTBinding.wasPressed()) {
                if (client.player != null) {
                    ItemStack curItem = client.player.getMainHandStack();
                    if (curItem.isEmpty()) {
                        curItem = client.player.getOffHandStack();
                    }
                    if (!curItem.isEmpty()) {
                        NbtCompound curItemNBT = curItem.getNbt();
                        String curItemID = curItem.toString().split("\\s+")[1];
                        client.keyboard.setClipboard(curItemID + (curItemNBT != null ? curItemNBT : ""));
                        client.player.sendMessage(Text.translatable("nbtgrab.message", curItemID + (curItemNBT != null ? curItemNBT : "")), false);
                    } else {
                        client.player.sendMessage(Text.translatable("nbtgrab.no_item"), false);
                    }
                }
            }
        });
    }
}
