package com.jigl.bodies;

import com.jigl.collisions.Collidable;

public class Sphere extends Body implements Collidable {
    private float radius;
    private float radiusSquared;

    public Sphere(float r, float mass) {
        super();
        this.radius = r;
        this.radiusSquared = this.radius * this.radius;
        this.inverseMass = 1 / mass;
        this.createInverseInertiaTensor(mass);
    }

    private void createInverseInertiaTensor(float mass) {
        float scalar = 5 / (2 * this.radiusSquared * mass);
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
