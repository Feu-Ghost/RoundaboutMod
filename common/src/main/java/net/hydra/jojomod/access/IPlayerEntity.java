package net.hydra.jojomod.access;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Shadow;

public interface IPlayerEntity {
    Inventory roundaboutGetInventory();

    void roundaboutSetPos(byte Pos);

    byte roundaboutGetPos();
}
