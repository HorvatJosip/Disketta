package com.testing;

import com.lib.dal.access.CsvHelper;
import com.lib.dal.access.DBExecutor;
import com.lib.dal.entities.DBConfig;
import com.lib.dal.entities.SQLParameter;
import com.lib.xml.XmlReader;
import com.lib.xml.XmlTag;
import com.lib.xml.XmlWriter;

import javax.swing.filechooser.FileSystemView;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class Start {
    private static String desktop;

    public static void main(String[] args) {
        desktop = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

        try {
            testDAL();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //testXML();
    }

    private static void testXML() {
        String filePath = desktop + "\\Disketta\\TestXML.xml";
        Function<List<String>, Food> converter = list -> new Food(list.get(0), list.get(1), list.get(2), list.get(3));

        try {
            XmlReader reader = new XmlReader(filePath);

            List<Food> food = reader.getObjects(
                    new XmlTag("food"),
                    converter,
                    new XmlTag("name"),
                    new XmlTag("price"),
                    new XmlTag("description"),
                    new XmlTag("calories")
            );

            for (Food item : food) {
                System.out.println(item);
            }

            System.out.println("============================");

            XmlWriter writer = new XmlWriter();
            writer.writeObjectData(desktop + "\\zapis.xml", food, "FoodRoot");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void testDAL() throws Exception {

        DBExecutor executor = new DBExecutor(new DBConfig(desktop + "\\CS.xml"));

        Function<Object[], TestObject> converter =
                array -> new TestObject((int) array[0], (int) array[2], (String) array[1], (String) array[4], (boolean) array[3], (Date) array[5]);

        List<TestObject> resultSet = executor.executeQuery("SELECT * FROM Testiranje", converter);
        for (TestObject test : resultSet) {
            System.out.println(test);
        }

        int rowsChanged;

        System.out.println("====================================");

        rowsChanged = executor.executeProcedure("removeItem", new SQLParameter<Integer>(new Integer(32)));
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

        String[] columnNames = {"String", "Integer", "Boolean", "AnotherString"};
        boolean insertSucceeded = executor.bulkInsert("Testiranje", columnNames, results, 5);

        System.out.println("Bulk insert succeeded: " + insertSucceeded);

        CsvHelper csvHelper = new CsvHelper();
        String csvPath = desktop + "\\csv_test.csv";
        //csvHelper.write(csvPath, columnNames, results, true);
        System.out.println(csvHelper.read(csvPath, 2, items -> new TestObject(
                1, Integer.parseInt(items[1]), items[0], items[3], Boolean.parseBoolean(items[2]), new Date()
        )));
    }
}
