package com.jigl.forces;

import com.jigl.bodies.Body;
import com.jigl.math.Scratch;
import com.jigl.math.Vec3;

/**
 * Spring force
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Spring implements Force {
    private Body anchor;
    private Vec3 anchorConnection;
    private Vec3 connection;
    private float springConstant;
    private float restLength;

    private Spring(Body anchor, float springConstant, float restLength) {
        this.anchor = anchor;
        this.springConstant = -springConstant;
        this.restLength = restLength;
    }

    @Override
    public void update(Body a, float dt) {
        // find distance between connections
        try (Vec3 v = Scratch.VEC3.next();
                Vec3 w = Scratch.VEC3.next();) {
            a.worldSpace(this.connection, v);
            anchor.worldSpace(this.anchorConnection, w);
            v.sub(w);
            v.get(w);
            float f = v.length();
            v.div(f); // normalize vector
            // determine scalar
            f = Math.abs(f - this.restLength);
            f *= this.springConstant;
            // apply force
            v.mul(f);
            a.applyForceAt(v, w);
        }
    }
}