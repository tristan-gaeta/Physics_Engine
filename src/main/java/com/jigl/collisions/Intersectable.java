/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
package com.jigl.collisions;

public interface Intersectable{
    public boolean intersectsGeneric(Intersectable other);
    public boolean intersects(BoundingSphere other);
    public boolean intersects(BoundingBox other);
}
