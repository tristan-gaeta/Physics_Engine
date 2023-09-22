package com.jigl.geometry;

import com.jigl.math.Quat;
import com.jigl.math.Vec3;

public class Box extends ConvexPolyhedra {
    private static final float[] VERTICES = {
            1, 1, 1,
            1, 1, -1,
            1, -1, 1,
            1, -1, -1,
            -1, 1, 1,
            -1, 1,- 1,
            -1, -1, 1,
            -1, -1, -1,
    };
    private static final float[] AXES = {
        1,0,0,
        0,1,0,
        0,0,1
    };
    public Box() {
        super(VERTICES, AXES, AXES, new Vec3(), new Quat());
    }
}
