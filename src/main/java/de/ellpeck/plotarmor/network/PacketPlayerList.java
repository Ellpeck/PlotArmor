package de.ellpeck.plotarmor.network;

import de.ellpeck.plotarmor.GuiPlotArmor;
import de.ellpeck.plotarmor.PlotArmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.stream.Collectors;

public class PacketPlayerList implements IMessage {

    private List<Player> players;

    public PacketPlayerList(EntityPlayer player) {
        this(Collections.singleton(player));
    }

    public PacketPlayerList(Collection<EntityPlayer> players) {
        this.players = players.stream()
                .map(p -> new Player(p.getUniqueID(), p.getDisplayNameString(), p.getHealth() / p.getMaxHealth(), PlotArmor.isEnabled(p)))
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
        pb.writeInt(this.players.size());
        for (Player player : this.players)
            player.toBytes(pb);
    }

    public static class Handler implements IMessageHandler<PacketPlayerList, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketPlayerList message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                // store player info on players
                for (Player player : message.players) {
                    EntityPlayer entity = mc.world.getPlayerEntityByUUID(player.id);
                    if (entity != null)
                        PlotArmor.setEnabled(entity, player.plotArmorEnabled);
                }

                // possibly give info to the admin ui
                if (mc.player.getPermissionLevel() >= PlotArmor.PERMISSION_LEVEL) {
                    GuiScreen screen = mc.currentScreen;
                    if (screen instanceof GuiPlotArmor)
                        ((GuiPlotArmor) screen).setPlayers(message.players);
                }
            });
            return null;
        }
    }

    public static class Player {

        public final UUID id;
        public final String name;
        public float healthPercentage;
        public boolean plotArmorEnabled;

        private Player(UUID id, String name, float healthPercentage, boolean plotArmorEnabled) {
            this.id = id;
            this.name = name;
            this.healthPercentage = healthPercentage;
            this.plotArmorEnabled = plotArmorEnabled;
        }

        private Player(PacketBuffer buf) {
            this.id = buf.readUniqueId();
            this.healthPercentage = buf.readFloat();
            this.plotArmorEnabled = buf.readBoolean();
            this.name = buf.readString(64);
        }

        private void toBytes(PacketBuffer buf) {
            buf.writeUniqueId(this.id);
            buf.writeFloat(this.healthPercentage);
            buf.writeBoolean(this.plotArmorEnabled);
            buf.writeString(this.name);
        }

    }
}
