
package com.jigl;

import java.util.HashMap;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class PhysicsEngine implements Runnable {
    private long deltaTime;
    private long currentTime;
    private long prevTime;

    private long prevWriteTime;
    private long writeInterval = 1000;

    public World world;

    public PhysicsEngine() {
        this.world = new World();
    }

    @Override
    public void run() {
        this.prevTime = System.currentTimeMillis();
        Vector3f v = new Vector3f();

        while (true) {
            this.currentTime = System.currentTimeMillis();
            this.deltaTime = currentTime - this.prevTime;
            this.prevTime = currentTime;

            this.world.update(this.deltaTime * 1e-3f);
            if (this.currentTime - this.prevWriteTime > this.writeInterval) {
                this.prevWriteTime = this.currentTime;
                for (int i = 0; i < this.world.bodies.size(); i++) {
                    System.out.println(this.world.bodies.get(i).getPosition(v));
                }
            }
        }
    }
}
