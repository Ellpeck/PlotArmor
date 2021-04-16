package de.ellpeck.plotarmor.events;

import de.ellpeck.plotarmor.PlotArmor;
import de.ellpeck.plotarmor.network.PacketHandler;
import de.ellpeck.plotarmor.network.PacketPlayerList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer && !entity.world.isRemote) {
            // notify the player of their plot armor state
            EntityPlayer player = (EntityPlayer) entity;
            PacketHandler.sendTo(player, new PacketPlayerList(player));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!entity.world.isRemote && entity instanceof EntityPlayer && PlotArmor.isEnabled((EntityPlayer) entity)) {
            entity.setHealth(1);
            event.setCanceled(true);
        }
    }
}
