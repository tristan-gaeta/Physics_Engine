package physics_engine;

import org.joml.Vector3f;

public class DragForce implements Force {

    private float k1, k2, drag;
    private Vector3f force;

    public DragForce(float k1, float k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public void update(Body a, float dt) {
        // f = (k1*|v| + k2*|v|^2)*-normalize(v)
        a.getVelocity(this.force);
        this.drag = this.force.length();
        this.force.div(this.drag);
        this.drag = this.k1 * this.drag + this.k2 * this.drag * this.drag;
        this.force.mul(-this.drag);
        a.applyForce(this.force);
    }

}
