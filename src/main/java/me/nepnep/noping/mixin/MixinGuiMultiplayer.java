package me.nepnep.noping.mixin;

import me.nepnep.noping.NoPing;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.ServerPinger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.UnknownHostException;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {
    @Shadow
    private ServerList savedServerList;

    @Final
    @Shadow
    private ServerPinger oldServerPinger;

    @Inject(method = "createButtons()V", at = @At("HEAD"))
    public void createButtons(CallbackInfo ci) {
        addButton(new GuiButton(323, width - 70, height - 20, 70, 20, "Ping"));
    }

    @Inject(method = "actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 323) {
            new Thread(() -> {
                for (int i = 0; i < savedServerList.countServers(); i++) {
                    try {
                        oldServerPinger.ping(savedServerList.getServerData(i));
                    } catch (UnknownHostException e) {
                        NoPing.LOGGER.error("Failed to ping a server", e);
                    }
                }
            }, "NoPing pinger").start();
        }
    }
}
