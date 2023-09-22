package com.jigl.geometry;

import com.jigl.math.Scratch;
import com.jigl.math.Vec3;

public interface Intersectable {
    public static final float[] A_BOUNDS = new float[2];
    public static final float[] B_BOUNDS = new float[2];

    public static boolean intersects(Intersectable a, Intersectable b) {
        if (!checkLocalAxes(a, b) || !checkLocalAxes(b, a))
            return false;
        try (Vec3 aEdge = Scratch.VEC3.next(); Vec3 bEdge = Scratch.VEC3.next();) {
            float[] aSides = a.getEdgeAxes();
            float[] bSides = b.getEdgeAxes();
            for (int i = 0; i < aSides.length; i += 3) {
                for (int j = 0; j < bSides.length; j += 3) {
                    aEdge.set(aSides[i], aSides[i + 1], aSides[i + 2]);
                    bEdge.set(bSides[j], bSides[j + 1], bSides[j + 2]);
                    a.worldSpace(aEdge);
                    b.worldSpace(bEdge);
                    Vec3 axis = (Vec3) aEdge.cross(bEdge);
                    float length = axis.length();
                    if (length > 0) {
                        axis.mul(1f / length);
                        a.projectOntoAxis(axis, A_BOUNDS);
                        b.projectOntoAxis(axis, B_BOUNDS);
                        if (A_BOUNDS[0] > B_BOUNDS[1] || A_BOUNDS[1] < B_BOUNDS[0])
                            return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean checkLocalAxes(Intersectable a, Intersectable b) {
        try (Vec3 axis = Scratch.VEC3.next()) {
            /* Check the axes from a */
            float[] aAxes = a.getFaceAxes();
            for (int i = 0; i < aAxes.length; i += 3) {
                axis.set(aAxes[i], aAxes[i + 1], aAxes[i + 2]);
                a.worldSpace(axis);
                b.projectOntoAxis(axis, B_BOUNDS);
                a.projectOntoAxis(axis, A_BOUNDS);
                if (A_BOUNDS[0] > B_BOUNDS[1] || A_BOUNDS[1] < B_BOUNDS[0])
                    return false;
            }
        }
        return true;
    }

    public Vec3 localSpace(Vec3 v);

    public Vec3 worldSpace(Vec3 v);

    public float[] getFaceAxes();

    public float[] getVertices();

    public float[] getEdgeAxes();

    public void projectOntoAxis(Vec3 axis, float[] dest);
}
