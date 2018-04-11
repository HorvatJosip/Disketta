package com.testing;

import java.util.Date;

public class TestObject {
    private int id, integer;
    private String string, anotherString;
    private boolean bool;
    private Date date;

    public TestObject(int id, int integer, String string, String anotherString, boolean bool, Date date) {
        this.id = id;
        this.integer = integer;
        this.string = string;
        this.anotherString = anotherString;
        this.bool = bool;
        this.date = date;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "id=" + id +
                ", integer=" + integer +
                ", string='" + string + '\'' +
                ", anotherString='" + anotherString + '\'' +
                ", bool=" + bool +
                ", date=" + date +
                '}';
    }
}
