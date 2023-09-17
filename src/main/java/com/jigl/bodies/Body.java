package com.jigl.bodies;

import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.jigl.Scratch;
import com.jigl.World;
import com.jigl.bounding.BoundingVolume;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public abstract class Body implements Collidable{
    /** Linear damping applied to this body every update */
    protected static final float LINEAR_DAMPING = 0.995f;
    /** Angular damping applied to this body every update */
    protected static final float ANGULAR_DAMPING = 0.995f;

    protected boolean isStatic;

    // angular motion
    protected Quaternionf orientation;
    protected Vector3f rotation;
    protected Vector3f torque;
    protected Vector3f angularAcc;
    protected Matrix3f inverseInertia;
    // linear motion
    protected Vector3f position;
    protected Vector3f velocity;
    protected Vector3f force;
    protected Vector3f acceleration;
    protected float inverseMass;

    public BoundingVolume boundingVolume;

    public Body() {
        this.isStatic = false;
        // angular motion
        this.orientation = new Quaternionf();
        this.rotation = new Vector3f();
        this.torque = new Vector3f();
        this.angularAcc = new Vector3f();
        this.inverseInertia = new Matrix3f();
        // linear motion
        this.position = new Vector3f();
        this.velocity = new Vector3f();
        this.force = new Vector3f();
        this.acceleration = new Vector3f(0, World.GRAVITY, 0);
    }

    public void poke(Vector3f force, Vector3f contact) {
        Vector3f v = Scratch.VEC3.next();
        contact.get(v);
        this.worldSpace(v);
        this.applyForceAt(force, v);
        Scratch.VEC3.free(v);
    }

    public void applyForceAt(Vector3f force, Vector3f source) {
        this.applyForce(force);
        Vector3f v = Scratch.VEC3.next();
        source.sub(this.position, v);
        v.cross(force);
        this.applyTorque(v);
        Scratch.VEC3.free(v);
    }

    public void applyForce(Vector3f force) {
        this.force.add(force);
    }

    public void applyForce(Vector3f force, float scalar) {
        this.force.fma(scalar, force);
    }

    public void applyTorque(Vector3f torque) {
        this.torque.add(torque);
    }

    public void applyTorque(Vector3f torque, float scalar) {
        this.torque.fma(scalar, torque);
    }

    public Vector3f localSpace(Vector3f v) {
        v.sub(this.position);
        this.orientation.transformInverse(v);
        return v;
    }

    public Vector3f localSpace(Vector3f v, Vector3f dest) {
        v.sub(this.position, dest);
        this.orientation.transformInverse(dest);
        return dest;
    }

    public Vector3f worldSpace(Vector3f v) {
        this.orientation.transform(v);
        v.add(this.position);
        return v;
    }

    public Vector3f worldSpace(Vector3f v, Vector3f dest) {
        this.orientation.transform(v, dest);
        dest.add(this.position);
        return dest;
    }

    public Matrix3f getInverseInertiaWord(Matrix3f dest) {
        // O' = R^T O R
        this.orientation.get(dest);
        dest.transpose();
        dest.mul(this.inverseInertia);
        dest.rotate(this.orientation);
        return dest;
    }

    public void update(float dt) {
        // acceleration from forces and torques
        Vector3f v = Scratch.VEC3.next();
        this.acceleration.fma(this.inverseMass, this.force, v);
        Matrix3f inverseInertiaWorld = this.getInverseInertiaWord(Scratch.MAT3.next());
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

        Scratch.VEC3.free(v);
    }

    private void updateOrientation(float dt) {
        Quaternionf q = Scratch.QUAT.next();
        q.set(this.rotation.x * dt, this.rotation.y * dt, this.rotation.z * dt, 0);
        q.mul(this.orientation);
        q.mul(0.5f);
        this.orientation.add(q);
        this.orientation.normalize();
        Scratch.QUAT.free(q);
    }

    public Vector3f getPosition(Vector3f dest) {
        return this.position.get(dest);
    }

    public Vector3f getVelocity(Vector3f dest) {
        return this.velocity.get(dest);
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

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public float getMass() {
        return 1 / this.inverseMass;
    }
}
