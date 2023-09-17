package com.jigl.bodies;

import com.jigl.collisions.Collidable;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class Sphere extends Body implements Collidable {
    private float radius;

    public Sphere(float r, float mass) {
        super();
        this.radius = r;
        this.inverseMass = 1 / mass;
        this.setInverseInertiaTensor(mass);
    }

    private void setInverseInertiaTensor(float mass) {
        float scalar = 5 / (2 * this.radius * this.radius * mass);
        this.inverseInertia.scaling(scalar);
    }

    @Override
    public void collideGeneric(Collidable other, Collidable.Contact[] data, int limit) {
        other.collide(this, data, limit);
    }

    @Override
    public void collide(Sphere s, Collidable.Contact[] data, int limit) {

    }
}
