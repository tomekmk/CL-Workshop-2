package pl.coderslab.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private static String DB_URL = "jdbc:mysql://localhost:3306/";
    private static String DB_NAME = "programming_school";
    private static String DB_PARAMS = "?useSSL=false&useUnicode=yes&characterEncoding=UTF-8";
    private static String DB_FIX = "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static String DB_USER = "root";
    private static String DB_PASS = "coderslab";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL + DB_NAME + DB_PARAMS + DB_FIX, DB_USER, DB_PASS);
    }

    private static String createDatabase = "CREATE SCHEMA programming_school";

    private static String createGroupsTable = "CREATE TABLE groups\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    name varchar(255)\n" +
            ");";

    private static String createUsersTable = "CREATE TABLE users\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    username varchar(255),\n" +
            "    email varchar(255) UNIQUE,\n" +
            "    password varchar(255),\n" +
            "    user_group_id int,\n" +
            "    CONSTRAINT users_groups_id_fk FOREIGN KEY (user_group_id) REFERENCES groups (id)\n" +
            ");\n";

    private static String createExercisesTable = "CREATE TABLE exercises\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    title varchar(255),\n" +
            "    description text\n" +
            ");";

    private static String createSolutionsTable = "CREATE TABLE solutions\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    created datetime DEFAULT current_timestamp,\n" +
            "    updated datetime,\n" +
            "    description text,\n" +
            "    exercise_id int,\n" +
            "    user_id int,\n" +
            "    CONSTRAINT solutions_exercises_id_fk FOREIGN KEY (exercise_id) REFERENCES exercises (id),\n" +
            "    CONSTRAINT solutions_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id)\n" +
            ");";
}