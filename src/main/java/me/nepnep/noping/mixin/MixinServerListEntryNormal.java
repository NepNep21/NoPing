package me.nepnep.noping.mixin;

import me.nepnep.noping.NoPing;
import net.minecraft.client.gui.ServerListEntryNormal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Mixin(ServerListEntryNormal.class)
public class MixinServerListEntryNormal {
    @Redirect(method = "drawEntry(IIIIIIIZF)V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ThreadPoolExecutor;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;"))
    public Future pingNullifier(ThreadPoolExecutor instance, Runnable runnable) {
        RunnableFuture<Void> task = new FutureTask<>(() -> NoPing.LOGGER.debug("Stopping server ping"), null);
        instance.execute(task);
        return task;
    }
}
