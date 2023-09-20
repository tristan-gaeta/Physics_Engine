/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
package com.jigl.forces;

import com.jigl.bodies.Body;
import com.jigl.math.Scratch;
import com.jigl.math.Vec3;

public class DragForce implements Force {
    private float k1, k2;

    public DragForce(float k1, float k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public void update(Body a, float dt) {
        // f = (k1*|v| + k2*|v|^2) * -normalize(v)
        // = -(k1 + k2*|v|) * v
        try (Vec3 v = Scratch.VEC3.next();) {
            a.getVelocity(v);
            float mag = v.length();
            if (mag > 0) {
                float scalar = this.k1 + this.k2 * mag;
                v.mul(-scalar);
                a.applyForce(v);
            }
        }
    }
}
