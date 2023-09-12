/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
package com.jigl.bodies;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class Body {
    protected static final Vector3f SCRATCH_VEC3_0 = new Vector3f();
    protected static final Quaternionf SCRATCH_QUATERNION = new Quaternionf();

    protected boolean isStatic;
    protected float linearDamping = 0.995f;
    protected float angularDamping = 0.995f;
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
    // derived quantities
    protected Matrix3f inverseInertiaWorld;

    public Body() {
        this.isStatic = false;
        // angular motion
        this.orientation = new Quaternionf();
        this.rotation = new Vector3f();
        this.torque = new Vector3f();
        this.angularAcc = new Vector3f();
        this.inverseInertia = new Matrix3f();
        this.inverseInertiaWorld = new Matrix3f();
        // linear motion
        this.position = new Vector3f();
        this.velocity = new Vector3f();
        this.force = new Vector3f();
        this.acceleration = new Vector3f(0, -9.801f, 0);
        this.inverseMass = 1;
    }

    public void poke(Vector3f force, Vector3f contact) {
        contact.get(SCRATCH_VEC3_0);
        this.worldSpace(SCRATCH_VEC3_0);
        this.applyForceAt(force, SCRATCH_VEC3_0);
    }

    public void applyForceAt(Vector3f force, Vector3f source) {
        this.applyForce(force);
        source.sub(this.position, SCRATCH_VEC3_0);
        SCRATCH_VEC3_0.cross(force);
        this.applyTorque(SCRATCH_VEC3_0);
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

    public void getInverseInertiaWord(Matrix3f dest) {
        // O' = R^T O R
        this.orientation.get(dest);
        dest.transpose();
        dest.mul(this.inverseInertia);
        dest.rotate(this.orientation);
    }

    public void update(float dt) {
        // acceleration from forces and torques
        this.acceleration.fma(this.inverseMass, this.force, SCRATCH_VEC3_0);
        this.inverseInertiaWorld.transform(this.torque, this.angularAcc);
        // velocity from acc and impulse
        this.velocity.fma(dt, SCRATCH_VEC3_0);
        this.rotation.fma(dt, this.angularAcc);
        // apply damping
        this.velocity.mul((float) Math.pow(this.linearDamping, dt));
        this.rotation.mul((float) Math.pow(this.angularDamping, dt));
        // position from velocities
        this.position.fma(dt, this.velocity);
        this.updateOrientation(dt);
        // clear forces and torque
        this.force.zero();
        this.torque.zero();

        this.getInverseInertiaWord(this.inverseInertiaWorld);
    }

    protected void updateOrientation(float dt) {
        SCRATCH_QUATERNION.set(this.rotation.x * dt, this.rotation.y * dt, this.rotation.z * dt, 0);
        SCRATCH_QUATERNION.mul(this.orientation);
        SCRATCH_QUATERNION.mul(0.5f);
        this.orientation.add(SCRATCH_QUATERNION);
        this.orientation.normalize();
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
