package com.jigl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Engine runs update on world object
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class PhysicsEngine {
    /** Target interval for engine */
    private static final long TARGET_DELTA_TIME_MILLIS = 16;

    /** Currently using nanoseconds as system time unit */
    private static final float SECONDS_PER_TIME_UNIT = 1e-9f;

    /** Executor will call step function at a fixed rate */
    private final ScheduledExecutorService stepExecutor;

    // thread safe pause and quite
    private final AtomicBoolean isPaused;
    private final AtomicBoolean quit;

    // timing
    private long deltaTime;
    private long currentTime;
    private long prevTime;

    /** This engine's current world object */
    public World world;

    /**
     * Creates a new instance of a Physics Engine
     */
    public PhysicsEngine() {
        this.world = new World();
        this.stepExecutor = Executors.newSingleThreadScheduledExecutor();
        this.isPaused = new AtomicBoolean();
        this.quit = new AtomicBoolean();
    }

    /**
     * Creates and starts a new thread to run this engine.
     * 
     * @return The new thread
     */
    public void start() {
        this.quit.set(false);
        this.isPaused.set(false);
        this.prevTime = this.getSystemTime();
        this.stepExecutor.scheduleAtFixedRate(this::step, 0, TARGET_DELTA_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

    /**
     * Pause the engine, by sleeping the thread it
     * is on, after it completes the current step.
     */
    public void pause() {
        this.isPaused.set(true);
    }

    /**
     * Resume the engine by notifying sleeping thread
     */
    public void resume() {
        if (!this.isPaused.getAndSet(false))
            return;
        synchronized (this.isPaused) {
            this.isPaused.notify();
        }
    }

    /**
     * Stop the thread currently running the engine
     */
    public void stop() {
        this.quit.set(true);
    }

    /**
     * Get the current system time
     * 
     * @return system time in nanoseconds
     */
    private long getSystemTime() {
        return System.nanoTime();
    }

    /**
     * Simulate a single time step
     * 
     */
    private void step() {
        this.updateTime();
        // pause
        if (this.isPaused.get()) {
            synchronized (this.isPaused) {
                try {
                    this.isPaused.wait();
                    this.prevTime = this.getSystemTime();
                } catch (InterruptedException e) {
                }
            }
        }
        // update
        this.world.update(this.deltaTime * SECONDS_PER_TIME_UNIT);
        // quit
        if (this.quit.get())
            this.stepExecutor.shutdown();
    }

    /**
     * Update the engine's time variables.
     */
    private void updateTime() {
        this.currentTime = this.getSystemTime();
        this.deltaTime = this.currentTime - this.prevTime;
        this.prevTime = this.currentTime;
    }
}
