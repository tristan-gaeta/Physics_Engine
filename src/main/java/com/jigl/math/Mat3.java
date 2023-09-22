package com.jigl.math;

import org.joml.Matrix3f;

public class Mat3 extends Matrix3f implements AutoCloseable {
    @Override
    public void close() {
        Scratch.MAT3.free(this);
    }
}
