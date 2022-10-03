package physics_engine;

import java.util.LinkedList;
import java.util.List;

public class World {
    public List<Body> bodies;
    private List<Connection> forces;

    public World(){
        this.bodies = new LinkedList<>();
        this.forces = new LinkedList<>();
    }

    public void addBody(Body b){
        this.bodies.add(b);
    }

    public void addConnection(Force f, Body b){
        this.forces.add(new Connection(f,b));
    }

    public void update(float dt){
        for (Connection pair: this.forces){
            pair.f.update(pair.b, dt);
        }
        for (Body b: this.bodies){
            b.update(dt);
        }
    }

    private class Connection{
        public Force f;
        public Body b;
        public Connection(Force f, Body b){
            this.f = f;
            this.b = b;
        }
    }
}
