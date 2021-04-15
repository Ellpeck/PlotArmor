package de.ellpeck.plotarmor.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PacketPlayerList implements IMessage {

    private List<Player> players;

    public PacketPlayerList(Collection<EntityPlayer> players) {
        this.players = players.stream()
                .map(p -> new Player(p.getUniqueID(), p.getDisplayNameString(), p.getHealth() / p.getMaxHealth()))
                .collect(Collectors.toList());
    }

    public PacketPlayerList() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        this.players = new ArrayList<>();
        for (int i = pb.readInt(); i > 0; i--)
            this.players.add(new Player(pb));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        for (Player player : this.players)
            player.toBytes(pb);
    }

    public static class Handler implements IMessageHandler<PacketPlayerList, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketPlayerList message, MessageContext ctx) {
            // TODO add players to screen here
            return null;
        }
    }

    public static class Player {

        public final UUID id;
        public final String name;
        public final float health;

        private Player(UUID id, String name, float health) {
            this.id = id;
            this.name = name;
            this.health = health;
        }

        private Player(PacketBuffer buf) {
            this.id = buf.readUniqueId();
            this.name = buf.readString(256);
            this.health = buf.readFloat();
        }

        private void toBytes(PacketBuffer buf) {
            buf.writeUniqueId(this.id);
            buf.writeString(this.name);
            buf.writeFloat(this.health);
        }

    }
}
