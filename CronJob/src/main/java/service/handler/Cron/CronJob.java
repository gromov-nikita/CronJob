package service.handler.Cron;

import models.users.User;
import service.db.TableQueryStringMaker.TableQueryStringMaker;
import service.db.query.Queries;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CronJob {
    private Queries queries;
    private int min = 1;
    private String path;
    public CronJob(Queries queries) {
        this.queries = queries;
        Properties properties = new Properties();
        FileReader reader = null;
        try {
            reader = new FileReader("src/main/resources/statisiticsInfo.properties");
            properties.load(reader);
            path = properties.getProperty("statistics");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMin() {
        return min;
    }
    public void writeStatistics() {
        FileWriter writer = null;
        while (true) {
            try {
                writer = new FileWriter(path,true);
                List<User> list;
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, -5);
                list = queries.pull(TableQueryStringMaker.selectMoreThenTimeStampString(User.class,
                        User.class.getDeclaredField("lastLoginDate"),
                        new Timestamp(calendar.getTime().getTime())),User.class);
                writer.write(" Users that logged in system last 5 hours:\r\n");
                for (User x : list) {
                    writer.write(x.getFirstName() + " " + x.getLastName() + "\r\n");
                }
                calendar = GregorianCalendar.getInstance();
                calendar.add(Calendar.YEAR,-1);
                list = queries.pull(TableQueryStringMaker.selectLessThenDateString(User.class,
                        User.class.getDeclaredField("registrationDate"),new Date(calendar.getTime().getTime())),
                        User.class);
                writer.write(" Count of users more than I years:\r\n");
                writer.write(list.size() + "\r\n");

                list = queries.pull(TableQueryStringMaker.selectByDateString(User.class,
                        User.class.getDeclaredField("registrationDate"),
                        new Date(Calendar.getInstance().getTime().getTime())),User.class);
                writer.write(" Users that registered per day:\r\n");
                for (User x : list) {
                    writer.write(x.getFirstName() + " " + x.getLastName() + "\r\n");
                }
                writer.write("\r\n___________________________________\r\n");
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(min));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
