package com.testing;

import com.lib.dal.access.DBExecutor;
import com.lib.dal.entities.DBConfig;
import com.lib.dal.entities.SQLParameter;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

class Testiranje{
    int id, integer;
    String string, anotherString;
    boolean bool;
    Date date;

    public Testiranje(int id, int integer, String string, String anotherString, boolean bool, Date date) {
        this.id = id;
        this.integer = integer;
        this.string = string;
        this.anotherString = anotherString;
        this.bool = bool;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Testiranje{" +
                "integer=" + integer +
                ", string='" + string + '\'' +
                ", anotherString='" + anotherString + '\'' +
                ", bool=" + bool +
                ", date=" + date +
                '}';
    }
}

public class Start {

    public static void main(String[] args){
        DBExecutor executor = new DBExecutor(new DBConfig(
           "den1.mssql1.gear.host",
           null,
           "testiranjejave",
           "testiranjejave",
           "Lu00?H1-GJKa",
           1433,
           false
        ));

        Function<Object[], Testiranje> converter =
                array -> new Testiranje((int)array[0], (int) array[2], (String)array[1], (String)array[4], (boolean)array[3], (Date)array[5]);

        List<Testiranje> resultSet = executor.executeQuery("SELECT * FROM Testiranje", converter);
        for (Testiranje doctor : resultSet) {
            System.out.println(doctor);
        }

        boolean done;

        System.out.println("====================================");

        done = executor.executeProcedure("removeItem", new SQLParameter<Integer>(4));
        System.out.println(done);

        System.out.println("====================================");

        SQLParameter<Integer> idParameter = new SQLParameter<Integer>(true, -1);

        done = executor.executeProcedure("addItem",
                new SQLParameter<String>("Unosim jos jedan item"),
                new SQLParameter<Integer>(34),
                new SQLParameter<Boolean>(false),
                new SQLParameter<String>("Radiiiiiiiiiii"),
                idParameter);

        //int id = byteArrayToInt((byte[])idParameter.getValue());
        System.out.println(idParameter.getValue().getClass().getTypeName());

        System.out.println("====================================");

        List<Testiranje> results = executor.executeProcedure("getItems", converter);
        for (Testiranje doctor : results) {
            System.out.println(doctor);
        }
    }
    public static int byteArrayToInt(byte[] b)
    {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
}
