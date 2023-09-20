package com.jigl.forces;


import com.jigl.bodies.Body;
import com.jigl.math.Vec3;

/**
 * Force applies constant acceleration.
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Gravity implements Force {
    private Vec3 g; //acceleration due to gravity (m/s^2)

    public Gravity(float x, float y, float z){
        this.g = new Vec3(x,y,z);
    }

    public Gravity(Vec3 g) {
        this.g = g;
    }

    @Override
    public void update(Body a, float dt) {
        if (a.isStatic()) return;
        a.applyForce(this.g, a.getMass());
    }

}
