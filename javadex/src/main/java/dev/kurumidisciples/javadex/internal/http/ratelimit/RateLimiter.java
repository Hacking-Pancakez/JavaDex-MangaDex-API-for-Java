package dev.kurumidisciples.javadex.internal.http.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static final ConcurrentHashMap<EndpointLimits, Semaphore> rateLimiters = new ConcurrentHashMap<>();
    private static final Semaphore defaultRateLimiter = new Semaphore(5); // Default: 5 requests per second

    static {
        for (EndpointLimits limit : EndpointLimits.values()) {
            rateLimiters.put(limit, new Semaphore(limit.getRequestsPerTimePeriod()));
        }
    }

    public static void acquire(EndpointLimits limit) throws InterruptedException {
        Semaphore semaphore = rateLimiters.getOrDefault(limit, defaultRateLimiter);
        semaphore.acquire();
    }

    public static void release(EndpointLimits limit) {
        Semaphore semaphore = rateLimiters.getOrDefault(limit, defaultRateLimiter);
        int releaseTime = limit != null ? limit.getTimePeriodInMinutes() : 1; // Default: 1 second
        executor.schedule((Runnable) semaphore::release, releaseTime, TimeUnit.SECONDS);
    }
}