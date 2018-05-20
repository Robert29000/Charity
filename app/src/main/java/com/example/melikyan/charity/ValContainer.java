package com.example.melikyan.charity;

/**
 * Created by melikyan on 20.05.2018.
 */

public class ValContainer<T>{
    private T value;

    public ValContainer(T value){
        this.value=value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
