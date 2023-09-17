package com.jigl;

import java.util.LinkedList;
import java.util.List;
import com.jigl.bodies.Body;
import com.jigl.forces.Force;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 * Bare bones world object
 */
public class World {
    /** Acceleration due to gravity in m/s */
    public static final float GRAVITY = 9.807f; 

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
            pair.force.update(pair.body, dt);
        }
        for (Body b: this.bodies){
            b.update(dt);
        }
    }

    private record Connection(Force force, Body body){};
}
