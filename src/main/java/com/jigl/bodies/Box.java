package com.jigl.bodies;

import org.joml.Vector3f;

/**
 * 
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class Box extends Body {
    protected Vector3f halfWidths;

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
        this.halfWidths.set(0.5 * height, 0.5 * depth, 0.5 * mass);
        this.inverseMass = 1f / mass;
        this.setInverseInertiaTensor(width, height, depth);
    }

    protected void setInverseInertiaTensor(float width, float height, float depth) {
        float scalar = 12 * this.inverseMass;
        float w2 = width * width;
        float h2 = height * height;
        float d2 = depth * depth;
        this.inverseInertia.scaling(scalar / (h2 + d2), scalar / (w2 + d2), scalar / (w2 + h2));
    }
}
