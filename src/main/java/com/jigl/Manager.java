package com.jigl;

import java.lang.reflect.Constructor;

/**
 * Usage: To avoid unnecessary memory allocation we want to
 * instantiate a number of objects, like vectors or matrices,
 * as we need them and keep a collection of them. 
 * 
 * To prevent memory loss, every object obtained using {@method #next()}
 * must be returned in the using {@method #free()} in FILO order.
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public class Manager<T> {
    private static final int DEFAULT_SIZE = 1;
    private final Constructor<T> itemConstructor;
    private T[] items;
    private int index;

    /**
     * Constructor takes the Class object of the type it is storing and
     * the initial number of items to create.
     * 
     * 
     * @param itemClass the class from which the no args constructor will be used
     * @param numItems  the number of items to be created initially
     */
    public Manager(Class<T> itemClass, int numItems) {
        try {
            this.itemConstructor = itemClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.allocateResources(numItems);
    }

    /**
     * Constructor takes the Class object of the type it is storing.
     * Default number of resources allocated is {@value #DEFAULT_SIZE}.
     * 
     * 
     * @param cls the class from which the no args constructor will be used
     */
    public Manager(Class<T> itemClass) {
        this(itemClass, DEFAULT_SIZE);
    }

    private void allocateResources(int numResources) {
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

    /**
     * Construct a new instance of the item being stored
     * 
     * @return the new instance
     */
    private T createNewInstance() {
        try {
            return this.itemConstructor.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get the next resource from this manager. If it is
     * out of resources, it will allocate more growing 1.5x in size.
     * 
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
     * the reverse order that they were requested (FILO).
     * 
     * @param items the resources being returned
     */
    public void free(T... items) throws RuntimeException {
        for (T item : items)
            if (item != this.items[--this.index])
                throw new RuntimeException("Resource Lost! Item being returned was not the last distributed");
    }
}
