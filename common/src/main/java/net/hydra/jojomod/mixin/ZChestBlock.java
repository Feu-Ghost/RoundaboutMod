package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBlockEntityAccess;
import net.hydra.jojomod.stand.powers.api.TimeStop;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public class ZChestBlock  {
    /**This mixin exists to make chest blocks open during TS. Unfortunately, requires packets to be sent.**/
    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$OpenChest(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5, CallbackInfoReturnable<InteractionResult> ci) {
        if (!$$1.isClientSide) {
            if (((TimeStop)$$1).inTimeStopRange($$2)) {
                ((TimeStop) $$1).streamTileEntityTSToCLient($$2);
                BlockEntity blk = $$1.getBlockEntity($$2);
                if (blk != null) {
                    ((IBlockEntityAccess) blk).roundabout$setRoundaboutTimeInteracted(true);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$TickChest(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3, CallbackInfo ci) {

            if (!((TimeStop)$$1).inTimeStopRange($$2)){
                BlockEntity blk = $$1.getBlockEntity($$2);
                if (blk != null) {
                    ((IBlockEntityAccess) blk).roundabout$setRoundaboutTimeInteracted(false);
                    ((TimeStop) $$1).streamTileEntityTSToCLient($$2);
                }
            }
    }


}
