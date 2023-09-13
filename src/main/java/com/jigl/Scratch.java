/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 * Global access to pre-allocated resources like matrices and vectors
 */
package com.jigl;

import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Scratch {
    public static final Manager<Vector3f> VEC3 = new Manager<>(Vector3f.class, 0);
    public static final Manager<Matrix3f> MAT3 = new Manager<>(Matrix3f.class, 0);
    public static final Manager<Quaternionf> QUAT = new Manager<>(Quaternionf.class, 0);
}
