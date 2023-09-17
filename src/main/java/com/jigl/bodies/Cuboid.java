package com.jigl.bodies;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class Cuboid extends Body {
    private float width, height, depth;

    public Cuboid(float width, float height, float depth, float mass) {
        super();
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.inverseMass = 1 / mass;
        this.createInverseInertiaTensor(mass);
    }

    private void createInverseInertiaTensor(float mass) {
        float scalar = 12 / mass;
        float w2 = this.width * this.width;
        float h2 = this.height * this.height;
        float d2 = this.depth * this.depth;
        this.inverseInertia.scaling(scalar / (h2 + d2), scalar / (w2 + d2), scalar / (w2 + h2));
    }
}
