package com.jigl.math;

import org.joml.Quaternionf;

public class Quat extends Quaternionf implements AutoCloseable {
    
    @Override
    public void close() {
        Scratch.QUAT.free(this);
    }
}
