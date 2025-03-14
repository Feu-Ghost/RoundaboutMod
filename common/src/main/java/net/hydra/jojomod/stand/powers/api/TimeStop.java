package net.hydra.jojomod.stand.powers.api;

import com.google.common.collect.ImmutableList;
import net.hydra.jojomod.event.TimeStopInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public interface TimeStop {
    ImmutableList<LivingEntity> getTimeStoppingEntities();
    boolean inTimeStopRange(Vec3i pos);
    boolean inTimeStopRange(Entity entity);
    LivingEntity inTimeStopRangeEntity(Vec3i pos);
    LivingEntity inTimeStopRangeEntity(Entity entity);
    boolean CanTimeStopEntity(Entity entity);
    boolean isTimeStoppingEntity(LivingEntity entity);
    void addTimeStoppingEntity(LivingEntity $$0);
    void removeTimeStoppingEntity(LivingEntity $$0);
    void streamTimeStopToClients();
    void streamTimeStopRemovalToClients(LivingEntity removedStoppingEntity);
    void processTSPacket(int timeStoppingEntity, double x, double y, double z, double range, int duration, int maxDuration);
    void processTSRemovePacket(int timeStoppingEntity);
    void removeTimeStoppingEntityClient(int w);
    void addTimeStoppingEntityClient(int timeStoppingEntity, double x, double y, double z, double range, int duration, int maxDuration);
    void processTSBlockEntityPacket(BlockEntity blockEntity);
    void tickTimeStoppingEntity();
    void tickAllTimeStops();

    void streamTileEntityTSToCLient(BlockPos blockEntity);
    TimeStopInstance getTimeStopperInstanceClient(Vec3 pos);
}
