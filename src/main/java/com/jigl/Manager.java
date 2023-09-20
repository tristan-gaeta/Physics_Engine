package com.jigl;

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
    private T[] items;
    private int index;

    /**
     * Constructor takes the Class object of the type it is storing and
     * the initial number of items to create.
     * 
     * 
     * @param numItems the number of items to be created initially
     */
    public Manager(int numItems) {
        this.allocateResources(numItems);
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
     * create a new resource array of given size,
     * copying old items, and allocating more if necessary
     * 
     * @param numResources new number of resources
     */
    private void allocateResources(int numResources) {
        System.out.println("malloc "+numResources);
        T[] newArray = (T[]) new Object[numResources];
        for (int i = 0; i < this.index; i++)
            newArray[i] = this.items[i];
        for (int i = this.index; i < numResources; i++)
            newArray[i] = this.createNewInstance();
        this.items = newArray;
    }

    /**
     * Construct a new instance of the item being stored
     * 
     * @return the new instance
     */
    protected abstract T createNewInstance();

    /**
     * Get the next resource from this manager. If it is
     * out of resources, it will allocate more growing 1.5x in size.
     * 
     * @return the next item in the stack
     */
    public T next() {
        if (this.index == this.items.length) {
            int newSize = (3 * this.items.length) / 2 + 1;
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
    public void free(T... items) {
        for (T item : items)
            if (this.items[--this.index] != item)
                throw new RuntimeException("Resource Lost!");
    }
}
