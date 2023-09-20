package com.jigl;

import java.util.LinkedList;
import java.util.List;

import com.jigl.bodies.Body;
import com.jigl.forces.Force;

/**
 * Bare bones world object
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
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
        for (int i = 1; i < this.bodies.size(); i++) {
            for (int j = 0; j < i; j++) {
                Body a = this.bodies.get(i);
                Body b = this.bodies.get(j);
                boolean test = a.boundingVolume.intersectsGeneric(b.boundingVolume);
                // System.out.println(a.getPosition(new Vec3()));
            }
        }
    }

    private static record Connection(Force force, Body body){};
}
