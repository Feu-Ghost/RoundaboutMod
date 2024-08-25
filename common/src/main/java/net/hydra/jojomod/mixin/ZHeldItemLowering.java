package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.GlaiveItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ZHeldItemLowering {
    /** Minor code for vanilla item rendering in first person while stand is active.*/

    @Shadow
    private float mainHandHeight;
    @Shadow
    private float offHandHeight;

    @Shadow
    private float oMainHandHeight;
    @Shadow
    private float oOffHandHeight;

    @Shadow
    private final Minecraft minecraft;

    public ZHeldItemLowering(Minecraft client) {
        this.minecraft = client;
    }

    /** This makes certain items lower when your stand is out. Indicates that you can't really use tools
     * like swords or pickaxes while a stand is out.
     * The reason for that design decision is mostly to prevent sword swings overriding stand attacks
     * to mobs in a close range, and accidentally breaking blocks when attacking.*/
    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void roundaboutHeldItems(CallbackInfo ci) {
        LocalPlayer clientPlayerEntity2 = this.minecraft.player;
        if (!this.minecraft.player.isHandsBusy()) {
        if (((StandUser) this.minecraft.player).getActive()) {
            ItemStack itemStack3 = clientPlayerEntity2.getMainHandItem();
            ItemStack itemStack4 = clientPlayerEntity2.getOffhandItem();
            if (itemStack3.getItem() instanceof TieredItem && !clientPlayerEntity2.getUseItem().equals(itemStack3)){
            if (this.mainHandHeight > 0.6) {
                this.mainHandHeight = Mth.clamp(this.mainHandHeight - 0.4f, 0.6f, 1.0f);
            }}

            if (this.offHandHeight > 0.6) {
                this.offHandHeight = Mth.clamp(this.offHandHeight - 0.4f, 0.6f, 1.0f);
            }

        }}
    }

    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    public void roundaboutHeldItems2(CallbackInfo ci) {
        LocalPlayer clientPlayerEntity2 = this.minecraft.player;
        if (clientPlayerEntity2 != null && ((TimeStop)clientPlayerEntity2.level()).CanTimeStopEntity(clientPlayerEntity2)){
            mainHandHeight = oMainHandHeight;
            offHandHeight = oOffHandHeight;
            ci.cancel();
        }
    }
}
