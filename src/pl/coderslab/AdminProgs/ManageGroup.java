package pl.coderslab.AdminProgs;

import pl.coderslab.Model.Group;
import pl.coderslab.Service.DBService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageGroup {
    public static void main(String[] args) {
        String option = "";
        Scanner scan = new Scanner(System.in);

        try {
            Connection conn = DBService.getConnection();

            while (!option.equals("4")) {
                Group[] groups = Group.loadAllGroups(conn);
                showGroupsAndMenu(groups);
                option = scan.nextLine();

                switch (option) {
                    case "1":
                        createNewGroup(conn);
                        break;
                    case "2":
                        editExistingGroup(conn, groups);
                        break;
                    case "3":
                        deleteGroup(conn, groups);
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


    static Group enterGroupParams(Group group) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter name: ");
        group.setName(scan.nextLine());

        return group;
    }

    static void editExistingGroup(Connection conn, Group[] groups) {
        Scanner scan = new Scanner(System.in);
        int group_number = 0;
        int max_group_number = groups.length;

        System.out.print("Which group you want to edit? Enter the number: ");
        while (true) {
            try {
                group_number = Integer.parseInt(scan.nextLine());
                if (group_number <= max_group_number && group_number > 0) {
                    groups[group_number - 1] = enterGroupParams(groups[group_number - 1]);
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter the correct group number: ");
        }

        try {
            groups[group_number - 1].saveToDB(conn);
            System.out.println("The group has been successfully edited!\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here\n");
        }
    }

    static void createNewGroup(Connection conn) {
        Group group = new Group();
        group = enterGroupParams(group);
        try {
            group.saveToDB(conn);
            if (group.getId() != 0)
                System.out.println("The group has been successfully added to the database!\n");
            else
                System.out.println("There is a problem here\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here\n");
        }
    }

    static void deleteGroup(Connection conn, Group[] groups) {
        Scanner scan = new Scanner(System.in);
        int group_number = 0;
        int max_group_number = groups.length;

        System.out.print("Which group you want to delete? Enter the number: ");
        while (true) {
            try {
                group_number = Integer.parseInt(scan.nextLine());
                if (group_number <= max_group_number)
                    break;
            } catch (Exception e) {
            }
            System.out.println("Enter the correct group number: ");
        }

        try {
            groups[group_number - 1].delete(conn);
            System.out.println("The group has been successfully deleted!\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here, the group can be associated\n");
        }
    }

    static void showGroupsAndMenu(Group[] groups) {
        System.out.println("TABLE OF GROUPS:");
        for (int i = 0; i < groups.length; i++) {
            System.out.println((i + 1) + ". NAME: " + groups[i].getName());
        }

        System.out.println("\nChoose one of the options:");
        System.out.print("1) add   2) edit   3) delete   4) quit   |   ");
    }
}
