package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.stand.powers.api.StandUserClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SoundStopPacket {

    public static void playSound(Minecraft client, ClientPacketListener handler,
                                 FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.level != null) {
            Entity User = client.level.getEntity(buf.readInt());
            if (User instanceof LivingEntity){
                ((StandUserClient)User).roundabout$clientQueSound(buf.readByte());
            }
        }
    }
    public static void stopSound(Minecraft client, ClientPacketListener handler,
                                  FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            Entity User = client.player.level().getEntity(buf.readInt());
            if (User instanceof LivingEntity){
                ((StandUserClient)User).roundabout$clientQueSoundCanceling(buf.readByte());
            }
        }
    }
}
