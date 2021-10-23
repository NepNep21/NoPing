package me.nepnep.noping.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen extends Screen {
    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, "NoPing pinger"));

    @Shadow
    private ServerList serverList;

    @Final
    @Shadow
    private MultiplayerServerListPinger serverListPinger;

    @Inject(method = "init()V", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        addDrawableChild(new ButtonWidget(width - 70, height - 20, 70, 20, new LiteralText("Ping"), button -> {
            executor.execute(() -> {
                for (int i = 0; i < serverList.size(); i++) {
                    try {
                        serverListPinger.add(serverList.get(i), () -> {});
                    } catch (UnknownHostException e) {
                        LogManager.getLogger("NoPing pinger").error("Failed to ping a server", e);
                    }
                }
            });
        }));
    }
}
