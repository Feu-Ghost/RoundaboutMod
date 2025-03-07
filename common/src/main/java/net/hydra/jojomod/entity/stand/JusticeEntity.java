package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class JusticeEntity extends StandEntity {
    public JusticeEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }


    protected static final EntityDataAccessor<Byte> JUSTICE_TEAM = SynchedEntityData.defineId(JusticeEntity.class,
            EntityDataSerializers.BYTE);
    public final void setJusticeTeam(Byte team) {
        this.entityData.set(JUSTICE_TEAM, team);
    } //sets leaning direction
    public byte getJusticeTeam() {
        return this.entityData.get(JUSTICE_TEAM);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(JUSTICE_TEAM, (byte) 0);
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }
    @Override
    public boolean lockPos(){
        return false;
    }
    public static final byte
            PART_3_SKIN = 1,
            MANGA_SKIN = 2,
            SKELETON_SKIN = 3,
            OVA_SKIN = 4,
            BOGGED = 5,
            STRAY_SKIN = 6,
            TAROT = 8,
            FLAMED = 7,
            WITHER = 9,
            TWILIGHT = 10,
            PIRATE = 11,
            BLUE_FLAMED = 12,
            DARK_MIRAGE = 13;

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.justice.base");
        } else if (skinId == MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.justice.manga");
        } else if (skinId == SKELETON_SKIN){
            return Component.translatable(  "skins.roundabout.justice.skeleton");
        } else if (skinId == OVA_SKIN){
            return Component.translatable(  "skins.roundabout.justice.ova");
        } else if (skinId == STRAY_SKIN){
            return Component.translatable(  "skins.roundabout.justice.stray");
        } else if (skinId == BOGGED){
            return Component.translatable(  "skins.roundabout.justice.bogged");
        } else if (skinId == TAROT){
            return Component.translatable(  "skins.roundabout.justice.tarot");
        } else if (skinId == WITHER){
            return Component.translatable(  "skins.roundabout.justice.wither");
        } else if (skinId == FLAMED){
            return Component.translatable(  "skins.roundabout.justice.flamed");
        } else if (skinId == BLUE_FLAMED){
            return Component.translatable(  "skins.roundabout.justice.flamed_blue");
        } else if (skinId == TWILIGHT){
            return Component.translatable(  "skins.roundabout.justice.twilight");
        } else if (skinId == PIRATE){
            return Component.translatable(  "skins.roundabout.justice.pirate");
        } else if (skinId == DARK_MIRAGE){
            return Component.translatable(  "skins.roundabout.justice.dark_mirage");
        }
        return Component.translatable(  "skins.roundabout.the_world.base");
    }
    public final AnimationState idleAnimation = new AnimationState();
    public final AnimationState idleAnimation2 = new AnimationState();
    @Override
    public void setupAnimationStates() {
        if (this.getUser() != null) {
            if (this.getAnimation() == 0 && this.getIdleAnimation() == 1) {
                this.idleAnimation2.startIfStopped(this.tickCount);
            } else {
                this.idleAnimation2.stop();
            }
            if (this.getAnimation() == 0 && this.getIdleAnimation() == 0) {
                this.idleAnimation.startIfStopped(this.tickCount);
            } else {
                this.idleAnimation.stop();
            }
        }
    }

    @Override
    public Component getPosName(byte posID){
        if (posID == 1){
            return Component.translatable(  "idle.roundabout.battle_justice");
        } else {
            return Component.translatable(  "idle.roundabout.passive_justice");
        }
    }

    @Override
    public void playerSetProperties(Player PE) {
        this.setJusticeTeam(((IPlayerEntity)PE).roundabout$getTeamColor());
        super.playerSetProperties(PE);
    }
    public int tsReleaseTime = 0;
    @Override
    public void tick(){

        if (!this.level().isClientSide){
            if (this.getAnimation() == 31) {
                tsReleaseTime++;
                if (tsReleaseTime > 24){
                    this.setAnimation((byte) 0);
                    tsReleaseTime = 0;
                }
            }
        } else {


            if (!(!ConfigManager.getClientConfig().particleSettings.renderJusticeParticlesInFirstPerson &&
                    ClientUtil.checkIfStandIsYoursAndFirstPerson(this)) &&
                    !(!ConfigManager.getClientConfig().particleSettings.renderJusticeParticlesWhilePilotingInFirstPerson &&
                    ClientUtil.checkIfStandIsYoursAndFirstPersonandPiloting(this))) {
                if (this.getSkin() != DARK_MIRAGE) {
                    if (this.getSkin() == FLAMED) {
                        for (int i = 0; i < ConfigManager.getClientConfig().particleSettings.justiceSkinFlameParticlesPerTick; i++) {
                            this.level()
                                    .addParticle(
                                            ParticleTypes.FLAME,
                                            this.getRandomX(1.1),
                                            this.getRandomY(),
                                            this.getRandomZ(1.1),
                                            0,
                                            0.1,
                                            0
                                    );
                        }
                    }

                    if (this.getSkin() == BLUE_FLAMED) {
                        for (int i = 0; i < ConfigManager.getClientConfig().particleSettings.justiceSkinFlameParticlesPerTick; i++) {
                            this.level()
                                    .addParticle(
                                            ParticleTypes.SOUL_FIRE_FLAME,
                                            this.getRandomX(1.1),
                                            this.getRandomY(),
                                            this.getRandomZ(1.1),
                                            0,
                                            0.1,
                                            0
                                    );
                        }
                    }

                    for (int i = 0; i < ConfigManager.getClientConfig().particleSettings.justiceFogParticlesPerTick; i++) {
                        this.level()
                                .addParticle(
                                        ModParticles.FOG_CHAIN,
                                        this.getRandomX(1.1),
                                        this.getRandomY(),
                                        this.getRandomZ(1.1),
                                        0,
                                        0.1,
                                        0
                                );
                    }
                }
            }
        }
        super.tick();
    }
    @Override
    public boolean isControlledByLocalInstance() {
        LivingEntity user =  this.getUser();
        if (user != null){
            Entity ent =  this.getUserData(user).roundabout$getStandPowers().getPilotingStand();
            if (ent != null && ent.is(this)){
                return (user instanceof Player $$0 ? $$0.isLocalPlayer() : this.isEffectiveAi());
            }
        }
        return super.isControlledByLocalInstance();
    }


    @Override
    protected float getFlyingSpeed() {
        return 0.10F;
    }

    public boolean stuck = false;
    @Override
    public void move(MoverType $$0, Vec3 $$1) {
        if (this.noPhysics) {
            Entity ent = this.getUser();
            if (ent != null){
                if (ent instanceof Player PE){
                    StandUser user = ((StandUser) PE);
                    StandPowers powers = user.roundabout$getStandPowers();
                    if (powers.isPiloting()){
                        Entity entX = powers.getPilotingStand();
                        if (entX != null && entX.is(this)){
                            BlockPos veci = BlockPos.containing(new Vec3(this.getX() + $$1.x, this.getY() + this.getEyeHeight() + $$1.y,this.getZ() + $$1.z));
                            BlockPos veci2 = BlockPos.containing(new Vec3(this.getX() + $$1.x*2, this.getY() + this.getEyeHeight() + $$1.y*2,this.getZ() + $$1.z*2));
                            BlockPos veci3 = BlockPos.containing(new Vec3(this.getX() + $$1.x*3, this.getY() + this.getEyeHeight() + $$1.y*2,this.getZ() + $$1.z*3));
                            BlockState bl = this.level().getBlockState(veci);
                            BlockState bl2 = this.level().getBlockState(veci2);
                            BlockState bl3 = this.level().getBlockState(veci3);
                            if (getFullBlock(bl,veci) ||
                                    getFullBlock(bl2,veci2) ||
                                    getFullBlock(bl3,veci3)){
                                this.setDeltaMovement(Vec3.ZERO);
                                if (!stuck) {
                                    stuck = true;
                                    this.setPos(this.getX() - $$1.x, this.getY(), this.getZ() - $$1.z);
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
        super.move($$0,$$1);
    }


    @SuppressWarnings("deprecation")
    public boolean getFullBlock(BlockState bs, BlockPos bp){
        Block blk = bs.getBlock();
        return (bs.isSolid() && (blk.isCollisionShapeFullBlock(bs,this.level(),bp) ||
                (blk instanceof SlabBlock ||
                        blk instanceof StairBlock)));

    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isControlledByLocalInstance()) {
            boolean bl;
            double d = 0.08;
            boolean bl2 = bl = this.getDeltaMovement().y <= 0.0;
            if (bl && this.hasEffect(MobEffects.SLOW_FALLING)) {
                d = 0.01;
            }
            FluidState fluidState = this.level().getFluidState(this.blockPosition());
                BlockPos blockPos = this.getBlockPosBelowThatAffectsMyMovement();
                float p = this.level().getBlockState(blockPos).getBlock().getFriction();
                float f = this.onGround() ? p * 0.91f : 0.91f;
                Vec3 vec37 = this.handleRelativeFrictionAndCalculateMovement(vec3, p);
                double q = vec37.y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    q += (0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec37.y) * 0.2;
                } else if (!this.level().isClientSide || this.level().hasChunkAt(blockPos)) {
                    if (!this.isNoGravity()) {
                        q -= d;
                    }
                } else {
                    q = this.getY() > (double)this.level().getMinBuildHeight() ? -0.1 : 0.0;
                }
                if (this.shouldDiscardFriction()) {
                    this.setDeltaMovement(vec37.x, q, vec37.z);
                } else {
                    this.setDeltaMovement(vec37.x * (double)f, q * (double)0.98f, vec37.z * (double)f);
                }

            if (this.getUser() instanceof Player PE && this.level().isClientSide()) {
                ModPacketHandler.PACKET_ACCESS.updatePilot(this);
            }
        }
        this.calculateEntityAnimation(this instanceof FlyingAnimal);
    }
    @Override
    public Vec3 getIdleOffset(LivingEntity standUser) {
        if (this.getSkin() != DARK_MIRAGE) {
            int vis = this.getFadeOut();
            double r = (((double) vis / MaxFade) * ((standUser.getBbWidth() / 2) + 2*(this.getDistanceOut())));
            if (r < 0.5) {
                r = 0.5;
            }
            double yawfix = standUser.getYRot();
            yawfix += this.getAnchorPlace() + 125;
            if (yawfix > 360) {
                yawfix -= 360;
            } else if (yawfix < 0) {
                yawfix += 360;
            }
            double ang = (yawfix - 180) * Math.PI;

            double mcap = 0.3;
            Vec3 xyz = standUser.getDeltaMovement();
            double yy = xyz.y() * 0.3;
            if (yy > mcap) {
                yy = mcap;
            } else if (yy < -mcap) {
                yy = -mcap;
            }
            if (isSwimming() || isVisuallyCrawling() || isFallFlying()) {
                yy += 1;
            }

            double x1 = standUser.getX() - -1 * (r * (Math.sin(ang / 180)));
            double y1 = standUser.getY() + getIdleYOffset() - yy+0.6;
            double z1 = standUser.getZ() - (r * (Math.cos(ang / 180)));

            return new Vec3(x1, y1, z1);
        } else {
            return super.getIdleOffset(standUser);
        }
    }

}

