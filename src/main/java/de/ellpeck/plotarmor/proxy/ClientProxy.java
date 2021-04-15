package de.ellpeck.plotarmor.proxy;

import de.ellpeck.plotarmor.PlotArmor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

public class ClientProxy implements IProxy {

    public static final KeyBinding OPEN_KEYBIND = new KeyBinding("key." + PlotArmor.ID + ".open", KeyConflictContext.IN_GAME, Keyboard.KEY_P, "key.categories.multiplayer");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ClientRegistry.registerKeyBinding(OPEN_KEYBIND);
    }
}
