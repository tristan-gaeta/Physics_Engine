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
    private final Class<T> itemClass;
    private T[] items;
    private int index;

    /**
     * Constructor takes the Class object of the type it is storing.
     * 
     * 
     * @param cls      the class from which the constructor will be used
     * @param numItems the number of items to be created
     */
    public Manager(Class<T> itemClass, int numItems) {
        this.itemClass = itemClass;
        this.allocateResources(numItems);
    }

    public void allocateResources(int numResources) {
        T[] newArray = (T[]) new Object[numResources];
        // copy old items
        for (int i = 0; i < this.index; i++) {
            newArray[i] = this.items[i];
        }
        // instantiate new resources
        for (int i = this.index; i < newArray.length; i++) {
            newArray[i] = this.createNewInstance();
        }
        this.items = newArray;
    }

    private T createNewInstance() {
        try {
            return this.itemClass.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not instantiate Generic type: " + this.itemClass);
        }
    }

    /**
     * @return the next item in the stack
     */
    public T next() {
        if (this.index == this.items.length) {
            int newSize = (this.items.length * 3) / 2 + 1;
            this.allocateResources(newSize);
        }
        return this.items[index++];
    }

    /**
     * Returns given resources to the stack. Items MUST be returned in
     * the reverse order that they were requested.
     * 
     * @param items the resources being returned
     */
    public void free(T... items) {
        for (T item : items)
            if (item != this.items[--this.index])
                throw new IllegalArgumentException("Resource Lost! Item being returned was not the last distributed");
    }
}
