package me.nepnep.noping;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "noping", name = "NoPing", version = "1.0.0")
public class NoPing {
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.debug("NoPing initialized");
    }
}
