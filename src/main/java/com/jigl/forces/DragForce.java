package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.bodies.Body;
public class DragForce implements Force {
    private static final Vector3f SCRATCH_VEC3 = new Vector3f();
    private static float SCRATCH_FLOAT;
    private float k1, k2;

    public DragForce(float k1, float k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public void update(Body a, float dt) {
        // f = (k1*|v| + k2*|v|^2)*-normalize(v)
        a.getVelocity(SCRATCH_VEC3);
        SCRATCH_FLOAT = SCRATCH_VEC3.length();
        if (SCRATCH_FLOAT > 0)
            SCRATCH_VEC3.mul(SCRATCH_FLOAT);
        SCRATCH_FLOAT = this.k1 * SCRATCH_FLOAT + this.k2 * SCRATCH_FLOAT * SCRATCH_FLOAT;
        SCRATCH_VEC3.mul(-SCRATCH_FLOAT);
        a.applyForce(SCRATCH_VEC3);
    }
}
