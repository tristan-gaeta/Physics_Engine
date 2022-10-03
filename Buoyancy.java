package physics_engine;

import org.joml.Vector3f;

public class Buoyancy implements Force {
    private static final Vector3f SCRATCH_VEC3 = new Vector3f();
    private static float SCRATCH_FLOAT;

    private float maxDepth;
    private float liquidHeight; // in world space
    private float liquidDensity; // real water is 1000 kg/m^3
    private float volume;
    private Vector3f centerOfBuoyancy; // in local space

    public Buoyancy(float maxDepth, float volume, float liquidHeight, float liquidDensity, Vector3f centerOfBuoyancy) {
        this.maxDepth = maxDepth;
        this.volume = volume;
        this.liquidHeight = liquidHeight;
        this.liquidDensity = liquidDensity;
        this.centerOfBuoyancy = centerOfBuoyancy;
    }

    @Override
    public void update(Body a, float dt) {
        a.getPosition(SCRATCH_VEC3);
        SCRATCH_FLOAT = a.worldSpace(SCRATCH_VEC3).y;
        if (SCRATCH_FLOAT >= this.liquidHeight + this.maxDepth) return; // above water
        SCRATCH_VEC3.zero();
        SCRATCH_VEC3.y = this.liquidDensity * this.volume;
        if (SCRATCH_FLOAT > this.liquidHeight - this.maxDepth){ 
            // partially submerged
            SCRATCH_VEC3.y *= (SCRATCH_FLOAT - this.maxDepth - this.liquidHeight) / 2 * this.maxDepth;
        }
        a.poke(SCRATCH_VEC3, this.centerOfBuoyancy);
    }

}
