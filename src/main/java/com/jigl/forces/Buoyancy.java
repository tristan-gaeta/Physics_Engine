package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.Scratch;
import com.jigl.bodies.Body;
/**
 * Planar buoyancy force
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Buoyancy implements Force {
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
        Vector3f pos = Scratch.VEC3.next();
        a.getPosition(pos);
        float height = pos.y;
        if (height >= this.liquidHeight + this.maxDepth) return; // above water do nothing

        Vector3f force = pos.zero();
        force.y = this.liquidWeight * this.volume;
        if (height > this.liquidHeight - this.maxDepth) {
            // partially submerged
            force.y *= (this.liquidHeight + this.maxDepth - height) / (2 * this.maxDepth);
        }
        force.y -= this.damping * a.getDY(); // apply damping
        a.poke(force, this.centerOfBuoyancy);
        Scratch.VEC3.free(force);
    }
}
