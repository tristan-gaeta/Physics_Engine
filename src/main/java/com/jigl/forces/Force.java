package com.jigl.forces;

import com.jigl.bodies.Body;

public interface Force {
    public void update(Body a, float dt);
}