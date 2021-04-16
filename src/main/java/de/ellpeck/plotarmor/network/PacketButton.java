package de.ellpeck.plotarmor.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.function.Function;

public class PacketButton implements IMessage {

    private Action action;

    public PacketButton(Action action) {
        this.action = action;
    }

    public PacketButton() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.action = Action.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.action.ordinal());
    }

    public static class Handler implements IMessageHandler<PacketButton, IMessage> {

        @Override
        public IMessage onMessage(PacketButton message, MessageContext ctx) {
            return message.action.action.apply(ctx);
        }
    }

    public enum Action {
        OPEN(c -> {
            EntityPlayer player = c.getServerHandler().player;
            return new PacketPlayerList(player.world.playerEntities);
        });

        public final Function<MessageContext, IMessage> action;

        Action(Function<MessageContext, IMessage> action) {
            this.action = action;
        }
    }
}
