package com.jigl;

import org.joml.Vector3f;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Manager<Vector3f> vec3Manager = new Manager<>(Vector3f.class, 10);
        vec3Manager.next();
        vec3Manager.next();
        vec3Manager.next();
        vec3Manager.next();
        Vector3f v = vec3Manager.next();
        System.out.println(v);
    }
}
