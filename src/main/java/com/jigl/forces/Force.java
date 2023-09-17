package com.jigl.forces;

import com.jigl.bodies.Body;

/**
 * Generic force interface
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public interface Force {
    /**
     * Update the effects this force has on a single body 
     * given the time elapsed.
     * 
     * @param a the body to update
     * @param dt delta time
     */
    public void update(Body a, float dt);
}