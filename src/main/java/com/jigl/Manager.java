package com.jigl;

public class Manager<T> {
    private final T[] items;
    private int index;

    public Manager(Class<T> cls, int numItems){
        this.items = (T[]) new Object[numItems];
        try {
            for (int i = 0; i < this.items.length; i++){
                this.items[i] = cls.getDeclaredConstructor().newInstance();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not instantiate Generic type");
        }
        this.index = 0;
    }

    public synchronized T next(){
        return this.items[index++];
    }

    public synchronized boolean hasNext(){
        return this.index < this.items.length;
    }

    public synchronized void free(T... items){
        for (T item: items){
            this.items[this.index--] = item;
        }
    }
}
