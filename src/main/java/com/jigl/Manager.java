/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 *
 * To avoid unnecessary memory allocation, we will instantiate a number of 
 * objects, like vectors or matrices, and manage their allocation ourselves.
 * The Manager class is recreating how vector ops should work in the call stack.
 * To prevent memory loss, make sure every object obtained using the "next()" 
 * method is returned in the using the "free()" method.
 */

package com.jigl;

public class Manager<T> {
    private final T[] items;
    private int index;

    /**
     * Constructor takes the Class object of the type it is storing.
     * 
     * 
     * @param cls the class from which the constructor will be used
     * @param numItems the number of items to be created
     */
    public Manager(Class<T> cls, int numItems){
        this.items = (T[]) new Object[numItems];
        try {
            // instantiate items
            for (int i = 0; i < this.items.length; i++){
                this.items[i] = cls.getDeclaredConstructor().newInstance();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not instantiate Generic type");
        }
        this.index = 0;
    }

    /**
     * @return the next item in the stack
     */
    public T next(){
        return this.items[index++];
    }

    /**
     * @return True if there are any more resources to be allocated
     */
    public boolean hasNext(){
        return this.index < this.items.length;
    }

    /**
     * Returns given resources to the stack. Items MUST be returned in
     * the reverse order that they were requested.
     * 
     * @param items the resources being returned
     */
    public void free(T... items){
        for (T item: items)
            if (item != this.items[--this.index]) 
                throw new IllegalArgumentException("Resource Lost! Item being returned was not the last distributed");
    }
}
