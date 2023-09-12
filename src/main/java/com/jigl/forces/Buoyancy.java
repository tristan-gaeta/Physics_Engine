package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.bodies.Body;

public class Buoyancy implements Force {
    private static final Vector3f SCRATCH_VEC3 = new Vector3f();
    private static float SCRATCH_FLOAT;

    private float maxDepth;
    private float volume;
    private float liquidHeight; // in world space
    private float liquidWeight; // real water is 1000 kg/m^3
    private Vector3f centerOfBuoyancy; // in local space
    private float damping = 1000;

    public Buoyancy(float maxDepth, float volume, float liquidHeight, float liquidDensity, float gravity,
            Vector3f centerOfBuoyancy) {
        this.maxDepth = maxDepth;
        this.volume = volume;
        this.liquidHeight = liquidHeight;
        this.liquidWeight = liquidDensity * gravity;
        this.centerOfBuoyancy = centerOfBuoyancy;
    }

    @Override
    public void update(Body a, float dt) {
        a.getPosition(SCRATCH_VEC3);
        SCRATCH_FLOAT = SCRATCH_VEC3.y;
        if (SCRATCH_FLOAT >= this.liquidHeight + this.maxDepth)
            return; // above water
        SCRATCH_VEC3.zero();
        SCRATCH_VEC3.y = this.liquidWeight * this.volume;
        if (SCRATCH_FLOAT > this.liquidHeight - this.maxDepth) {
            // partially submerged
            SCRATCH_VEC3.y *= (this.liquidHeight + this.maxDepth - SCRATCH_FLOAT) / (2 * this.maxDepth);
        }
        SCRATCH_VEC3.y -= this.damping * a.getDY(); // apply damping
        a.poke(SCRATCH_VEC3, this.centerOfBuoyancy);
    }
}
