package com.jigl.bodies;

import com.jigl.World;
import com.jigl.bounding.BoundingVolume;
import com.jigl.math.Mat3;
import com.jigl.math.Quat;
import com.jigl.math.Scratch;
import com.jigl.math.Vec3;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public abstract class Body implements Collidable {
    /** Linear damping applied to this body every update */
    protected static final float LINEAR_DAMPING = 0.995f;
    /** Angular damping applied to this body every update */
    protected static final float ANGULAR_DAMPING = 0.995f;

    protected boolean isStatic;

    // angular motion
    protected Quat orientation;
    protected Vec3 rotation;
    protected Vec3 torque;
    protected Vec3 angularAcc;
    protected Mat3 inverseInertia;
    // linear motion
    protected Vec3 position;
    protected Vec3 velocity;
    protected Vec3 force;
    protected Vec3 acceleration;
    protected float inverseMass;

    public BoundingVolume boundingVolume;

    public Body() {
        this.isStatic = false;
        // angular motion
        this.orientation = new Quat();
        this.rotation = new Vec3();
        this.torque = new Vec3();
        this.angularAcc = new Vec3();
        this.inverseInertia = new Mat3();
        // linear motion
        this.position = new Vec3();
        this.velocity = new Vec3();
        this.force = new Vec3();
        this.acceleration = new Vec3(0, World.GRAVITY, 0);
    }

    public void poke(Vec3 force, Vec3 contact) {
        try (Vec3 v = Scratch.VEC3.next();) {
            contact.get(v);
            this.worldSpace(v);
            this.applyForceAt(force, v);
        }
    }

    public void applyForceAt(Vec3 force, Vec3 source) {
        this.applyForce(force);
        try (Vec3 v = Scratch.VEC3.next();) {
            source.sub(this.position, v);
            v.cross(force);
            this.applyTorque(v);
        }
    }

    public void applyForce(Vec3 force) {
        this.force.add(force);
    }

    public void applyForce(Vec3 force, float scalar) {
        this.force.fma(scalar, force);
    }

    public void applyTorque(Vec3 torque) {
        this.torque.add(torque);
    }

    public void applyTorque(Vec3 torque, float scalar) {
        this.torque.fma(scalar, torque);
    }

    public Vec3 localSpace(Vec3 v) {
        v.sub(this.position);
        this.orientation.transformInverse(v);
        return v;
    }

    public Vec3 localSpace(Vec3 v, Vec3 dest) {
        v.sub(this.position, dest);
        this.orientation.transformInverse(dest);
        return dest;
    }

    public Vec3 worldSpace(Vec3 v) {
        this.orientation.transform(v);
        v.add(this.position);
        return v;
    }

    public Vec3 worldSpace(Vec3 v, Vec3 dest) {
        this.orientation.transform(v, dest);
        dest.add(this.position);
        return dest;
    }

    public Mat3 getInverseInertiaWord(Mat3 dest) {
        // O' = R^T O R
        this.orientation.get(dest);
        dest.transpose();
        dest.mul(this.inverseInertia);
        dest.rotate(this.orientation);
        return dest;
    }

    public void update(float dt) {
        // acceleration from forces and torques
        try (Vec3 v = Scratch.VEC3.next(); Mat3 inverseInertiaWorld = Scratch.MAT3.next();) {

            this.acceleration.fma(this.inverseMass, this.force, v);
            this.getInverseInertiaWord(inverseInertiaWorld);
            inverseInertiaWorld.transform(this.torque, this.angularAcc);
            // velocity from acc and impulse
            this.velocity.fma(dt, v);
            this.rotation.fma(dt, this.angularAcc);
            // apply damping
            this.velocity.mul((float) Math.pow(LINEAR_DAMPING, dt));
            this.rotation.mul((float) Math.pow(ANGULAR_DAMPING, dt));
            // position from velocities
            this.position.fma(dt, this.velocity);
            this.updateOrientation(dt);
            // clear forces and torque
            this.force.zero();
            this.torque.zero();
        }
    }

    private void updateOrientation(float dt) {
        try (Quat q = Scratch.QUAT.next();) {
            q.set(this.rotation.x * dt, this.rotation.y * dt, this.rotation.z * dt, 0);
            q.mul(this.orientation);
            q.mul(0.5f);
            this.orientation.add(q);
            this.orientation.normalize();
        }
    }

    public Vec3 getPosition(Vec3 dest) {
        return (Vec3) this.position.get(dest);
    }

    public Vec3 getVelocity(Vec3 dest) {
        return (Vec3) this.velocity.get(dest);
    }

    public float getX() {
        return this.position.x;
    }

    public float getY() {
        return this.position.y;
    }

    public float getZ() {
        return this.position.z;
    }

    public float getDX() {
        return this.velocity.x;
    }

    public float getDY() {
        return this.velocity.y;
    }

    public float getDZ() {
        return this.velocity.z;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public void setPosition(Vec3 position) {
        this.position.set(position);
    }

    public float getMass() {
        return 1 / this.inverseMass;
    }
}
