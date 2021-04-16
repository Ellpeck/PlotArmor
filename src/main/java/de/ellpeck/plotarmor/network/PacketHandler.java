package de.ellpeck.plotarmor.network;

import de.ellpeck.plotarmor.PlotArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    private static SimpleNetworkWrapper network;

    public static void init() {
        network = new SimpleNetworkWrapper(PlotArmor.ID);
        network.registerMessage(PacketOpen.Handler.class, PacketOpen.class, 0, Side.SERVER);
        network.registerMessage(PacketPlayerList.Handler.class, PacketPlayerList.class, 1, Side.CLIENT);
        network.registerMessage(PacketToggle.Handler.class, PacketToggle.class, 2, Side.SERVER);
    }

    public static void sendTo(EntityPlayer player, IMessage message) {
        network.sendTo(message, (EntityPlayerMP) player);
    }

    public static void sendToServer(IMessage message) {
        network.sendToServer(message);
    }
}
