package de.ellpeck.plotarmor.events;

import de.ellpeck.plotarmor.GuiPlotArmor;
import de.ellpeck.plotarmor.PlotArmor;
import de.ellpeck.plotarmor.network.PacketOpen;
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
        if (event.phase != TickEvent.Phase.START)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null && ClientProxy.OPEN_KEYBIND.isPressed() && mc.player.getPermissionLevel() >= PlotArmor.PERMISSION_LEVEL) {
            mc.displayGuiScreen(new GuiPlotArmor());
            PacketHandler.sendToServer(new PacketOpen());
        }
    }

}
