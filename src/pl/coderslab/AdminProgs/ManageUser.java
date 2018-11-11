package pl.coderslab.AdminProgs;

import pl.coderslab.Model.Group;
import pl.coderslab.Model.User;
import pl.coderslab.Service.BCrypt;
import pl.coderslab.Service.DBService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ManageUser {
    public static void main(String[] args) {
        String option = "";
        Scanner scan = new Scanner(System.in);
        Map<Integer, String> groups = new HashMap<>();

        try {
            Connection conn = DBService.getConnection();
            for (Group group : Group.loadAllGroups(conn)) {
                groups.put(group.getId(), group.getName());
            }

            while (!option.equals("4")) {
                User[] users = User.loadAllUsers(conn);
                showUsersAndMenu(groups, users);
                option = scan.nextLine();

                switch (option) {
                    case "1":
                        createNewUser(conn, groups);
                        break;
                    case "2":
                        editExistingUser(conn, groups, users);
                        break;
                    case "3":
                        deleteUser(conn, users);
                        break;
                    case "4":
                        System.out.println("Good bye ;)");
                        break;
                        default:
                            System.out.println("There is no such option");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static User enterUserParams(Map<Integer, String> groups, User existingUser) {
        User user = new User();
        if (existingUser != null)
            user = existingUser;

        Scanner scan = new Scanner(System.in);
        String str = "";
        int group_id = 0;

        System.out.print("Enter name: ");
        user.setUsername(scan.nextLine());

        System.out.print("Enter email: ");
        while (!str.contains("@")) {
            str = scan.nextLine();
            if (!str.contains("@"))
                System.out.print("Enter the correct email: ");
        }
        user.setEmail(str);

        System.out.print("Enter the password: ");
        user.setPassword(scan.nextLine());

        System.out.println("\nChoose one of the groups: ");
        Set<Integer> keyset = groups.keySet();
        for (Integer key : keyset) {
            System.out.println(key + " : " + groups.get(key));
        }

        while (true) {
            try {
                group_id = Integer.parseInt(scan.nextLine());
                if (groups.containsKey(group_id)) {
                    user.setUserGroupId(group_id);
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter the correct group: ");
        }
        return user;
    }

    static void editExistingUser(Connection conn, Map<Integer, String> groups, User[] users) {
        Scanner scan = new Scanner(System.in);
        int user_number = 0;
        int max_user_number = users.length;

        System.out.print("Which user you want to edit? Enter the number: ");
        while (true) {
            try {
                user_number = Integer.parseInt(scan.nextLine());
                if (user_number <= max_user_number && user_number > 0) {
                    users[user_number - 1] = enterUserParams(groups, users[user_number - 1]);
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter the correct user number: ");
        }

        try {
            users[user_number - 1].saveToDB(conn);
            System.out.println("The user has been successfully edited!\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here, email can be duplicated\n");
        }
    }

    static void createNewUser(Connection conn, Map<Integer, String> groups) {
        User user = enterUserParams(groups, null);
        try {
            user.saveToDB(conn);
            if (user.getId() != 0)
                System.out.println("The user has been successfully added to the database!\n");
            else
                System.out.println("There is a problem here\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here, email can be duplicated\n");
        }
    }

    static void deleteUser(Connection conn, User[] users) {
        Scanner scan = new Scanner(System.in);
        int user_number = 0;
        int max_user_number = users.length;

        System.out.print("Which user you want to delete? Enter the number: ");
        while (true) {
            try {
                user_number = Integer.parseInt(scan.nextLine());
                if (user_number <= max_user_number)
                    break;
            } catch (Exception e) {
            }
            System.out.println("Enter the correct user number: ");
        }

        try {
            users[user_number - 1].delete(conn);
            System.out.println("The user has been successfully deleted!\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here\n");
        }
    }

    static void showUsersAndMenu(Map<Integer, String> groups, User[] users) {
        System.out.println("TABLE OF USERS:");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". NAME: " + users[i].getUsername() +
                    ", EMAIL: " + users[i].getEmail() + ", GROUP: " + groups.get(users[i].getUserGroupId()));
        }

        System.out.println("\nChoose one of the options:");
        System.out.print("1) add   2) edit   3) delete   4) quit   |   ");
    }
}
