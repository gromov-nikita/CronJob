package service.handler.Cron;

import helpers.PropertiesHelper;
import models.users.User;
import service.db.query.Queries;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CronJob {
    private Queries queries;
    private int min = 1;
    private boolean work = true;
    public CronJob(Queries queries) {
        this.queries = queries;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMin() {
        return min;
    }
    public synchronized void writeStatistics() throws IOException, NoSuchFieldException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InterruptedException, SQLException, InstantiationException {
        Properties properties = PropertiesHelper.useProperty("src/main/resources/queries.properties");
        FileWriter writer = null;
        Calendar calendar;
        List<User> list;
        File file;
        startInputThread();
        while (work) {
            try {
                String path = PropertiesHelper.useProperty("src/main/resources/statisiticsInfo.properties")
                        .getProperty("statistics");
                file = addDateToFileName(path);
                writer = new FileWriter(file, true);
                calendar = GregorianCalendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, -5);
                list = queries.pull(properties.getProperty("users.moreThanLastLoginDate") + "'"
                        + new Timestamp(calendar.getTime().getTime()) + "'",User.class);
                writer.write("Date: " + new Timestamp(new java.util.Date().getTime()) + "\r\n");
                writer.write(" Users that logged in system last 5 hours:\r\n");
                for (User x : list) {
                    writer.write(x.getFirstName() + " " + x.getLastName() + "\r\n");
                }
                calendar = GregorianCalendar.getInstance();
                calendar.add(Calendar.YEAR,-1);
                list = queries.pull(properties.getProperty("users.lessThanRegistrationDate")
                        + "'" + new Date(calendar.getTime().getTime()) + "'", User.class);
                writer.write(" Count of users more than I years:\r\n");
                writer.write(list.size() + "\r\n");
                list = queries.pull(properties.getProperty("users.byRegistrationDate")
                        + "'" + new Date(Calendar.getInstance().getTime().getTime()) + "'",User.class);
                writer.write(" Users that registered per day:\r\n");
                for (User x : list) {
                    writer.write(x.getFirstName() + " " + x.getLastName() + "\r\n");
                }
                writer.write("\r\n___________________________________\r\n");
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
            wait(TimeUnit.MINUTES.toMillis(min));

        }
    }
    private File addDateToFileName(String path) {
        StringBuffer newPath = new StringBuffer(path);
        Date today = new Date(GregorianCalendar.getInstance().getTime().getTime());
        newPath.insert(newPath.length() - ".txt".length(), " " + today.toString());
        return new File(String.valueOf(newPath));
    }
    private void startInputThread() {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                String in;
                Scanner scanner = new Scanner(System.in);
                while (work) {
                    System.out.println("Enter command: ");
                    in = scanner.nextLine();
                    if (in.equals("STOP")) {
                        changeWorkStatus();
                        synchronized (CronJob.this) {
                            CronJob.this.notify();
                        }
                    } else {
                        System.out.println("Unknown command: " + in + "\nYou can try: \"STOP\"");
                    }
                }
            }
        }).start();
    }
    private synchronized void changeWorkStatus() {
        if(work == true) {
            work = false;
        }
        else {
            work = true;
        }
    }
}
