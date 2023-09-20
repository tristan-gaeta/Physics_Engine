package com.jigl.math;

import org.joml.Matrix3f;

import com.jigl.Scratch;

public class Mat3 extends Matrix3f implements AutoCloseable {
    
    @Override
    public void close() {
        Scratch.MAT3.free(this);
    }

    @Override
    public void finalize() {
        System.err.println("Resource Lost.");
        this.close();
    }
}
