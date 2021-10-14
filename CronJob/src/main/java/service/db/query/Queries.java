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
    /*
    public void insert(Object query) throws SQLException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        StringBuffer str = new StringBuffer("INSERT INTO " +
                query.getClass().getDeclaredMethod("getTableName").invoke(query) + " SET ");
        stringMaker(str,query);
        logQ.info(connection.getNameDB() + str);
        System.out.println(str);
        statement.executeUpdate(String.valueOf(str));
    }
    public void deleteByID(Class myClass, int id) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, SQLException {
        logQ.info(connection.getNameDB() +
                "DELETE FROM " + myClass.getDeclaredMethod("getTableName").invoke(null) +
                " WHERE ID= " + id);
        statement.executeUpdate(
                "DELETE FROM " + myClass.getDeclaredMethod("getTableName").invoke(null) +
                        " WHERE ID= " + id);
    }
    public void updateByID(Object query, int id) throws SQLException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        StringBuffer str = new StringBuffer("UPDATE " +
                query.getClass().getDeclaredMethod("getTableName").invoke(query) + " SET ");
        stringMaker(str,query);
        str.append(" WHERE ID = " + id);
        logQ.info(connection.getNameDB() + str);
        statement.executeUpdate(String.valueOf(str));
    }
    public List selectAll(Class myClass) throws SQLException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchFieldException {
        logQ.info(connection.getNameDB() + " Select all from " +
                myClass.getDeclaredMethod("getTableName").invoke(null));
        ResultSet res = statement.executeQuery("SELECT * " + "FROM " +
                myClass.getDeclaredMethod("getTableName").invoke(null));
        List list = new LinkedList();
        Field[] fields = myClass.getDeclaredFields();
        Constructor constructor = myClass.getDeclaredConstructors()[0];
        Object[] objects = new Object[fields.length];
        while(res.next()) {
            for (int i = 0; i < objects.length; i++) {
                objects[i] = res.getObject(fields[i].getName());
            }
            list.add(constructor.newInstance(objects));
        }
        return list;
    }
    public List selectByRegistrationDate(Class myClass, Date date) throws SQLException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchFieldException {
        logQ.info(connection.getNameDB() + " Select all from " +
                myClass.getDeclaredMethod("getTableName").invoke(null) + " where registrationDate = '"
                + date.toString() + "'");
        System.out.println("SELECT * " + "FROM " +
                myClass.getDeclaredMethod("getTableName").invoke(null) + " where registrationDate = '"
                + date.toString() + "'");
        ResultSet res = statement.executeQuery("SELECT * " + "FROM " +
                myClass.getDeclaredMethod("getTableName").invoke(null) + " where registrationDate = '"
                + date.toString() + "'");
        List list = new LinkedList();
        Field[] fields = myClass.getDeclaredFields();
        Constructor constructor = myClass.getDeclaredConstructors()[0];
        Object[] objects = new Object[fields.length];
        while(res.next()) {
            for (int i = 0; i < objects.length; i++) {
                objects[i] = res.getObject(fields[i].getName());
            }
            list.add(constructor.newInstance(objects));
        }
        return list;
    }
    private StringBuffer stringMaker(StringBuffer str, Object table) throws IllegalAccessException {
        for(Field x : table.getClass().getDeclaredFields()) {
            x.setAccessible(true);
            if(x.getType() != String.class) {
                str.append(x.getName() + " = " +
                        x.get(table) + ", ");
            }
            else {
                str.append(x.getName() + " = '" + x.get(table) + "', ");
            }
        }
        str.delete(str.length()-2,str.length());
        return str;
    }

     */
}

