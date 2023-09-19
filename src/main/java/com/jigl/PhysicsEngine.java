package com.jigl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Engine runs update on world object
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class PhysicsEngine implements Runnable {
    /** throttle refresh rate at 125Hz */
    private static final long TARGET_DELTA_TIME = 16;

    private final ScheduledExecutorService executor;

    private long deltaTime;
    private long currentTime;
    private long prevTime;

    public World world;

    /**
     * Creates a new instance of a Physics Engine
     */
    public PhysicsEngine() {
        this.world = new World();
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Creates and starts a new thread to run
     * this engine.
     * 
     * @return The new thread
     */
    public void start() {
        this.prevTime = System.currentTimeMillis();
        this.executor.scheduleAtFixedRate(this, 0, TARGET_DELTA_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 
     * 
     */
    @Override
    public void run() {
        this.updateTime();
        this.step(this.deltaTime);
    }

    /**
     * Simulate a single time step
     * 
     * @param ms delta time in milliseconds
     */
    public void step(long ms) {
        this.world.update(ms * 1e-3f);
    }

    /**
     * Update the engine's time variable.
     */
    private void updateTime() {
        this.currentTime = System.currentTimeMillis();
        this.deltaTime = this.currentTime - this.prevTime;
        this.prevTime = this.currentTime;
    }
}
