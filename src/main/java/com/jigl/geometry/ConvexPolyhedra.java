package com.jigl.geometry;

import com.jigl.math.Quat;
import com.jigl.math.Scratch;
import com.jigl.math.Vec3;

public class ConvexPolyhedra implements Intersectable {
    public final Quat orientation;
    public final Vec3 position;
    private final float[] vertices;
    private final float[] faceNormals;
    private final float[] edges;

    public ConvexPolyhedra(float[] vertices, float[] normals, float[] edges, Vec3 pos, Quat orientation) {
        this.vertices = vertices;
        this.faceNormals = normals;
        this.edges = edges;
        this.position = pos;
        this.orientation = orientation;
    }

    @Override
    /**
     * Project this polyhedra onto this given
     * axis in world space
     * 
     * @param axis the axis in world space
     * @param dest where the min and max bounds will be stored
     */
    public void projectOntoAxis(Vec3 axis, float[] dest) {
        dest[0] = Float.POSITIVE_INFINITY;
        dest[1] = Float.NEGATIVE_INFINITY;
        try (Vec3 vertex = Scratch.VEC3.next()) {
            for (int i = 0; i < this.vertices.length; i += 3) {
                vertex.set(this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]);
                this.orientation.transformInverse(vertex).add(this.position);
                float component = axis.dot(vertex);
                if (component < dest[0])
                    dest[0] = component;
                if (component > dest[1])
                    dest[1] = component;
            }
        }
    }

    @Override
    public Vec3 worldSpace(Vec3 v) {
        this.orientation.transform(v);
        // v.add(this.position);
        return v;
    }

    @Override
    public Vec3 localSpace(Vec3 v) {
        // v.sub(this.position);
        this.orientation.transformInverse(v);
        return v;
    }

    @Override
    public float[] getFaceAxes() {
        return this.faceNormals;
    }

    @Override
    public float[] getVertices() {
        return this.vertices;
    }

    @Override
    public float[] getEdgeAxes() {
        return this.edges;
    }

}
