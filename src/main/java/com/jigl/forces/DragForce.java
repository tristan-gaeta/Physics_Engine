/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.Scratch;
import com.jigl.bodies.Body;

public class DragForce implements Force {
    private float k1, k2;

    public DragForce(float k1, float k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public void update(Body a, float dt) {
        // f = (k1*|v| + k2*|v|^2)*-normalize(v)
        Vector3f v = Scratch.VEC3.next();
        a.getVelocity(v);
        float mag = v.length();
        if (mag > 0) v.mul(mag);
        float f = this.k1 * mag + this.k2 * mag * mag;
        v.mul(-f);
        a.applyForce(v);
        Scratch.VEC3.free(v);
    }
}
