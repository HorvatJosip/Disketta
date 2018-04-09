package com.testing;

import com.lib.dal.access.DBExecutor;
import com.lib.dal.entities.DBConfig;
import com.lib.dal.entities.SQLParameter;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

class Doctor{
    int id;
    String name, surname, title;

    public Doctor(int id, String name, String surname, String title) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

public class Start {

    public static void main(String[] args){
        DBExecutor executor = new DBExecutor(new DBConfig(
           "localhost",
           "HOKISQL",
           "JAVASTART",
           "Administrator",
           null
        ));

        Function<Object[], Doctor> converter =
                array -> new Doctor((int)array[0], (String)array[1], (String)array[2], (String)array[3]);

        List<Doctor> resultSet = executor.executeQuery("SELECT * FROM Doctor", converter);
        for (Doctor doctor : resultSet) {
            System.out.println(doctor);
        }

        System.out.println("====================================");

        List<Doctor> results = executor.executeProcedure("getDoctors", converter);
        for (Doctor doctor : results) {
            System.out.println(doctor);
        }

        System.out.println("====================================");

        boolean done = executor.executeProcedure("deleteDoctor", new SQLParameter(8));
        System.out.println(done);
    }

}
