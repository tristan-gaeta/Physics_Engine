package com.jigl.bodies;

import com.jigl.bounding.BoundingSphere;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class Sphere extends Body  {
    private float radius;

    public Sphere(float r, float mass) {
        super();
        this.radius = r;
        this.inverseMass = 1 / mass;
        this.setInverseInertiaTensor(mass);
        this.boundingVolume = new BoundingSphere(this.position, r);
    }

    private void setInverseInertiaTensor(float mass) {
        float scalar = 5 / (2 * this.radius * this.radius * mass);
        this.inverseInertia.scaling(scalar);
    }

    @Override
    public void collideGeneric(Collidable other, Collidable.Contact contactData) {
        other.collide(this, contactData);
    }

    @Override
    public void collide(Sphere s, Collidable.Contact contactData) {
        
    }

    @Override
    public void collide(Box s, Contact contactData) {
    }
}
