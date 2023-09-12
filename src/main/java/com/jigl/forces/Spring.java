package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.bodies.Body;

public class Spring implements Force {
    private static final Vector3f SCRATCH_VEC3_0 = new Vector3f();
    private static final Vector3f SCRATCH_VEC3_1 = new Vector3f();
    private static float SCRATCH_FLOAT;

    private Body anchor;
    private Vector3f anchorConnection;
    private Vector3f connection;
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
        a.worldSpace(this.connection, SCRATCH_VEC3_0);
        anchor.worldSpace(this.anchorConnection, SCRATCH_VEC3_1);
        SCRATCH_VEC3_0.sub(SCRATCH_VEC3_1);
        SCRATCH_VEC3_0.get(SCRATCH_VEC3_1);
        SCRATCH_FLOAT = SCRATCH_VEC3_0.length();
        SCRATCH_VEC3_0.div(SCRATCH_FLOAT); //normalize vector
        // determine scalar
        SCRATCH_FLOAT = Math.abs(SCRATCH_FLOAT - this.restLength);
        SCRATCH_FLOAT *= this.springConstant;
        // apply force
        SCRATCH_VEC3_0.mul(SCRATCH_FLOAT);
        a.applyForceAt(SCRATCH_VEC3_0,SCRATCH_VEC3_1);
    }
}