package com.jigl;

import com.jigl.bodies.Body;
import com.jigl.bodies.Box;
import com.jigl.forces.DragForce;
import com.jigl.forces.Force;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class App {
    public static void main(String[] args) throws Exception {
        PhysicsEngine engine = new PhysicsEngine();
        Force f = new DragForce(0.001f, 0.01f);

        Body a = new Box(1, 1, 1, 1);
        engine.world.addBody(a);
        engine.world.addConnection(f, a);

        Body b = new Box(1, 1, 1, 1);
        engine.world.addBody(b);
        engine.world.addConnection(f, b);

        engine.start();

        // engine.stop();
    }
}
