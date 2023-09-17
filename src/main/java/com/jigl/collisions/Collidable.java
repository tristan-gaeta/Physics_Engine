package com.jigl.collisions;

import org.joml.Vector3f;

import com.jigl.bodies.Sphere;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public interface Collidable {
    public static record Contact(Vector3f point, Vector3f normal, float depth){};
    public void collideGeneric(Collidable other, Contact[] data, int limit);
    public void collide(Sphere s, Contact[] data, int limit);
}
