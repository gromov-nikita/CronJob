import service.db.connection.DBConnection;
import service.db.query.Queries;
import service.handler.Cron.CronJob;
import helpers.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

/*
Create table with users table. Table should include information about user: name, birthday, last login, age...,
registration date
Based on info from table,
cron should be started every X hours (time should be configured) - takes data from table and saved statistics in file:
names of users that logged in in system last  5 hours, count of users more than I years,
users that registered today
 */
public class Runner {
    public static void main(String[] args) {
        FileReader reader = null;
        try {
            Properties properties = PropertiesHelper.useProperty("src/main/resources/dbInfo.properties");
            CronJob cronJob = new CronJob(new Queries(DBConnection.getInstance(properties.getProperty("login"),
                    properties.getProperty("password"),
                    properties.getProperty("url"))));
            cronJob.writeStatistics();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
