package com.jigl;

import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Global access to pre-allocated resources like matrices and vectors.
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Scratch {
    /** Scratch Vector3f Manager */
    public static final Manager<Vector3f> VEC3 = new Manager<>() {
        @Override
        protected Vector3f createNewInstance() {
            return new Vector3f();
        }
    };
    /** Scratch Matrix3f Manager */
    public static final Manager<Matrix3f> MAT3 = new Manager<>() {
        @Override
        protected Matrix3f createNewInstance() {
            return new Matrix3f();
        }
    };
    /** Scratch Quaternionf Manager */
    public static final Manager<Quaternionf> QUAT = new Manager<>() {
        @Override
        protected Quaternionf createNewInstance() {
            return new Quaternionf();
        }
    };

}
