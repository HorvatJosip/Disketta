package com.lib.dal.access;

import com.lib.dal.entities.DBConfig;
import com.lib.dal.entities.SQLParameter;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DBExecutor {
    private SQLServerDataSource dataSource;

    public DBExecutor(DBConfig config) {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName(config.getServerName());
        dataSource.setInstanceName(config.getInstanceName());
        dataSource.setDatabaseName(config.getDatabaseName());
        dataSource.setUser(config.getUserName());
        dataSource.setPassword(config.getPassword());
        dataSource.setPortNumber(config.getPort());
        dataSource.setIntegratedSecurity(config.getIntegratedSecurity());
    }

    public <T> List<T> executeQuery(String query, Function<Object[], T> converter) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            return GetResults(resultSet, converter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public int executeProcedure(String procedureName, SQLParameter... parameters){
        return (int)exec(procedureName, null, parameters);
    }

    public <T> List<T> executeProcedure(String procedureName, Function<Object[], T> converter, SQLParameter... parameters){
        return (List<T>)exec(procedureName, converter, parameters);
    }

    private <T> List<T> GetResults(ResultSet resultSet, Function<Object[], T> converter)
            throws SQLException {
        List<T> result = new ArrayList<>();
        int numColumns = resultSet.getMetaData().getColumnCount();

        while (resultSet.next()) {
            Object[] columnData = new Object[numColumns];

            for (int i = 0; i < numColumns; i++) {
                columnData[i] = resultSet.getObject(i + 1);
            }

            result.add(converter.apply(columnData));
        }

        return result;
    }

    private <T> Object exec(String procedureName, Function<Object[], T> converter, SQLParameter... parameters) {
        String call = "{ CALL " + procedureName;

        if (parameters.length > 0) {

            StringBuilder stringBuilder = new StringBuilder(" ( ");

            for (int i = 0; i < parameters.length; i++) {
                stringBuilder.append("?");

                if (i != parameters.length - 1)
                    stringBuilder.append(", ");
            }

            stringBuilder.append(" )");
            call += stringBuilder.toString();

        }

        call += " }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(call)) {

            for (int i = 0; i < parameters.length; i++) {
                SQLParameter parameter = parameters[i];

                if (!parameter.getOutputParam())
                    statement.setObject(i + 1, parameter.getValue());
                else
                    statement.registerOutParameter(i + 1, parameter.getOutputType());
            }

            if(converter != null)
                return GetResults(statement.executeQuery(), converter);
            else{
                int changed = statement.executeUpdate();

                for (int i = 0; i < parameters.length; i++){
                    SQLParameter parameter = parameters[i];

                    if(parameter.getOutputParam())
                        parameter.setValue(statement.getObject(i + 1));
                }

                return  changed;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
