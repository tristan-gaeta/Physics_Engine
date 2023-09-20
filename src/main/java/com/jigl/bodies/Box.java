package com.jigl.bodies;


import com.jigl.bounding.BoundingBox;
import com.jigl.math.Vec3;

/**
 * 
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class Box extends Body {
    protected Vec3 halfWidths;

    /**
     * Create a new cuboid with given dimensions and mass
     * 
     * @param width
     * @param height
     * @param depth
     * @param mass
     */
    public Box(float width, float height, float depth, float mass) {
        super();
        this.halfWidths = new Vec3(0.5f * height, 0.5f * depth, 0.5f * mass);
        this.inverseMass = 1f / mass;
        this.setInverseInertiaTensor(width, height, depth);
        this.boundingVolume = new BoundingBox(this.position, this.orientation, this.halfWidths);
    }

    /**
     * Set the inverse inertia tensor using the current inverse mass
     * and given dimensions.
     * 
     * @param width
     * @param height
     * @param depth
     */
    protected void setInverseInertiaTensor(float width, float height, float depth) {
        float scalar = 12 * this.inverseMass;
        float w2 = width * width;
        float h2 = height * height;
        float d2 = depth * depth;
        this.inverseInertia.scaling(scalar / (h2 + d2), scalar / (w2 + d2), scalar / (w2 + h2));
    }

    @Override
    public void collideGeneric(Collidable other, Contact contactData) {
        other.collide(this, contactData);
    }

    @Override
    public void collide(Sphere s, Contact contactData) {

    }

    @Override
    public void collide(Box s, Contact contactData) {

    }
}
