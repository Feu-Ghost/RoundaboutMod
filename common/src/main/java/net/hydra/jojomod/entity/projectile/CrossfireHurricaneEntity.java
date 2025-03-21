package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PowersMagiciansRed;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class CrossfireHurricaneEntity extends AbstractHurtingProjectile {
    public CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(CrossfireHurricaneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CROSS_NUMBER = SynchedEntityData.defineId(CrossfireHurricaneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(CrossfireHurricaneEntity.class, EntityDataSerializers.INT);

    public LivingEntity standUser;
    public UUID standUserUUID;
    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }
    public void setUserID(int idd) {
        this.getEntityData().set(USER_ID, idd);
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            this.standUser = LE;
            if (!this.level().isClientSide()){
                standUserUUID = LE.getUUID();
            }
        }
    }
    @Override
    public boolean isControlledByLocalInstance() {

        if (this.getStandUser() != null && ((StandUser)this.getStandUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            if (this.getCrossNumber() > 0) {
                return true;
            }
        }
        return super.isControlledByLocalInstance();
    }


    public void setOldPosAndRot2() {
        if (storeVec != null) {
            double $$0 = storeVec.x();
            double $$1 = storeVec.y();
            double $$2 = storeVec.z();
            this.xo = $$0;
            this.yo = $$1;
            this.zo = $$2;
            this.xOld = $$0;
            this.yOld = $$1;
            this.zOld = $$2;
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }
    public Vec3 storeVec;
    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    public boolean initialized = false;
    public int getSize() {
        return this.getEntityData().get(SIZE);
    }
    public void setSize(int idd) {
        this.getEntityData().set(SIZE, idd);
    }
    public int getCrossNumber() {
        return this.getEntityData().get(CROSS_NUMBER);
    }
    public void setCrossNumber(int idd) {
        this.getEntityData().set(CROSS_NUMBER, idd);
    }
    protected CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public boolean isBundle = false;
    public boolean isTickable = false;
    public void actuallyTick(){
        isTickable = true;
        tick();
        isTickable = false;
    }
    public void tick() {
        if (this.getStandUser() != null && ((StandUser)this.getStandUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){

            if (this.getCrossNumber() > 0){
                this.setDeltaMovement(Vec3.ZERO);
                if (this.level().isClientSide()){

                    if (!initialized){
                        initialized = true;
                        PMR.addHurricaneSpecial(this);
                    }
                }
                if (!isTickable) {
                return;
                }
            }
        }
        super.tick();
    }
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {

    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(USER_ID, -1);
        this.entityData.define(SIZE, 0);
        this.entityData.define(CROSS_NUMBER, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        super.addAdditionalSaveData($$0);
        if (standUser != null) {
            $$0.putUUID("standUser", standUser.getUUID());
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        super.readAdditionalSaveData($$0);
        if ($$0.hasUUID("standUser")) {
            standUserUUID = $$0.getUUID("standUser");
            if (!this.level().isClientSide()) {
                Entity ett = ((ServerLevel) this.level()).getEntity(standUserUUID);
                if (ett instanceof LivingEntity lett) {
                    standUser = lett;
                    this.setUserID(lett.getId());
                }
            }
        }
    }
    public LivingEntity getStandUser(){
        if (standUser != null){
            return standUser;
        } else if (standUserUUID != null && !this.level().isClientSide()){
            Entity ett = ((ServerLevel)this.level()).getEntity(standUserUUID);
            if (ett instanceof LivingEntity lett){
                standUser = lett;
                this.setUserID(lett.getId());
            }
        } else if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            standUser = LE;
        }
        return standUser;
    }

    @Override

    protected boolean shouldBurn() {
        return false;
    }


}
