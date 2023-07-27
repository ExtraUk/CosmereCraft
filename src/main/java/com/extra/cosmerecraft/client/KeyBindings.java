package com.extra.cosmerecraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORIES_COSMERECRAFT = "key.categories.cosmerecraft";
    public static final String KEY_FERUCHEMY_MENU = "key.feruchemy_menu";
    //public static final String KEY_ALLOMANCY_MENU = "key.allomancy_menu";

    public static final KeyMapping FERUCHEMY_MENU_KEY = new KeyMapping(KEY_FERUCHEMY_MENU, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORIES_COSMERECRAFT);
    //public static final KeyMapping ALLOMANCY_MENU_KEY = new KeyMapping(KEY_ALLOMANCY_MENU, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORIES_COSMERECRAFT);
}
