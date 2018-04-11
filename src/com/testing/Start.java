package com.testing;

import com.lib.dal.access.DBExecutor;
import com.lib.dal.entities.DBConfig;
import com.lib.dal.entities.SQLParameter;
import com.lib.xml.XmlHelpers;
import org.w3c.dom.Document;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class Start {

    public static void main(String[] args){
        //testDAL();

        testXML();
    }

    private static void testXML() {
        String filePath = "E:\\Windows Folders\\Desktop\\XML.xml";
        Document xmlDoc = XmlHelpers.TryGetXmlDocument(filePath, "UTF-8");


    }

    private static void testDAL(){

        DBExecutor executor = new DBExecutor(new DBConfig(
                "den1.mssql1.gear.host",
                null,
                "testiranjejave",
                "testiranjejave",
                "Lu00?H1-GJKa",
                1433,
                false
        ));

        Function<Object[], TestObject> converter =
                array -> new TestObject((int)array[0], (int) array[2], (String)array[1], (String)array[4], (boolean)array[3], (Date)array[5]);

        List<TestObject> resultSet = executor.executeQuery("SELECT * FROM TestObject", converter);
        for (TestObject test : resultSet) {
            System.out.println(test);
        }

        int rowsChanged;

        System.out.println("====================================");

        rowsChanged = executor.executeProcedure("removeItem", new SQLParameter<Integer>(new Integer(15)));
        System.out.println("Rows changed: " + rowsChanged);

        System.out.println("====================================");

        SQLParameter<Integer> idParameter = new SQLParameter<Integer>(Types.INTEGER);

        rowsChanged = executor.executeProcedure("addItem",
                new SQLParameter<String>("Unosim jos jedan item"),
                new SQLParameter<Integer>(new Integer(34)),
                new SQLParameter<Boolean>(false),
                new SQLParameter<String>("Radiiiiiiiiiii"),
                idParameter);

        System.out.println("New item ID: " + idParameter.getValue());
        System.out.println("Rows changed: " + rowsChanged);

        System.out.println("====================================");

        List<TestObject> results = executor.executeProcedure("getItems", converter);
        for (TestObject test : results) {
            System.out.println(test);
        }

    }
}
