package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ICreeper;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IRaider;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenCreeper;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.corpses.FallenVillager;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.event.powers.stand.presets.DashPreset;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowersJustice extends DashPreset {
    public PowersJustice(LivingEntity self) {
        super(self);
    }
    private List<LivingEntity> fogControlledEntities = new ArrayList<>();
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersJustice(entity);
    }
    @Override
    public boolean canSummonStand(){
        if (this.getSelf() instanceof Creeper || this.getSelf() instanceof Raider){
            return false;
        }
        return true;
    }
    @Override
    public boolean interceptAttack(){
        return false;
    }
    @Override
    public boolean interceptGuard(){
        return false;
    }

    @Override
    public boolean isMiningStand() {
        return false;
    }
    public void cycleThroughJusticeEntities(){
        if (fogControlledEntities == null){
            fogControlledEntities = new ArrayList<>();
        }
        List<LivingEntity> fogControlledEntities2 = new ArrayList<>(fogControlledEntities) {};
        if (!fogControlledEntities2.isEmpty()){
            for (LivingEntity value : fogControlledEntities2) {
                if (value.isRemoved() || !value.isAlive()) {
                    removeJusticeEntities(value);
                } else {
                    if (value instanceof FallenMob fm){
                        if (fm.controller != null && fm.controller.is(this.getSelf())){

                        } else {
                            removeJusticeEntities(value);
                        }
                    } else {
                        removeJusticeEntities(value);
                    }
                }
            }
        }
    }

    public void cycleThroughJusticeEntities2(){
        if (fogControlledEntities == null){
            fogControlledEntities = new ArrayList<>();
        }
        List<LivingEntity> fogControlledEntities2 = new ArrayList<>(fogControlledEntities) {};
        if (!fogControlledEntities2.isEmpty()){
            for (LivingEntity value : fogControlledEntities2) {
                if (value.isRemoved() || !value.isAlive()) {
                } else {
                    if (value instanceof FallenMob fm){
                        if (fm.controller != null && fm.controller.is(this.getSelf())){
                            if (this.getSelf() instanceof Player PE){
                                byte bt = ((IPlayerEntity)PE).roundabout$getTeamColor();
                                if (fm.getJusticeTeamColor() != bt){
                                    fm.setJusticeTeamColor(bt);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public List<LivingEntity> queryJusticeEntities(){
        if (fogControlledEntities == null){
            fogControlledEntities = new ArrayList<>();
        }
        return fogControlledEntities;
    }
    public List<LivingEntity> addJusticeEntities(LivingEntity LE){
        if (fogControlledEntities == null){
            fogControlledEntities = new ArrayList<>();
        }
        fogControlledEntities.add(LE);
        return fogControlledEntities;
    }
    public List<LivingEntity> removeJusticeEntities(LivingEntity LE){
        if (fogControlledEntities == null){
            fogControlledEntities = new ArrayList<>();
        }
        fogControlledEntities.remove(LE);
        return fogControlledEntities;
    }
    public List<LivingEntity> clearJusticeEntities(){
        fogControlledEntities = new ArrayList<>();
        return fogControlledEntities;
    }

    @Override
    public void onStandSwitch(){
        if (this.getSelf() instanceof  Player PE){
            IPlayerEntity ipe = ((IPlayerEntity)PE);
            byte morph = ipe.roundabout$getShapeShift();
            if (!ShapeShifts.getShiftFromByte(morph).equals(ShapeShifts.PLAYER)){
                ipe.roundabout$shapeShift();
                ipe.roundabout$setShapeShift(ShapeShifts.PLAYER.id);
            }
        }
    }

    @Override
    public void pilotInputAttack(){
        LivingEntity ent = getPilotingStand();
        if (ent != null) {
            Entity TE = MainUtil.getTargetEntity(ent, 100, 10);
            if (TE != null && !(TE instanceof StandEntity && !TE.isAttackable())) {
                Vec3 vec3d = ent.getEyePosition(0);
                Vec3 vec3d2 = ent.getViewVector(0);
                Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                BlockHitResult blockHit = ent.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, ent));
                if ((blockHit.distanceTo(ent) - 1) < ent.distanceToSqr(TE)) {
                } else {
                    if (TE instanceof FallenMob fm && fm.getController() == this.self.getId()) {
                        this.self.playSound(ModSounds.JUSTICE_SELECT_EVENT, 200F, 1.0F);
                       ModPacketHandler.PACKET_ACCESS.intToServerPacket(fm.getId(),
                                    PacketDataIndex.INT_STAND_ATTACK);
                    } else if (!ClientUtil.isPlayer(TE)){
                        this.self.playSound(ModSounds.JUSTICE_SELECT_ATTACK_EVENT, 200F, 1.0F);
                        ModPacketHandler.PACKET_ACCESS.intToServerPacket(TE.getId(),
                                PacketDataIndex.INT_STAND_ATTACK);
                    }
                }
            }
        }
    }

    @Override
    public void handleStandAttack(Player player, Entity target){
        if (target instanceof FallenMob fm && fm.getController() == this.self.getId()) {
            if (fm.getSelected()){
                fm.setSelected(false);
            } else {
                fm.setSelected(true);
            }
        } else {
            if (fogControlledEntities == null){
                fogControlledEntities = new ArrayList<>();
            }
            List<LivingEntity> fogControlledEntities2 = new ArrayList<>(fogControlledEntities) {};
            if (!fogControlledEntities2.isEmpty()) {
                for (LivingEntity value : fogControlledEntities2) {
                    if (value.isRemoved() || !value.isAlive()) {
                    } else {
                        if (value instanceof FallenMob fm && target instanceof LivingEntity LE) {
                            if (fm.controller != null && fm.controller.is(this.getSelf()) && fm.getSelected()) {

                                fm.corpseTarget = LE;
                                if (LE instanceof Player pl){
                                    fm.setLastHurtByPlayer(pl);
                                }
                                fm.manualTarget = LE;
                                fm.setLastHurtByMob(LE);
                                fm.setTarget(LE);
                                fm.setSelected(false);
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void handleStandAttack2(Player player, Entity target){
        if (target instanceof FallenCreeper fm && fm.getController() == this.self.getId()) {
            fm.ignite();
        } else  {

        }
    }

    public LivingEntity rollCorpse(){
        LivingEntity corpse = null;
        if (this.getSelf() instanceof Skeleton){
            corpse = ModEntities.FALLEN_SKELETON.create(this.getSelf().level());
        } else if (this.getSelf() instanceof Creeper){
            corpse = ModEntities.FALLEN_CREEPER.create(this.getSelf().level());
        } else if (this.getSelf() instanceof Zombie){
            corpse = ModEntities.FALLEN_ZOMBIE.create(this.getSelf().level());
        } else if (this.getSelf() instanceof Spider){
            corpse = ModEntities.FALLEN_SPIDER.create(this.getSelf().level());
        } else if (this.getSelf() instanceof Vindicator){
            corpse = ModEntities.FALLEN_VILLAGER.create(this.getSelf().level());
        } else {
            double rand = Math.random();
            if (rand < 0.2){
                corpse = ModEntities.FALLEN_VILLAGER.create(this.getSelf().level());
            } else if (rand < 0.4){
                corpse = ModEntities.FALLEN_SPIDER.create(this.getSelf().level());
            } else if (rand < 0.6){
                corpse = ModEntities.FALLEN_ZOMBIE.create(this.getSelf().level());
            } else if (rand < 0.8){
                if (this.self instanceof Villager || this.self instanceof IronGolem){
                    corpse = ModEntities.FALLEN_VILLAGER.create(this.getSelf().level());
                } else {
                    corpse = ModEntities.FALLEN_SKELETON.create(this.getSelf().level());
                }
            } else {
                if (this.self instanceof Villager || this.self instanceof IronGolem || this.self instanceof SnowGolem
                        || this.self instanceof TamableAnimal){
                    corpse = ModEntities.FALLEN_SPIDER.create(this.getSelf().level());
                } else {
                    corpse = ModEntities.FALLEN_CREEPER.create(this.getSelf().level());
                }
            }
        }
        return corpse;
    }
    public void initializeCorpse(LivingEntity corpse, LivingEntity attackTarget){
        if (corpse instanceof FallenMob fm){
            fm.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            fm.diesWhenUncontrolled = true;
            this.getSelf().level().addFreshEntity(fm);
            this.addJusticeEntities(fm);
            fm.setActivated(true);
            fm.setMovementTactic(Tactics.FOLLOW.id);
            fm.setTarget(attackTarget);
            fm.setController(this.getSelf());
        }
    }
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        boolean check = attackTarget != null && attackTarget.isAlive();
        if (check) {
            if (!this.isDazed(this.getSelf())) {
                if (!this.isCastingFog()){
                    if (!(this.getSelf() instanceof Creeper cr && this.getSelf().getMaxHealth() <= this.getSelf().getHealth())) {
                        this.castFog();
                    }
                }
                if (this.getSelf() instanceof Creeper cr){
                    ICreeper ic = ((ICreeper) cr);
                    if (this.getSelf().getMaxHealth() <= this.getSelf().getHealth()){
                        if (!ic.roundabout$isTransformed()){
                            ic.roundabout$setTransformed(true);
                            particleSpew();
                        }
                    } else {
                        if (ic.roundabout$isTransformed()){
                            ic.roundabout$setTransformed(false);
                            particleSpew();
                        }
                    }
                } if (this.getSelf() instanceof Raider rd){
                    IRaider ir = ((IRaider) rd);
                    if (!ir.roundabout$isTransformed()){
                        ir.roundabout$setTransformed(true);
                        particleSpew();
                    }
                }
                if (fogControlledEntities == null) {
                    fogControlledEntities = new ArrayList<>();
                }
                if (fogControlledEntities.size() < ClientNetworking.getAppropriateConfig().justiceStandUserMobMinionCount
                && this.getSelf().tickCount % 20 == 0){
                    if (!(this.getSelf() instanceof Creeper cr && this.getSelf().getMaxHealth() <= this.getSelf().getHealth())) {
                        initializeCorpse(rollCorpse(),attackTarget);
                    }
                }
            }
        } else {
            if (this.isCastingFog()){
                this.castFog();
            }
            if (this.getSelf() instanceof Creeper cr){
                ICreeper ic = ((ICreeper) cr);
                if (ic.roundabout$isTransformed()){
                    ic.roundabout$setTransformed(false);
                    particleSpew();
                }
            } if (this.getSelf() instanceof Raider rd) {
                IRaider ir = ((IRaider) rd);
                if (ir.roundabout$isTransformed()) {
                    ir.roundabout$setTransformed(false);
                    particleSpew();
                }
            }
        }
    }

    public void particleSpew(){
        this.self.level().playSound(null, this.self, ModSounds.FOG_MORPH_EVENT, SoundSource.PLAYERS, 0.36F, 1.0F);
        ((ServerLevel) this.self.level()).sendParticles(ModParticles.FOG_CHAIN, this.self.getX(),
                this.self.getY()+(this.self.getBbWidth()*0.6), this.self.getZ(),
                14, 0.4, 0.2, 0.4, 0.35);
    }
    public int lastHeldAge = 0;
    @Override
    public boolean pilotInputInteract(){
        if (Math.abs(lastHeldAge-this.getSelf().tickCount) >= 6){
        LivingEntity ent = getPilotingStand();
            if (ent != null) {
                Entity TE = MainUtil.getTargetEntity(ent, 100, 10);
                if (TE != null && !(TE instanceof StandEntity && !TE.isAttackable())) {
                    Vec3 vec3d = ent.getEyePosition(0);
                    Vec3 vec3d2 = ent.getViewVector(0);
                    Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                    BlockHitResult blockHit = ent.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, ent));
                    if ((blockHit.distanceTo(ent) - 1) < ent.distanceToSqr(TE)) {
                    } else {
                        if (TE instanceof FallenCreeper fm && fm.getController() == this.self.getId()) {
                            this.self.playSound(ModSounds.JUSTICE_SELECT_ATTACK_EVENT, 200F, 1.0F);
                            ModPacketHandler.PACKET_ACCESS.intToServerPacket(TE.getId(),
                                    PacketDataIndex.INT_STAND_ATTACK_2);
                            return true;
                        }
                    }
                }
                Vec3 vec3d = ent.getEyePosition(0);
                Vec3 vec3d2 = ent.getViewVector(0);
                Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                BlockHitResult blockHit = ent.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, ent));
                BlockPos bpos = blockHit.getBlockPos().relative(blockHit.getDirection());
                ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_3_EXTRA, bpos);
                this.self.playSound(ModSounds.JUSTICE_SELECT_EVENT, 200F, 1.2F);
            }
        }
        lastHeldAge = this.getSelf().tickCount;
        return false;
    }
    public void tickPower() {
        if  (!this.self.level().isClientSide()){
            if (((StandUser)this.self).roundabout$getSealedTicks() >= 0){
                if (this.isCastingFog()){
                    this.castFog();
                }
            }
        }
        if (this.self instanceof Player PL){
            cycleThroughJusticeEntities2();
            int getPilotInt = ((IPlayerEntity) PL).roundabout$getControlling();
            Entity getPilotEntity = this.self.level().getEntity(getPilotInt);
            if (this.self.level().isClientSide() && isPacketPlayer()) {

                if (getPilotEntity instanceof LivingEntity le) {

                    if (le.isRemoved() || !le.isAlive() ||
                            MainUtil.cheapDistanceTo2(le.getX(),le.getZ(),PL.getX(),PL.getZ())
                                    > getMaxPilotRange()) {
                        IPlayerEntity ipe = ((IPlayerEntity) PL);
                        ipe.roundabout$setIsControlling(0);
                        ModPacketHandler.PACKET_ACCESS.intToServerPacket(0,
                                PacketDataIndex.INT_UPDATE_PILOT);
                        ClientUtil.setCameraEntity(null);
                    } else {
                        StandEntity SE = getStandEntity(this.self);
                        if (SE != null && le.is(SE)) {
                            ClientUtil.setCameraEntity(le);
                        }
                    }
                } else {
                    ClientUtil.setCameraEntity(null);
                }
            }
        }
        if (this.self instanceof Player PE && PE.isSpectator()) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            if (ipe.roundabout$getShapeShift() != ShapeShifts.PLAYER.id){
                ipe.roundabout$shapeShift();
                ipe.roundabout$setShapeShift(ShapeShifts.PLAYER.id);
            }
        }
        super.tickPower();
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public StandEntity getNewStandEntity(){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == JusticeEntity.PIRATE){
            return ModEntities.JUSTICE_PIRATE.create(this.getSelf().level());
        } else if (((StandUser)this.getSelf()).roundabout$getStandSkin() == JusticeEntity.DARK_MIRAGE){
            return ModEntities.DARK_MIRAGE.create(this.getSelf().level());
        }
        return ModEntities.JUSTICE.create(this.getSelf().level());
    }

    @Override
    public byte getMaxLevel(){
        return 5;
    }

    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1) {
            amt = 50;
        } else if (currentLevel == 2){
            amt = 150;
        } else {
            amt = (100+((currentLevel-1)*100));
        }
        amt= (int) (amt*(ClientNetworking.getAppropriateConfig().standExperienceNeededForLevelupMultiplier *0.01));
        return amt;
    }
    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 5){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            } else if (level == 2 || level == 3 || level == 4){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            if (bt == JusticeEntity.FLAMED || bt == JusticeEntity.BLUE_FLAMED){
                return ModSounds.SUMMON_JUSTICE_2_EVENT;
            }
            return ModSounds.SUMMON_JUSTICE_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }


    @Override
    public void playSummonEffects(boolean forced){
        if (!forced) {
            Level lv = this.getSelf().level();
            if ((this.getSelf()) instanceof Player PE && lv.getBiome(this.getSelf().getOnPos()).is(Biomes.BASALT_DELTAS)){
                StandUser user = ((StandUser)PE);
                ItemStack stack = user.roundabout$getStandDisc();
                if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_JUSTICE)){
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    if (!ipe.roundabout$getUnlockedBonusSkin()){
                        if (!lv.isClientSide()) {
                            ipe.roundabout$setUnlockedBonusSkin(true);
                            lv.playSound(null, PE.getX(), PE.getY(),
                                    PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                            ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                                    PE.getY()+PE.getEyeHeight(), PE.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.2);
                            user.roundabout$setStandSkin(JusticeEntity.FLAMED);
                            ((ServerPlayer) ipe).displayClientMessage(
                                    Component.translatable("unlock_skin.roundabout.justice.bad_bone"), true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getDisplayPowerInventoryScale(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == JusticeEntity.DARK_MIRAGE){
            return super.getDisplayPowerInventoryScale();
        }
        return 14;
    }
    @Override
    public int getDisplayPowerInventoryYOffset(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == JusticeEntity.DARK_MIRAGE){
            return super.getDisplayPowerInventoryYOffset();
        }
        return -7;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isPiloting()){
            setSkillIcon(context, x, y, 1, StandIcons.JUSTICE_CAST_FOG, PowerIndex.SKILL_1);
            setSkillIcon(context, x, y, 2, StandIcons.JUSTICE_FOG_CHAIN, PowerIndex.SKILL_2);
            setSkillIcon(context, x, y, 3, StandIcons.JUSTICE_TACTICS, PowerIndex.NO_CD);
            setSkillIcon(context, x, y, 4, StandIcons.JUSTICE_PILOT_EXIT, PowerIndex.SKILL_4);
        } else {

            if (isHoldingSneak()){
                if (canExecuteMoveWithLevel(getVillagerMorphLevel())
                || canExecuteMoveWithLevel(getZombieMorphLevel()) ||
                        canExecuteMoveWithLevel(getSkeletonMorphLevel())) {
                    setSkillIcon(context, x, y, 1, StandIcons.JUSTICE_DISGUISE, PowerIndex.SKILL_3);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.JUSTICE_CAST_FOG, PowerIndex.NO_CD);
            }

            if (isHoldingSneak()){
                setSkillIcon(context, x, y, 2, StandIcons.JUSTICE_FOG_BLOCKS, PowerIndex.SKILL_2_SNEAK);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.JUSTICE_FOG_CHAIN, PowerIndex.SKILL_2);

            }

            if (isHoldingSneak()){
                if (canExecuteMoveWithLevel(getFogCloneLevel())) {
                    setSkillIcon(context, x, y, 3, StandIcons.JUSTICE_FOG_CLONES, PowerIndex.SKILL_3);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
            }

            setSkillIcon(context, x, y, 4, StandIcons.JUSTICE_PILOT, PowerIndex.SKILL_4);
        }
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.fog_sword",
                "instruction.roundabout.passive", StandIcons.JUSTICE_FOG_SWORD, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.cast_fog",
                "instruction.roundabout.press_skill", StandIcons.JUSTICE_CAST_FOG,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.fog_chain",
                "instruction.roundabout.press_skill", StandIcons.JUSTICE_FOG_CHAIN,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, getVillagerMorphLevel(), "ability.roundabout.fog_morph_2",
                "instruction.roundabout.press_skill_crouch", StandIcons.JUSTICE_DISGUISE_2,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, getZombieMorphLevel(), "ability.roundabout.fog_morph_3",
                "instruction.roundabout.press_skill_crouch", StandIcons.JUSTICE_DISGUISE_3,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, getSkeletonMorphLevel(), "ability.roundabout.fog_morph_4",
                "instruction.roundabout.press_skill_crouch", StandIcons.JUSTICE_DISGUISE_4,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 0, "ability.roundabout.fog_blocks",
                "instruction.roundabout.press_skill_crouch", StandIcons.JUSTICE_FOG_BLOCKS,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 118, 0, "ability.roundabout.fog_pilot",
                "instruction.roundabout.press_skill", StandIcons.JUSTICE_PILOT,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 80, 0, "ability.roundabout.corpse_army",
                "instruction.roundabout.passive", StandIcons.JUSTICE_CORPSE_ARMY,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 99, 0, "ability.roundabout.tactics",
                "instruction.roundabout.press_skill", StandIcons.JUSTICE_TACTICS,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 118, getFogCloneLevel(), "ability.roundabout.fog_clones",
                "instruction.roundabout.press_skill_crouch", StandIcons.JUSTICE_FOG_CLONES,3,level,bypas));
        return $$1;
    }
    @Override
    public float getBonusAttackSpeed() {
        return 1.3F;
    }

    public int getFogCloneLevel(){
        return 5;
    }
    public int getVillagerMorphLevel(){
        return 2;
    }
    public int getZombieMorphLevel(){
        return 3;
    }
    public int getSkeletonMorphLevel(){
        return 4;
    }
    @Override
    public boolean hasMoreThanOneSkin(){
        return true;
    }
    @Override
    public float getBonusPassiveMiningSpeed(){
        return 1.3F;
    }

    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(TheWorldEntity.PART_3_SKIN);
        $$1.add(JusticeEntity.SKELETON_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(JusticeEntity.MANGA_SKIN);
                $$1.add(JusticeEntity.OVA_SKIN);
            } if (Level > 2 || bypass){
                $$1.add(JusticeEntity.STRAY_SKIN);
                $$1.add(JusticeEntity.BOGGED);
            } if (Level > 3 || bypass){
                $$1.add(JusticeEntity.WITHER);
                $$1.add(JusticeEntity.TWILIGHT);
            } if (Level > 4 || bypass){
                $$1.add(JusticeEntity.TAROT);
                $$1.add(JusticeEntity.PIRATE);
                $$1.add(JusticeEntity.DARK_MIRAGE);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(JusticeEntity.FLAMED);
                $$1.add(JusticeEntity.BLUE_FLAMED);
            }
        }
        return $$1;
    }


    @Override
    public int getMaxPilotRange(){
        return ClientNetworking.getAppropriateConfig().justiceFogAndPilotRange;
    }

    public boolean hold1 = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
            if (this.getSelf().level().isClientSide) {
                if (!isHoldingSneak() || isPiloting()) {
                    if (keyIsDown) {
                        if (!hold1) {
                            hold1 = true;
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1);
                        }
                    } else {
                        hold1 = false;
                    }
                } else {
                    if (keyIsDown) {
                        if (!hold1) {
                            if (!this.onCooldown(PowerIndex.SKILL_3)){
                                hold1 = true;

                                if (canExecuteMoveWithLevel(getVillagerMorphLevel())
                                        || canExecuteMoveWithLevel(getZombieMorphLevel()) ||
                                        canExecuteMoveWithLevel(getSkeletonMorphLevel())) {
                                    ClientUtil.setJusticeScreen();
                                }
                            }
                        }
                    } else {

                        hold1 = false;
                    }
                }
            }
        super.buttonInput1(keyIsDown, options);
    }

    @Override
    public void buttonInputUse(boolean keyIsDown, Options options) {
        if (keyIsDown) {
        }
    }
    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            boolean done = false;
            Vec3 vector = null;
            if (effect.getDuration() == 13) {
                vector = new Vec3(0,
                        (this.self.getY()+10 - this.self.getY()),
                        0).normalize().scale(1.8F);


                done = true;
                this.self.setDeltaMovement(this.self.getDeltaMovement().add(vector.x,vector.y+0.2F,vector.z
                ));
            } else if (effect.getDuration() == 2) {
                vector = new Vec3(0,
                        (this.self.getY()-10 - this.self.getY()),
                        0).normalize().scale(1.8F);
                done = true;
                this.self.setDeltaMovement(this.self.getDeltaMovement().add(vector.x,vector.y+0.2F,vector.z
                ));
            }
            if (done){
                this.self.hurtMarked = true;
                this.self.hasImpulse = true;
                double random = (Math.random() * 1.2) - 0.6;
                double random2 = (Math.random() * 1.2) - 0.6;
                double random3 = (Math.random() * 1.2) - 0.6;
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.POOF, this.self.getX(),
                        this.self.getY() + this.self.getEyeHeight(), this.self.getZ(),
                        0,
                        vector.x+random,
                        vector.y+random2,
                        vector.z+random3,
                        0.15);

                this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                        this.self.getZ(), ModSounds.INHALE_EVENT, this.self.getSoundSource(), 100.0F, 0.5F);
            }
        }
    }
    public void justiceTacticsUse(byte context) {
        if (fogControlledEntities == null){
            fogControlledEntities = new ArrayList<>();
        }

        if (context == Tactics.CHANGE_TEAM.id){
            if (this.getSelf() instanceof Player PE){
                byte bt = ((IPlayerEntity) PE).roundabout$getTeamColor();
                bt++;
                if (bt > 3){
                    bt = 0;
                }
                ((IPlayerEntity) PE).roundabout$setTeamColor(bt);
               StandEntity SE = this.getStandEntity(this.self);
               if (SE instanceof JusticeEntity JE){
                   JE.setJusticeTeam(bt);
               }
            }
            return;
        }

        if (context == Tactics.CACKLE.id){
            StandEntity SE = this.getStandEntity(this.self);
            if (SE instanceof JusticeEntity JE){
                JE.setAnimation((byte) 2);
                JE.cackleTime = 100;
                this.self.level().playSound(null, JE.getX(),JE.getY(),
                        JE.getZ(), ModSounds.CACKLE_EVENT, this.self.getSoundSource(), 2.0F, 1F);
            }
            return;
        }

            List<LivingEntity> fogControlledEntities2 = new ArrayList<>(fogControlledEntities) {};
            if (!fogControlledEntities2.isEmpty()){
                for (LivingEntity value : fogControlledEntities2) {
                    if (value.isRemoved() || !value.isAlive()) {
                        removeJusticeEntities(value);
                    } else {
                        if (value instanceof FallenMob fm){
                            if (fm.controller != null && fm.controller.is(this.getSelf())){
                                if (context == Tactics.SELECT_ALL.id){
                                    if (!fm.getSelected()){
                                        fm.setSelected(true);
                                    }
                                } else if (context == Tactics.DESELECT_ALL.id){
                                    if (fm.getSelected()){
                                        fm.setSelected(false);
                                    }
                                } else if (context == Tactics.ROAM.id || context == Tactics.FOLLOW.id ||
                                        context == Tactics.STAY_PUT.id) {
                                    if (fm.getSelected()){
                                        fm.setMovementTactic(context);
                                    }
                                } else if (context == Tactics.KILL_ALL.id){
                                    if (fm.getSelected()) {
                                        fm.kill();
                                    }
                                } else {
                                    if (fm.getSelected()){
                                        fm.setTargetTactic(context);
                                        if (context == Tactics.PEACEFUL.id){
                                            fm.setAggressive(false);
                                            fm.setLastHurtByPlayer(null);
                                            fm.manualTarget = null;
                                            fm.setLastHurtByMob(null);
                                            fm.setTarget(null);
                                        } else if (context == Tactics.HUNT_TARGET.id){
                                            if (fm.manualTarget != null && !fm.manualTarget.isRemoved() &&
                                                    fm.manualTarget.isAlive()){
                                                if (fm.manualTarget instanceof Player PE){
                                                    fm.setLastHurtByPlayer(PE);
                                                }
                                                fm.setLastHurtByMob(fm.manualTarget);
                                                fm.setTarget(fm.manualTarget);
                                            }

                                        }
                                    }
                                }
                            } else {
                                removeJusticeEntities(value);
                            }
                        } else {
                            removeJusticeEntities(value);
                        }
                    }
                }
            }
    }
    @Override
    public boolean isPiloting(){
        if (this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            int zint = ipe.roundabout$getControlling();
            StandEntity sde = ((StandUser)PE).roundabout$getStand();
            if (sde != null && zint == sde.getId()){
                return true;
            }
        }
        return false;
    }
    public BlockPos bpos;
    public boolean hold3 = false;
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (!isPiloting()) {
            if (keyIsDown) {
                if (!inputDash) {
                    if (isHoldingSneak()) {
                        if (!this.onCooldown(PowerIndex.SKILL_3)) {

                            if (canExecuteMoveWithLevel(getFogCloneLevel())) {
                                if (this.getSelf() instanceof Player PE && ((IPlayerEntity)PE).roundabout$getShapeShift() > ShapeShifts.PLAYER.id){
                                    ModPacketHandler.PACKET_ACCESS.byteToServerPacket((byte) 0, PacketDataIndex.BYTE_CHANGE_MORPH);
                                }
                                this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.justiceFogClone);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3);
                            }
                        }
                        inputDash = true;
                    } else {
                        super.buttonInput3(keyIsDown, options);
                    }
                }
            } else {
                inputDash = false;
            }
        } else {
            if (keyIsDown) {
                if (!hold3) {
                    hold3 = true;
                    ClientUtil.setJusticeTacticsScreen();
                }
            } else {
                if (hold3) {
                    hold3 = false;
                }
            }
        }
    }
    public boolean hold2 = false;
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (!isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold2) {
                        hold2 = true;
                        if (!this.onCooldown(PowerIndex.SKILL_2)) {
                            StandEntity piloting = getPilotingStand();
                            if (isPiloting() && piloting != null && piloting.isAlive() && !piloting.isRemoved()) {
                                Vec3 vec3d = piloting.getEyePosition(0);
                                Vec3 vec3d2 = piloting.getViewVector(0);
                                Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                                BlockHitResult blockHit = this.self.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, piloting));
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                                ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_2, blockHit.getBlockPos());
                            } else {
                                Vec3 vec3d = this.self.getEyePosition(0);
                                Vec3 vec3d2 = this.self.getViewVector(0);
                                Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                                BlockHitResult blockHit = this.self.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.self));
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                                ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_2,blockHit.getBlockPos());
                            }
                        }
                    }
                } else {
                    hold2 = false;
                }
            } else {
                if (keyIsDown) {
                    if (!hold1) {
                        if (!isPiloting()) {
                            if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                                hold1 = true;
                                ClientUtil.setJusticeBlockScreen();
                            }
                        }
                    }
                } else {
                    hold1 = false;
                }
            }
        }
    }

    public boolean hold4 = false;
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (keyIsDown) {
                if (!hold4) {
                    hold4 = true;
                    if (isPiloting()){
                        if (this.self instanceof Player PE) {
                            IPlayerEntity ipe = ((IPlayerEntity) PE);
                            ipe.roundabout$setIsControlling(0);
                        }
                        ModPacketHandler.PACKET_ACCESS.intToServerPacket(0,
                                PacketDataIndex.INT_UPDATE_PILOT);
                    } else {
                        StandEntity entity = this.getStandEntity(this.self);
                        int L = 0;
                        if (entity != null){L=entity.getId();}

                        ModPacketHandler.PACKET_ACCESS.intToServerPacket(L,
                                PacketDataIndex.INT_UPDATE_PILOT);
                    }
                }
            } else {
                hold4 = false;
            }
        }
    }

    @Override
    public void poseStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand) && !isPiloting()){
            stand.setOffsetType(r);
        }
    }

    @Override
    public void setPiloting(int ID){
        if (this.self instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            Entity ent = this.self.level().getEntity(ID);
            if (ent != null && ent.is(this.getPilotingStand())){
                poseStand(OffsetIndex.LOOSE);
                ipe.roundabout$setIsControlling(ID);
            } else {
                ipe.roundabout$setIsControlling(ID);
                poseStand(OffsetIndex.FOLLOW);
            }
        }
    }

    private float flyingSpeed = 0.075F;
    private float walkingSpeed = 0.05F;

    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity){

        int $$13 = 0;
        if (entity instanceof JusticeEntity JE) {
                entity.xxa = kpi.leftImpulse;
                entity.zza = kpi.forwardImpulse;
                Vec3 vec32 = new Vec3(entity.xxa * walkingSpeed, 0, entity.zza * walkingSpeed);

                Vec3 delta = entity.getDeltaMovement();


                if (kpi.shiftKeyDown) {
                    $$13--;
                }

                if (kpi.jumping) {
                    $$13++;
                }

                if ($$13 != 0) {
                    entity.setDeltaMovement(delta.x, $$13 *flyingSpeed *5.0F, delta.z);
                } else {
                    entity.setDeltaMovement(delta.x, 0, delta.z);
                }
        }
    }
    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos){
        if (move == PowerIndex.POWER_2) {
            this.bpos = blockPos;
            return tryPower(move, forced);
        } else {
            if (move == PowerIndex.POWER_3_EXTRA){
                if (fogControlledEntities == null){
                    fogControlledEntities = new ArrayList<>();
                }

                List<LivingEntity> fogControlledEntities2 = new ArrayList<>(fogControlledEntities) {};
                if (!fogControlledEntities2.isEmpty()) {
                    for (LivingEntity value : fogControlledEntities2) {
                        if (value.isRemoved() || !value.isAlive()) {
                            removeJusticeEntities(value);
                        } else {
                            if (value instanceof FallenMob fm) {
                                if (fm.controller != null && fm.controller.is(this.getSelf())) {
                                    if (fm.getSelected()) {
                                        fm.getNavigation().moveTo(fm.getNavigation().createPath(blockPos, 0), 1);
                                        if (fm.getTarget() != null){
                                            fm.manualTarget = null;
                                            fm.setLastHurtByMob(null);
                                            fm.setTarget(null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
    }

    @Override
    public float getPermaCastRange(){
        return ClientNetworking.getAppropriateConfig().justiceFogAndPilotRange;
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1) {
            return this.castFog();
        } else if (move == PowerIndex.POWER_3) {
            return this.spawnClones();
        } else if (move == PowerIndex.POWER_2) {
            return this.yankChain();
        }
        return super.setPowerOther(move,lastMove);
    }

    public FogCloneEntity clone1;
    public FogCloneEntity clone2;
    
    /**Fog Clones*/
    public boolean spawnClones(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE) {
            FogCloneEntity fclone = ModEntities.FOG_CLONE.create(this.getSelf().level());
            FogCloneEntity fclone2 = ModEntities.FOG_CLONE.create(this.getSelf().level());
            fclone.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            fclone2.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            fclone.setPlayer(PE);
            fclone2.setPlayer(PE);
            fclone.setTimer(100);
            fclone2.setTimer(101);
            float first = ((this.getSelf().getYHeadRot()-25)%360);
            float second = ((this.getSelf().getYHeadRot()+25)%360);
            fclone.setYRot(first);
            fclone2.setYRot(second);
            fclone.lockedYRot = first;
            fclone2.lockedYRot = second;
            fclone.yRotO = first;
            fclone2.yRotO = second;
            this.getSelf().level().addFreshEntity(fclone);
            this.getSelf().level().addFreshEntity(fclone2);
            fclone.setYRot(first);
            fclone2.setYRot(second);
            fclone.lockedYRot = first;
            fclone2.lockedYRot = second;
            fclone.yRotO = first;
            fclone2.yRotO = second;

            fclone.setDeltaMovement(fclone.getForward().scale(0.3));
            fclone2.setDeltaMovement(fclone2.getForward().scale(0.3));

            clone1 = fclone;
            clone2 = fclone2;

            this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.justiceFogClone);
            ((ServerLevel) this.self.level()).sendParticles(ModParticles.FOG_CHAIN, this.self.getX(),
                    this.self.getY()+this.self.getEyeHeight(), this.self.getZ(),
                    50, 1, 1, 1, 0.1);
            this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                    this.self.getZ(), ModSounds.FOG_CLONE_EVENT, this.self.getSoundSource(), 2.0F, 1F);
        }
        return true;
    }

    @Override
    public Component getSkinName(byte skinId){
        return JusticeEntity.getSkinNameT(skinId);
    }
    @Override
    public boolean onCreateProjectile(Projectile proj){
        if (clone1 != null && clone1.isAlive()){
            clone1.goPoof();
        } if (clone2 != null && clone2.isAlive()){
            clone2.goPoof();
        }
        return false;
    }
    @Override
    public boolean interceptDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if (clone1 != null && clone1.isAlive()){
            clone1.goPoof();
        } if (clone2 != null && clone2.isAlive()){
            clone2.goPoof();
        }

        return false;
    }
    @Override
    public void gainExpFromStandardMining(BlockState $$1, BlockPos $$2) {
        if (hasStandActive(this.getSelf())) {
            if (!($$1.getBlock() instanceof IceBlock)) {
                if (Math.random() > 0.62) {
                    addEXP(1);
                }
            }
        }
    }
    @Override
    public boolean interceptSuccessfulDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if (hasStandActive(this.getSelf()) || $$0.is(ModDamageTypes.CORPSE)
                || $$0.is(ModDamageTypes.CORPSE_ARROW) || $$0.is(ModDamageTypes.CORPSE_EXPLOSION)){
            addEXP(1);
        }

        return false;
    }
    @Override
    public boolean interceptDamageEvent(DamageSource $$0, float $$1){
        if (clone1 != null && clone1.isAlive() && ((StandUser)clone1).roundabout$getStoredDamage() <= 0){
            if (!(((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))) {
                clone1.switchPlaces();
                return true;
            }
        } else if (clone2 != null && clone2.isAlive() && ((StandUser)clone2).roundabout$getStoredDamage() <= 0){
            if (!(((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))) {
                clone2.switchPlaces();
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean cancelCollision(Entity et) {
        if (et instanceof FogCloneEntity FC){
            return true;
        }
        return false;
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if ((slot == 2  && (!this.isHoldingSneak() || isPiloting()))){
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                return true;
            }
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public void tickPermaCast(){
        cycleThroughJusticeEntities();
    }
    public void tickJusticeInput(){

    }
    public boolean yankChain(){
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                List<Entity> entities = DamageHandler.genHitbox(this.self, this.self.getX(), this.self.getY(),
                        this.self.getZ(), 50, 50, 50);
                boolean success = false;
                for (Entity value : entities) {
                    if (!(!value.showVehicleHealth() || !value.isAttackable() || value.isInvulnerable() ||
                            !value.isAlive())) {
                        if (icast.roundabout$inPermaCastFogRange(value)) {
                            if (bpos != null){
                                if (value instanceof LivingEntity LE){
                                    if (LE.hasEffect(ModEffects.BLEED) ||
                                            (ClientNetworking.getAppropriateConfig().disableBleedingAndBloodSplatters
                                            && LE.getHealth() < LE.getMaxHealth())){
                                        double random = (Math.random() * 1.2) - 0.6;
                                        double random2 = (Math.random() * 1.2) - 0.6;
                                        double random3 = (Math.random() * 1.2) - 0.6;
                                        Vec3 vector = new Vec3((bpos.getX() - LE.getX()),
                                                (bpos.getY()+2 - LE.getY()),
                                                (bpos.getZ() - LE.getZ())).normalize().scale(1.8F);
                                        ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.POOF, value.getX(),
                                                value.getY() + value.getEyeHeight(), value.getZ(),
                                                0,
                                                vector.x+random,
                                                vector.y+random2,
                                                vector.z+random3,
                                                0.15);

                                        LE.setDeltaMovement(LE.getDeltaMovement().add(vector.x,vector.y*0.55+0.2F,vector.z
                                        ));
                                        LE.hurtMarked = true;
                                        LE.hasImpulse = true;
                                        success = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (success) {
                    addEXP(4);
                    int cdr = 80;
                    ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()),
                            PowerIndex.SKILL_2, cdr);
                    this.setCooldown(PowerIndex.SKILL_2, cdr);
                    this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                            this.self.getZ(), ModSounds.INHALE_EVENT, this.self.getSoundSource(), 100.0F, 0.5F);
                }
            }
        }
        return true;
    }

    @Override
    public byte getPermaCastContext(){
        return PermanentZoneCastInstance.FOG_FIELD;
    }
    @Override
    public boolean canSeeThroughFog(){
        return true;
    }
    public boolean castFog(){
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                icast.roundabout$addPermaCaster(this.getSelf());
            } else {
                icast.roundabout$removePermaCastingEntity(this.getSelf());
            }
        }
        return true;
    }
    public boolean isCastingFog(){
        IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
        if (icast.roundabout$isPermaCastingEntity(this.getSelf())) {
            return true;
        }
        return false;
    }
}
