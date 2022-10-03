package physics_engine;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Body {
    private static final Vector3f SCRATCH_VEC3_0 = new Vector3f();
    private static final Quaternionf SCRATCH_QUATERNION = new Quaternionf();

    private boolean isStatic;
    private float linearDamping = 0.995f;
    private float angularDamping = 0.995f;
    // angular motion
    private Quaternionf orientation;
    private Vector3f rotation;
    private Vector3f torque;
    private Vector3f angularAcc;
    private Matrix3f inverseInertia;
    // linear motion
    private Vector3f position;
    private Vector3f velocity;
    private Vector3f force;
    private Vector3f acceleration;
    private float inverseMass;
    // derived quantities
    private Matrix3f inverseInertiaWorld;

    public Body(){
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
        this.acceleration = new Vector3f();
        this.inverseMass = 1;
    }

    public void createInverseInertiaWord() {
        // O' = R^-1 O R
        this.orientation.get(this.inverseInertiaWorld);
        this.inverseInertiaWorld.transpose();
        this.inverseInertiaWorld.mul(this.inverseInertia);
        this.inverseInertiaWorld.rotate(this.orientation);
    }

    public void poke(Vector3f force, Vector3f contact) {
        contact.get(Body.SCRATCH_VEC3_0);
        this.worldSpace(Body.SCRATCH_VEC3_0);
        this.applyForceAt(force, Body.SCRATCH_VEC3_0);
    }

    public void applyForceAt(Vector3f force, Vector3f source) {
        this.applyForce(force);
        source.sub(this.position, Body.SCRATCH_VEC3_0);
        Body.SCRATCH_VEC3_0.cross(force);
        this.applyTorque(Body.SCRATCH_VEC3_0);
    }

    public void applyTorque(Vector3f torque) {
        this.torque.add(torque);
    }

    public void applyTorque(Vector3f torque, float scalar) {
        torque.mulAdd(scalar, this.torque, this.torque);
    }

    public void applyForce(Vector3f force) {
        this.force.add(force);
    }

    public void applyForce(Vector3f force, float scalar) {
        force.mulAdd(scalar, this.force, this.force);
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

    public void update(float dt) {
        // acceleration from forces and torques
        this.force.mulAdd(this.inverseMass, this.acceleration, SCRATCH_VEC3_0);
        this.inverseInertiaWorld.transform(this.torque, this.angularAcc);
        // velocity from acc and impulse
        SCRATCH_VEC3_0.mulAdd(dt, this.velocity, this.velocity);
        this.angularAcc.mulAdd(dt, this.rotation, this.rotation);
        // apply damping
        this.velocity.mul((float) Math.pow(this.linearDamping, dt));
        this.rotation.mul((float) Math.pow(this.angularDamping, dt));
        // position from velocities
        this.velocity.mulAdd(dt, this.position, this.position);
        this.updateOrientation(dt);
        // clear forces and torque
        this.force.zero();
        this.torque.zero();

        this.createInverseInertiaWord();
    }

    private void updateOrientation(float dt) {
        SCRATCH_QUATERNION.set(this.rotation.x * dt, this.rotation.y * dt, this.rotation.z * dt, 0);
        SCRATCH_QUATERNION.mul(this.orientation);
        this.orientation.x += SCRATCH_QUATERNION.x * 0.5f;
        this.orientation.y += SCRATCH_QUATERNION.y * 0.5f;
        this.orientation.z += SCRATCH_QUATERNION.z * 0.5f;
        this.orientation.w += SCRATCH_QUATERNION.w * 0.5f;
        this.orientation.normalize();
    }

    public Vector3f manipulatePosition() {
        return this.position;
    }

    public Vector3f getPosition(Vector3f dest) {
        return this.position.get(dest);
    }

    public Vector3f manipulateVelocity() {
        return this.velocity;
    }

    public Vector3f getVelocity(Vector3f dest) {
        return this.velocity.get(dest);
    }

    public Matrix4f getTransform(Matrix4f dest){
        dest.translationRotate(this.position, this.orientation);
        return dest;
    }

    public float getX(){
        return this.position.x;
    }

    public boolean isStatic() {
        return this.isStatic;
    }
}
