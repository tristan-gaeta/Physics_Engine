package com.jigl;

import java.util.HashMap;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PhysicsEngine implements Runnable {
    private long deltaTime;
    private long currentTime;
    private long prevTime;

    private long prevWriteTime;
    private long writeInterval = 16666666;

    public World world;
    private HashMap<Integer, Matrix4f> sharedData;
    private Matrix4f sharedDatum;

    public PhysicsEngine(HashMap<Integer, Matrix4f> sharedData) {
        this.world = new World();
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        this.prevTime = System.nanoTime();
        Vector3f v = new Vector3f();
        Quaternionf q = new Quaternionf();

        while (true) {
            this.currentTime = System.nanoTime();
            this.deltaTime = currentTime - this.prevTime;
            this.prevTime = currentTime;

            this.world.update(this.deltaTime * 1e-9f);
            if (this.currentTime - this.prevWriteTime > this.writeInterval) {
                this.prevWriteTime = this.currentTime;
                for (int i = 0; i < this.world.bodies.size(); i++) {
                    v = this.world.bodies.get(i).getPosition(v);
                    // q = this.world.bodies.get(i).get;

                    this.sharedDatum = this.sharedData.get(i);
                    synchronized (this.sharedDatum) {
                        sharedDatum.translationRotate(v, q);
                    }
                }
            }
        }
    }
}
