/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.Scratch;
import com.jigl.bodies.Body;
public class Elastic implements Force {
    private Body anchor;
    private Vector3f anchorConnection;
    private Vector3f connection;
    private float springConstant;
    private float restLength;

    private Elastic(Body anchor, float springConstant, float restLength) {
        this.anchor = anchor;
        this.springConstant = -springConstant;
        this.restLength = restLength;
    }

    @Override
    public void update(Body a, float dt) {
        // find distance between connections
        Vector3f v = Scratch.VEC3.next();
        Vector3f w = Scratch.VEC3.next();
        a.worldSpace(this.connection, v);
        anchor.worldSpace(this.anchorConnection, w);
        v.sub(w);
        v.get(w);
        float f = v.length();
        if (f < this.restLength) return;
        v.div(f); //normalize vector
        // determine scalar
        f = Math.abs(f - this.restLength);
        f *= this.springConstant;
        // apply force
        v.mul(f);
        a.applyForceAt(v,w);
        Scratch.VEC3.free(w,v);
    }
}
