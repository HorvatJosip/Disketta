package com.testing;

import java.util.Date;

public class TestObject {
    private int id, integer;
    private String string, anotherString;
    private boolean aBoolean;
    private Date date;

    public TestObject(int id, int integer, String string, String anotherString, boolean aBoolean, Date date) {
        this.id = id;
        this.integer = integer;
        this.string = string;
        this.anotherString = anotherString;
        this.aBoolean = aBoolean;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getInteger() {
        return integer;
    }

    public String getString() {
        return string;
    }

    public String getAnotherString() {
        return anotherString;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "id=" + id +
                ", integer=" + integer +
                ", string='" + string + '\'' +
                ", anotherString='" + anotherString + '\'' +
                ", aBoolean=" + aBoolean +
                ", date=" + date +
                '}' + "\n";
    }
}
