package net.hydra.jojomod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class KeyInputRegistry {
    /**This is where all keybinds in the mod are registered.
     * Note that forge and fabric also register the keys so they can be
     * remapped ingame.*/
    public static final String KEY_CATEGORY_JOJO = "key.category.roundabout.jojo";
    public static final String KEY_ABILITY_1 = "key.roundabout.ability.one";
    public static final String KEY_ABILITY_2 = "key.roundabout.ability.two";
    public static final String KEY_ABILITY_3 = "key.roundabout.ability.three";
    public static final String KEY_ABILITY_4 = "key.roundabout.ability.four";
    public static final String KEY_SUMMON_STAND = "key.roundabout.summon.stand";
    public static final String KEY_JOJO_MENU = "key.roundabout.menu";
    public static final String KEY_GUARD = "key.roundabout.bonus_guard";
    public static final String KEY_SWITCH_ROWS = "key.roundabout.switch_rows";
    public static final String KEY_SHOW_EXP = "key.roundabout.show_exp";
    public static final String KEY_POSE = "key.roundabout.pose";

    public static final KeyMapping summonKey = new KeyMapping(
            KeyInputRegistry.KEY_SUMMON_STAND,
            InputConstants.KEY_R,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );

    /** Sequential order of slot keys 1-4 (inclusive).
     * Remember: these are zero sequenced. The first key is index 0 and the last is index 3. */
    public static final List<KeyMapping> SLOT_KEYS = Arrays.asList(
            new KeyMapping(KEY_ABILITY_1, InputConstants.KEY_Z, KEY_CATEGORY_JOJO),
            new KeyMapping(KEY_ABILITY_2, InputConstants.KEY_X, KEY_CATEGORY_JOJO),
            new KeyMapping(KEY_ABILITY_3, InputConstants.KEY_C, KEY_CATEGORY_JOJO),
            new KeyMapping(KEY_ABILITY_4, InputConstants.KEY_V, KEY_CATEGORY_JOJO)
    );

    public static KeyMapping menuKey = new KeyMapping(
            KeyInputRegistry.KEY_JOJO_MENU,
            InputConstants.KEY_Y,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    public static KeyMapping guardKey = new KeyMapping(
            KeyInputRegistry.KEY_GUARD,
            -1,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    public static KeyMapping switchRow = new KeyMapping(
            KeyInputRegistry.KEY_SWITCH_ROWS,
            -1,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    public static KeyMapping showExp = new KeyMapping(
            KeyInputRegistry.KEY_SHOW_EXP,
            -1,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    public static KeyMapping pose = new KeyMapping(
            KeyInputRegistry.KEY_POSE,
            -1,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    // used to use GLFW.GLFW_KEY_X instead of InputConstants.KEY_X
    // also had InputConstants.Type.KEYSYM,
}
