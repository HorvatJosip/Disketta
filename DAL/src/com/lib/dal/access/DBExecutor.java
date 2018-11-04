package com.lib.dal.access;

import com.lib.dal.entities.DBConfig;
import com.lib.dal.entities.SQLParameter;
import com.lib.dal.helpers.Utils;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public <T> boolean bulkInsert(String tableName, String[] columnNames, List<T> values, int batchSize) {
        if(tableName == null || columnNames == null || values == null ||
                columnNames.length == 0 || values.size() == 0 || batchSize < 1)
            throw new IllegalArgumentException();

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append('(');
        query.append(String.join(", ", columnNames));
        query.append(") VALUES (");
        for (int i = 0; i < columnNames.length; i++) {
            query.append('?');

            if (i != columnNames.length - 1)
                query.append(", ");
        }
        query.append(')');

        Method[] getters = Utils.getGetters(values.get(0).getClass(), columnNames).toArray(new Method[0]);

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try(PreparedStatement statement = connection.prepareStatement(query.toString())) {
                for (int i = 0; i < values.size(); i++) {
                    T value = values.get(i);

                    for (int j = 0; j < getters.length; j++)
                        statement.setObject(j + 1, getters[j].invoke(value));

                    statement.addBatch();

                    if (i + 1 % batchSize == 0)
                        statement.executeBatch();
                }

                if (values.size() % batchSize != 0)
                    statement.executeBatch();

                connection.commit();
                return true;
            }
            catch (Exception e){
                try {
                    System.out.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public <T> List<T> executeQuery(String query, Function<Object[], T> converter) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            return getResults(resultSet, converter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public int executeProcedure(String procedureName, SQLParameter... parameters) {
        return (int) exec(procedureName, null, parameters);
    }

    public <T> List<T> executeProcedure(String procedureName, Function<Object[], T> converter, SQLParameter... parameters) {
        return (List<T>) exec(procedureName, converter, parameters);
    }

    private <T> List<T> getResults(ResultSet resultSet, Function<Object[], T> converter)
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

            if (converter != null)
                return getResults(statement.executeQuery(), converter);
            else {
                int changed = statement.executeUpdate();

                for (int i = 0; i < parameters.length; i++) {
                    SQLParameter parameter = parameters[i];

                    if (parameter.getOutputParam())
                        parameter.setValue(statement.getObject(i + 1));
                }

                return changed;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
