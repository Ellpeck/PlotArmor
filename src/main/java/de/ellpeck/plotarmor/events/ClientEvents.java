package de.ellpeck.plotarmor.events;

import de.ellpeck.plotarmor.GuiPlotArmor;
import de.ellpeck.plotarmor.network.PacketButton;
import de.ellpeck.plotarmor.network.PacketHandler;
import de.ellpeck.plotarmor.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        // TODO check if the person has enough permission level (on the server too!)
        if (ClientProxy.OPEN_KEYBIND.isPressed()) {
            PacketHandler.sendToServer(new PacketButton(PacketButton.Action.OPEN));
            Minecraft.getMinecraft().displayGuiScreen(new GuiPlotArmor());
        }
    }

}
