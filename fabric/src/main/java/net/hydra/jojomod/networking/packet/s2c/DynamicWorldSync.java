package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DynamicWorldSync {
    public static void updateWorlds(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, PacketSender responseSender)
    {
        ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, Roundabout.location(buf.readUtf()));
        Roundabout.LOGGER.info("Got packet for dimension {}", LEVEL_KEY.toString());

        LocalPlayer player = client.player;

        player.connection.levels().add(LEVEL_KEY);
    }
}
