package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.Scratch;
import com.jigl.World;
import com.jigl.bodies.Body;

/**
 * Planar buoyancy force
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Buoyancy implements Force {
    /** Maximum depth before object is completely submerged */
    private float maxDepth;
    /** Height of the water in world space */
    private float liquidHeight;
    /** Density of water times gravity. pure water has a density of 1000 kg/m^3 */
    private float liquidWeight;
    /** Center of buoyancy in local space */
    private Vector3f centerOfBuoyancy;

    private float damping = 1000;

    public Buoyancy(float maxDepth, float liquidHeight, float liquidDensity, Vector3f centerOfBuoyancy) {
        this.maxDepth = maxDepth;
        this.liquidHeight = liquidHeight;
        this.liquidWeight = liquidDensity * World.GRAVITY;
        this.centerOfBuoyancy = centerOfBuoyancy;
    }

    @Override
    public void update(Body a, float dt) {
        Vector3f pos = Scratch.VEC3.next();
        a.getPosition(pos);
        float height = pos.y;
        if (height >= this.liquidHeight + this.maxDepth)
            return; // above water do nothing

        Vector3f force = pos.zero();
        force.y = this.liquidWeight * a.boundingVolume.getVolume();
        if (height > this.liquidHeight - this.maxDepth) {
            // partially submerged
            force.y *= (this.liquidHeight + this.maxDepth - height) / (2 * this.maxDepth);
        }
        // force.y -= this.damping * a.getDY(); // apply damping
        a.poke(force, this.centerOfBuoyancy);
        Scratch.VEC3.free(force);
    }
}
