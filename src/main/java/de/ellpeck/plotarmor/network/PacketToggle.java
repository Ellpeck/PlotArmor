package de.ellpeck.plotarmor.network;

import de.ellpeck.plotarmor.PlotArmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketToggle implements IMessage {

    private UUID player;
    private boolean value;

    public PacketToggle(UUID player, boolean value) {
        this.player = player;
        this.value = value;
    }

    public PacketToggle() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = new UUID(buf.readLong(), buf.readLong());
        this.value = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.player.getMostSignificantBits());
        buf.writeLong(this.player.getLeastSignificantBits());
        buf.writeBoolean(this.value);
    }

    public static class Handler implements IMessageHandler<PacketToggle, IMessage> {

        @Override
        public IMessage onMessage(PacketToggle message, MessageContext ctx) {
            EntityPlayer invoker = ctx.getServerHandler().player;
            EntityPlayer player = invoker.world.getPlayerEntityByUUID(message.player);
            if (player != null && player.canUseCommand(PlotArmor.PERMISSION_LEVEL, PlotArmor.ID)) {
                PlotArmor.setEnabled(player, message.value);
                // notify the player whose value was just updated
                PacketHandler.sendTo(player, new PacketPlayerList(player));
            }
            return null;
        }
    }
}
