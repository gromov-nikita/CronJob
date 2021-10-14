package models.users;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {
    private String firstName;
    private String lastName;
    private int age;
    private Date birthdayDate;
    private Timestamp lastLoginDate;
    private Date registrationDate;

    public User(String firstName, String lastName, int age, Date birthdayDate,
                Timestamp lastLoginDate, Date registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthdayDate = birthdayDate;
        this.lastLoginDate = lastLoginDate;
        this.registrationDate = registrationDate;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getAge() {
        return age;
    }
    public Date getBirthdayDate() {
        return birthdayDate;
    }
    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }
    public Date getRegistrationDate() {
        return registrationDate;
    }
    public static String getTableName() {
        return "user";
    }
}
