package com.jigl;

import org.joml.Matrix3f;
import org.joml.Vector3f;

public class Scratch {
    public static final Manager<Vector3f> VEC3 = new Manager<>(Vector3f.class, 10);
    public static final Manager<Matrix3f> MAT3 = new Manager<>(Matrix3f.class, 10);
}
