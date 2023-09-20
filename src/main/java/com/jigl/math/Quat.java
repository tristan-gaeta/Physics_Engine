package com.jigl.math;

import org.joml.Quaternionf;

import com.jigl.Scratch;

public class Quat extends Quaternionf implements AutoCloseable {
    
    @Override
    public void close() {
        Scratch.QUAT.free(this);
    }

    @Override
    public void finalize() {
        System.err.println("Resource Lost.");
        this.close();
    }
}
