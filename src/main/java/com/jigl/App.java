package com.jigl;

import com.jigl.bodies.Body;
import com.jigl.forces.DragForce;
import com.jigl.forces.Force;
import com.jigl.geometry.Box;
import com.jigl.geometry.Intersectable;
import com.jigl.math.Vec3;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class App {
    public static void main(String[] args) throws Exception {
        // PhysicsEngine engine = new PhysicsEngine();
        // Force f = new DragForce(0.001f, 0.01f);

        // Body a = new Box(1, 1, 1, 1);
        // engine.world.addBody(a);
        // engine.world.addConnection(f, a);

        // Body b = new Box(1, 1, 1, 1);
        // engine.world.addBody(b);
        // engine.world.addConnection(f, b);

        // engine.start();
        Box a = new Box();
        a.position.x += 2.0000001;
        // a.orientation.rotateXYZ(0.1f, 0.1f, 0.1f);

        Box b = new Box();
        // b.orientation.rotateXYZ(-0.1f, -0.1f, -0.1f);

        System.out.println(Intersectable.intersects(a, b));
    }
}
