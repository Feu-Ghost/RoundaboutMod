package net.hydra.jojomod.stand.powers.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;

/** Blueprint for all StandPowers (besides AbstractStandPower) */
public interface StandPower {
    int SQUARE_SIZE = 24;

    /** Ticks the specific power. */
    void roundabout$tick();

    /** Called whenever the user activates the power.
     * I.e. if this is slot 4, and I press V, this function is called immediately. */
    void roundabout$onInputBegin();
    /** Called whenever the user activates the power.
     * I.e. if this is slot 4, and I press V, this function is called when I stop pressing V. */
    void roundabout$onInputEnd();

    boolean roundabout$isAttackInept();
    boolean roundabout$isAttackIneptVisually();

    /** Used primarily for rendering. Designates if the power is unlocked or not. */
    void roundabout$setLocked(boolean value);
    boolean roundabout$getLocked();

    /** Get the StandUser that has this power. */
    StandUser roundabout$getUser();
    /** Get the owner of the stand that has this power */
    LivingEntity roundabout$getUserEntity();

    /** Draws the stand power with its icon */
    void roundabout$draw(GuiGraphics context);
}
