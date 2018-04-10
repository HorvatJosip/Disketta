package com.lib.dal.entities;

public class SQLParameter<T> {
    private boolean outputParam = false;
    private T value;
    //private int ordinalNumber;

    public SQLParameter(boolean outputParam, T value) {
        this.outputParam = outputParam;
        this.value = value;
    }

    public SQLParameter(T value){
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean getOutputParam(){
        return outputParam;
    }
}
