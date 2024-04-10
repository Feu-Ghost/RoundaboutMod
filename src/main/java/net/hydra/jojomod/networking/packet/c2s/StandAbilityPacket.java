package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class StandAbilityPacket {
    public static void summon(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerWorld world = (ServerWorld) player.getWorld();
        server.execute(() -> {
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            userData.summonStand(world, false, true);
            ((IEntityDataSaver) player).getPersistentData().putLong("guard", (player.getWorld().getTime() + 200));
        });
    }
    public static void attack(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerWorld world = (ServerWorld) player.getWorld();
        server.execute(() -> {
            RoundaboutMod.LOGGER.info("attack");
            RoundaboutMod.LOGGER.info(String.valueOf(MyComponents.STAND_USER.get((LivingEntity) player).getPowerUser()));
            MyComponents.STAND_USER.get((LivingEntity) player).setPowerAttack();
            //DamageHandler.genPointHitbox(2,(LivingEntity) player, 5, player.getX(), player.getY(), player.getZ(), 2, 2, 2);
        });
    }

    public static void attackCancel(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerWorld world = (ServerWorld) player.getWorld();
        server.execute(() -> {
            RoundaboutMod.LOGGER.info("cancel");
        });
    }
}
