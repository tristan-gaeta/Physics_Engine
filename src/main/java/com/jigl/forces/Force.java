/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 * Generic force interface
 */
package com.jigl.forces;

import com.jigl.bodies.Body;

public interface Force {
    public void update(Body a, float dt);
}