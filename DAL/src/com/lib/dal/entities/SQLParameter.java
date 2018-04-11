package com.lib.dal.entities;

public class SQLParameter<T> {
    private boolean outputParam = false;
    private T value;
    private int outputType;
    //private int ordinalNumber;

    public SQLParameter(int outputType, T value) {
        this(outputType);
        this.value = value;
    }

    public SQLParameter(int outputType) {
        this.outputType = outputType;
        this.outputParam = true;
    }

    public SQLParameter(T value){
        this.value = value;
    }

    public void setValue(T value) { this.value = value; }

    public T getValue() { return value; }

    public boolean getOutputParam(){
        return outputParam;
    }

    public int getOutputType() { return outputType; }
}
