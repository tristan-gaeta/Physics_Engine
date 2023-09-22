package com.jigl.math;

import org.joml.Vector3f;

public class Vec3 extends Vector3f implements AutoCloseable {

    public Vec3() {
        super();
    }

    public Vec3(float x, float y, float z) {
        super();
        this.set(x, y, z);
    }

    @Override
    public void close() {
        Scratch.VEC3.free(this);
    }

}
