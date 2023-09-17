package com.jigl.bodies;

import org.joml.Vector3f;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public interface Collidable {
    public static record Contact(Vector3f point, Vector3f normal, float depth){};
    public void collideGeneric(Collidable other, Contact contactData);
    public void collide(Sphere s, Contact contactData);
    public void collide(Box s, Contact contactData);
}
