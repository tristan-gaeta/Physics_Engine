package com.jigl.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Usage: To avoid unnecessary memory allocation we want to
 * instantiate a number of objects, like vectors or matrices,
 * as we need them and keep a collection of them.
 * 
 * To prevent memory loss, every object obtained using {@method #next()}
 * must be returned in the using {@method #free()} in FILO order.
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 */
public abstract class Manager<T> {
    private static final int DEFAULT_SIZE = 1;
    private final List<T> items;
    private int index;

    /**
     * Constructor takes the Class object of the type it is storing and
     * the initial number of items to create.
     * 
     * 
     * @param numItems the number of items to be created initially
     */
    public Manager(int numItems) {
        this.items = new ArrayList<>(numItems);
        for (int i = 0; i < numItems; i++)
            this.items.add(this.createNewInstance());
    }

    /**
     * Constructor takes the Class object of the type it is storing.
     * Default number of resources allocated is {@value #DEFAULT_SIZE}.
     * 
     */
    public Manager() {
        this(DEFAULT_SIZE);
    }

    /**
     * Construct a new instance of the item being stored
     * 
     * @return the new instance
     */
    protected abstract T createNewInstance();

    /**
     * Get the next resource from this manager. If it is
     * out of resources, it will allocate one more.
     * 
     * @return the next item in the stack
     */
    public T next() {
        if (this.index == this.items.size())
            this.items.add(this.createNewInstance());
        return this.items.get(this.index++);
    }

    /**
     * Returns given resources to the stack. Items MUST be returned in
     * the reverse order that they were requested (FILO).
     * 
     * @param items the resources being returned
     */
    public void free(T... items) {
        for (T item : items)
            if (this.items.get(--this.index) != item) {
                System.err.println("Resource Lost!");
                Thread.dumpStack();
            }
    }

    public int itemsOnLoan() {
        return this.index;
    }
}
