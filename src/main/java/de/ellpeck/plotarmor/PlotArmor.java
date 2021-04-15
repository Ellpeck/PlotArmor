package de.ellpeck.plotarmor;

import de.ellpeck.plotarmor.network.PacketHandler;
import de.ellpeck.plotarmor.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PlotArmor.ID, name = PlotArmor.NAME, version = PlotArmor.VERSION)
public class PlotArmor {

    public static final String ID = "plotarmor";
    public static final String NAME = "Plot Armor";
    public static final String VERSION = "@VERSION@";

    @SidedProxy(clientSide = "de.ellpeck.plotarmor.proxy.ClientProxy", serverSide = "de.ellpeck.plotarmor.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
        proxy.preInit(event);
    }
}
