package de.ellpeck.plotarmor.network;

import de.ellpeck.plotarmor.PlotArmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.function.Function;

public class PacketOpen implements IMessage {

    public PacketOpen() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<PacketOpen, IMessage> {

        @Override
        public IMessage onMessage(PacketOpen message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player.canUseCommand(PlotArmor.PERMISSION_LEVEL, PlotArmor.ID))
                return new PacketPlayerList(player.world.playerEntities);
            return null;
        }
    }
}
