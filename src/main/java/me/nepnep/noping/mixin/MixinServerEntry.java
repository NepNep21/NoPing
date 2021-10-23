package me.nepnep.noping.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public class MixinServerEntry {
    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIIIIIIZF)V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ThreadPoolExecutor;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;"))
    public Future pingNullifier(ThreadPoolExecutor instance, Runnable runnable) {
        RunnableFuture<Void> task = new FutureTask<>(() -> LogManager.getLogger("NoPing").debug("Stopping server ping"), null);
        instance.execute(task);
        return task;
    }
}
