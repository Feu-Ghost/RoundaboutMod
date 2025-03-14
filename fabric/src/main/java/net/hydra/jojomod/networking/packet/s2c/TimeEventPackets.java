package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.stand.powers.api.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TimeEventPackets {
    public static void updateTSList(Minecraft client, ClientPacketListener handler,
                                 FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((TimeStop)client.player.level()).processTSPacket(buf.readInt(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),
                    buf.readInt(), buf.readInt());
        }
    }
    public static void updateTSRemovalList(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((TimeStop)client.player.level()).processTSRemovePacket(buf.readInt());
        }
    }
    public static void updatePermaCastingList(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((IPermaCasting)client.player.level()).roundabout$processPermaCastPacket(buf.readInt(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),
                    buf.readByte());
        }
    }
    public static void updatePermaCastingRemovalList(Minecraft client, ClientPacketListener handler,
                                           FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((IPermaCasting)client.player.level()).roundabout$processPermaCastRemovePacket(buf.readInt());
        }
    }

    public static void updateTileEntityTS(Minecraft client, ClientPacketListener handler,
                                          FriendlyByteBuf buf, PacketSender responseSender){
        if (client.player != null) {
            BlockEntity openedBlock = client.player.level().getBlockEntity(new BlockPos(buf.readInt(),buf.readInt(),buf.readInt()) );
            if (openedBlock != null){
                ((TimeStop)client.player.level()).processTSBlockEntityPacket(openedBlock);
            }
        }
    }
}
