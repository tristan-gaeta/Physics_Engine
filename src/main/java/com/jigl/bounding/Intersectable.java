package com.jigl.bounding;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public interface Intersectable{
    public boolean intersectsGeneric(Intersectable other);
    public boolean intersects(BoundingSphere other);
    public boolean intersects(BoundingBox other);
}
