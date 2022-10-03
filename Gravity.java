package physics_engine;

import org.joml.Vector3f;

public class Gravity implements Force {
    private Vector3f g;

    public Gravity(float x, float y, float z){
        this.g = new Vector3f(x,y,z);
    }

    public Gravity(Vector3f g) {
        this.g = g;
    }

    @Override
    public void update(Body a, float dt) {
        if (a.isStatic()) return;
        a.applyForce(this.g, dt);
    }

}
