package com.ecom.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.ecom.util.Log;
import com.ecom.workers.MainThread;
import com.ecom.workers.SubThread;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class BackendListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> mainFuture;
    private ScheduledFuture<?> t1sub1Future;
    private ScheduledFuture<?> t1sub2Future;
    private ScheduledFuture<?> t2sub1Future;
    private ScheduledFuture<?> t2sub2Future;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newScheduledThreadPool(5);

        mainFuture = scheduler.scheduleAtFixedRate(new MainThread(), 0, 15, TimeUnit.SECONDS);

        t1sub1Future = scheduler.scheduleAtFixedRate(new SubThread("T1SUB1"), 0, 15, TimeUnit.SECONDS);
        t1sub2Future = scheduler.scheduleAtFixedRate(new SubThread("T1SUB2"), 0, 15, TimeUnit.SECONDS);
        t2sub1Future = scheduler.scheduleAtFixedRate(new SubThread("T2SUB1"), 0, 15, TimeUnit.SECONDS);
        t2sub2Future = scheduler.scheduleAtFixedRate(new SubThread("T2SUB2"), 0, 15, TimeUnit.SECONDS);

        Log.info("Backend Listener : ", "Scheduler started with periodic jobs");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            try {
                Log.info("Backend Listener : ", "Shutting down scheduler...");
                scheduler.shutdown();
                if (mainFuture != null) mainFuture.cancel(true);
                if (t1sub1Future != null) t1sub1Future.cancel(true);
                if (t1sub2Future != null) t1sub2Future.cancel(true);
                if (t2sub1Future != null) t2sub1Future.cancel(true);
                if (t2sub2Future != null) t2sub2Future.cancel(true);

                if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    Log.err("Backend Listener : ", "Scheduler did not terminate in time; forcing shutdown");
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.err("Backend Listener", "Interrupted while waiting for scheduler termination: " + e.getMessage());
            }
        }
    }
}
