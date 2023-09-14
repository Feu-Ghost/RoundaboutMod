package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(Entity.class)
public class EntityStandMixin implements IStandUser {

    //This Code gives every entity the potential to be a stand user.
    //It provides movement sync code for stands as well, by offering a modified copy of mount code
    private Entity standOut;
    private Entity master;

    @Override
    public Entity getStandOut() {
        return this.standOut;
    }

    @Override
    public boolean hasMaster() {
        return this.getMaster() != null;
    }


    @Override
    public void setMaster(Entity Master) {
        this.master = Master;
    }

    @Override
    public Entity getMaster() {
        return this.master;
    }

    @Override
    public boolean hasStandOut() {
        return (standOut != null && standOut.isAlive() && !standOut.isRemoved());
    }

    @Override
    public void updateStandOutPosition(Entity passenger) {
        this.updateStandOutPosition(passenger, Entity::setPosition);
        RoundaboutMod.LOGGER.info("updateStandOutPosition");
    }

    @Override
    public void updateStandOutPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        RoundaboutMod.LOGGER.info("updateStandOutPosition");
        Vec3d grabPos = ((StandEntity)passenger).getStandOffsetVector(((Entity) (Object) this));
        positionUpdater.accept(passenger, grabPos.x, grabPos.y, grabPos.z);
        passenger.setYaw(((Entity) (Object) this).getHeadYaw());
        passenger.setPitch(((Entity) (Object) this).getPitch());
        passenger.setBodyYaw(((Entity) (Object) this).getHeadYaw());
        passenger.setHeadYaw(((Entity) (Object) this).getHeadYaw());
    }

    @Inject(method = "teleportPassengers", at = @At(value = "TAIL"))
    private void teleportStandOut(CallbackInfo ci) {
        if (hasStandOut()){
            RoundaboutMod.LOGGER.info("teleportPassengers");
            this.updateStandOutPosition(standOut, Entity::refreshPositionAfterTeleport);
        }
    }

    @Override
    public void tickStandOut() {
        ((Entity) (Object) this).setVelocity(Vec3d.ZERO);
        ((Entity) (Object) this).tick();
        if (!(this.hasMaster())) {
            return;
        }
        RoundaboutMod.LOGGER.info("tickStandOut");
        ((IStandUser) this.getMaster()).updateStandOutPosition( ((Entity) (Object) this));
    }

    @Override
    public void onStandOutLookAround(Entity passenger) {
    }

    @Override
    public boolean startStandRiding(Entity entity) {
        RoundaboutMod.LOGGER.info("startStandRiding");

        ((Entity) (Object) entity).setPose(EntityPose.STANDING);
        //entity.streamIntoPassengers().filter(passenger -> passenger instanceof ServerPlayerEntity).forEach(player -> Criteria.STARTED_RIDING.trigger((ServerPlayerEntity)player));
        return this.startStandRiding(entity, false);
    }

    @Override
    public boolean startStandRiding(Entity entity, boolean force) {
        if (entity == this.getMaster()) {
            RoundaboutMod.LOGGER.info("startStandRiding Fail");
            return false;
        }
        ((IStandUser) entity).setMaster(((Entity) (Object) this));
        this.addStandOut(entity);
        RoundaboutMod.LOGGER.info("startStandRiding 2");
        return true;
    }

    @Override
    public void removeAllStandOuts() {
        RoundaboutMod.LOGGER.info("removeAllStandOuts");
        ((IStandUser) standOut).stopStandOut();
    }

    @Override
    public void dismountMaster() {
        if (this.master != null) {
            RoundaboutMod.LOGGER.info("dismountMaster");
            Entity entity = this.master;
            this.master = null;
            ((IStandUser) entity).removeStandOut(((Entity) (Object) this));
        }
    }

    @Override
    public void stopStandOut() {
        this.dismountMaster();
        RoundaboutMod.LOGGER.info("stopStandOut");
    }

    @Override
    public void addStandOut(Entity passenger) {
            this.standOut = passenger;
        RoundaboutMod.LOGGER.info("addStandOut");
        //this.emitGameEvent(GameEvent.ENTITY_MOUNT, passenger);
    }

    @Override
    public void removeStandOut(Entity passenger) {
        this.standOut = null;
        RoundaboutMod.LOGGER.info("removeStandOut");
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }


}
