package de.ellpeck.plotarmor.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.function.Consumer;

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
            message.action.action.accept(ctx);
            return null;
        }
    }

    public enum Action {
        OPEN(c -> {
            // TODO actually open the gui and send PacketPlayerList
        });

        public final Consumer<MessageContext> action;

        Action(Consumer<MessageContext> action) {
            this.action = action;
        }
    }
}
