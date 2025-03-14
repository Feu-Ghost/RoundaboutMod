package net.hydra.jojomod.stand.powers.api;

import net.hydra.jojomod.stand.powers.impl.AbstractStandPower;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public interface StandPower {
    int SQUARE_SIZE = 24;

    /** Ticks the specific power */
    void roundabout$tick();

    boolean isAttackInept();
    boolean isAttackIneptVisually();

    /** Draws the stand power with its icon */
    void roundabout$draw(GuiGraphics context, int y, ResourceLocation rl, boolean locked);
}
