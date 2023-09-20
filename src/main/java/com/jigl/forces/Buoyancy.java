package com.jigl.forces;

import com.jigl.World;
import com.jigl.bodies.Body;
import com.jigl.math.Scratch;
import com.jigl.math.Vec3;

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
    private Vec3 centerOfBuoyancy;

    public Buoyancy(float maxDepth, float liquidHeight, float liquidDensity, Vec3 centerOfBuoyancy) {
        this.maxDepth = maxDepth;
        this.liquidHeight = liquidHeight;
        this.liquidWeight = liquidDensity * World.GRAVITY;
        this.centerOfBuoyancy = centerOfBuoyancy;
    }

    @Override
    public void update(Body a, float dt) {
        try (Vec3 pos = Scratch.VEC3.next();) {
            a.getPosition(pos);
            float height = pos.y;
            if (height >= this.liquidHeight + this.maxDepth)
                return; // above water do nothing

            Vec3 force = (Vec3) pos.zero();
            force.y = this.liquidWeight * a.boundingVolume.getVolume();
            if (height > this.liquidHeight - this.maxDepth) {
                // partially submerged
                force.y *= (this.liquidHeight + this.maxDepth - height) / (2 * this.maxDepth);
            }
            // force.y -= this.damping * a.getDY(); // apply damping
            a.poke(force, this.centerOfBuoyancy);
        }
    }
}
