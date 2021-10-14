package service.db.TableQueryStringMaker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TableQueryStringMaker {
    private Class myClass;
    private Object table;
    public static String insertString(Object query) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        StringBuffer queryString = new StringBuffer("INSERT INTO ");
        queryString.append(query.getClass().getDeclaredMethod("getTableName").invoke(query));
        queryString.append(" SET ");
        stringMaker(queryString,query);
        return String.valueOf(queryString);
    }
    public static String deleteByIDString(Class myClass, int id) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, SQLException {
        StringBuffer queryString = new StringBuffer("DELETE FROM ");
        queryString.append(myClass.getDeclaredMethod("getTableName").invoke(null));
        queryString.append(" WHERE ID= ");
        queryString.append(id);
        return String.valueOf(queryString);
    }
    public static String updateByIDString(Object query, int id) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        StringBuffer queryString = new StringBuffer("UPDATE ");
        queryString.append(query.getClass().getDeclaredMethod("getTableName").invoke(query));
        queryString.append(" SET ");
        stringMaker(queryString,query);
        queryString.append(" WHERE ID = ");
        queryString.append(id);
        return String.valueOf(queryString);
    }
    public static String selectAllString(Class myClass) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        StringBuffer queryString = new StringBuffer();
        queryString.append("SELECT * FROM ");
        queryString.append(myClass.getDeclaredMethod("getTableName").invoke(null));
        return String.valueOf(queryString);
    }
    public static String selectByDateString(Class myClass, Field tableDate, Date date) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        StringBuffer queryString = new StringBuffer();
        queryString.append("SELECT * FROM ");
        queryString.append(myClass.getDeclaredMethod("getTableName").invoke(null));
        queryString.append(" WHERE ");
        queryString.append(tableDate.getName());
        queryString.append(" = '");
        queryString.append(date.toString());
        queryString.append("'");
        return String.valueOf(queryString);
    }
    public static String selectMoreThenTimeStampString(Class myClass, Field tableTime, Timestamp time) throws
            NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        StringBuffer queryString = new StringBuffer();
        queryString.append("SELECT * FROM ");
        queryString.append(myClass.getDeclaredMethod("getTableName").invoke(null));
        queryString.append(" WHERE ");
        queryString.append(tableTime.getName());
        queryString.append(" >= '");
        queryString.append(time.toString());
        queryString.append("'");
        return String.valueOf(queryString);
    }
    public static String selectLessThenDateString(Class myClass, Field tableDate, Date date) throws
            NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        StringBuffer queryString = new StringBuffer();
        queryString.append("SELECT * FROM ");
        queryString.append(myClass.getDeclaredMethod("getTableName").invoke(null));
        queryString.append(" WHERE ");
        queryString.append(tableDate.getName());
        queryString.append(" <= '");
        queryString.append(date.toString());
        queryString.append("'");
        return String.valueOf(queryString);
    }
    public static String selectOnTheSegmentDate(Class myClass, Field tableDate, Date leftDate, Date rightDate) throws
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuffer queryString = new StringBuffer();
        queryString.append("SELECT * FROM ");
        queryString.append(myClass.getDeclaredMethod("getTableName").invoke(null));
        queryString.append(" WHERE ");
        queryString.append(tableDate.getName());
        queryString.append(" >= '");
        queryString.append(leftDate.toString());
        queryString.append("' AND ");
        queryString.append(tableDate.getName());
        queryString.append(" <= '");
        queryString.append(rightDate.toString());
        queryString.append("'");
        return String.valueOf(queryString);
    }

    private static void stringMaker(StringBuffer str, Object table) throws IllegalAccessException {
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
    }
    /*
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
     */
}

