package service.db.query;

import org.apache.log4j.Logger;
import service.db.connection.DBConnection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class Queries {
    private DBConnection connection;
    private Statement statement;
    private static Logger logQ = Logger.getLogger(Queries.class.getName());
    public Queries(DBConnection connection) throws SQLException {
        this.connection = connection;
        statement = connection.getConnection().createStatement();
        logQ.info(connection.getNameDB() + " database. Created a statement.");
    }
    public void push(String query) throws SQLException {
        statement.executeUpdate(query);
        logQ.info(connection.getNameDB() + " " + query);
    }
    public <T> List<T> pull(String query, Class myClass) throws SQLException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        ResultSet resultSet = statement.executeQuery(query);
        List<T> list = new LinkedList();
        Field[] fields = myClass.getDeclaredFields();
        Constructor constructor = myClass.getDeclaredConstructors()[0];
        Object[] objects = new Object[fields.length];
        while(resultSet.next()) {
            for (int i = 0; i < objects.length; i++) {
                objects[i] = resultSet.getObject(fields[i].getName());
            }
            list.add((T)constructor.newInstance(objects));
        }
        logQ.info(connection.getNameDB() + " " + query);
        return list;
    }
}

