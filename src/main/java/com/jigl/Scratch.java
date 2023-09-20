package com.jigl;

import com.jigl.math.Mat3;
import com.jigl.math.Quat;
import com.jigl.math.Vec3;

/**
 * Global access to pre-allocated resources like matrices and vectors.
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Scratch {
    /** Scratch Vector3 Manager */
    public static final Manager<Vec3> VEC3 = new Manager<>() {
        @Override
        protected Vec3 createNewInstance() {
            return new Vec3();
        }
    };
    /** Scratch Matrix3 Manager */
    public static final Manager<Mat3> MAT3 = new Manager<>() {
        @Override
        protected Mat3 createNewInstance() {
            return new Mat3();
        }
    };
    /** Scratch Quaternion Manager */
    public static final Manager<Quat> QUAT = new Manager<>() {
        @Override
        protected Quat createNewInstance() {
            return new Quat();
        }
    };
}
