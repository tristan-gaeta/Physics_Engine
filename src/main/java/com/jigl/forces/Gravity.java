package com.jigl.forces;

import org.joml.Vector3f;

import com.jigl.bodies.Body;

/**
 * Force applies constant acceleration.
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Gravity implements Force {
    private Vector3f g; //acceleration due to gravity (m/s^2)

    public Gravity(float x, float y, float z){
        this.g = new Vector3f(x,y,z);
    }

    public Gravity(Vector3f g) {
        this.g = g;
    }

    @Override
    public void update(Body a, float dt) {
        if (a.isStatic()) return;
        a.applyForce(this.g, a.getMass());
    }

}
