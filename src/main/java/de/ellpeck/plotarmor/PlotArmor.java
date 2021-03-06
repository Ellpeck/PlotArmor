package de.ellpeck.plotarmor;

import de.ellpeck.plotarmor.network.PacketHandler;
import de.ellpeck.plotarmor.proxy.IProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PlotArmor.ID, name = PlotArmor.NAME, version = PlotArmor.VERSION)
public class PlotArmor {

    public static final String ID = "plotarmor";
    public static final String NAME = "Plot Armor";
    public static final String VERSION = "@VERSION@";

    public static final int PERMISSION_LEVEL = 4;

    @SidedProxy(clientSide = "de.ellpeck.plotarmor.proxy.ClientProxy", serverSide = "de.ellpeck.plotarmor.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
        proxy.preInit(event);
    }

    public static void setEnabled(EntityPlayer player, boolean enabled) {
        player.getEntityData().setBoolean(ID, enabled);
    }

    public static boolean isEnabled(EntityPlayer player) {
        return player.getEntityData().getBoolean(ID);
    }
}
