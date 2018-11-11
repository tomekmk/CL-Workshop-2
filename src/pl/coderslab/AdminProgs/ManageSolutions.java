package pl.coderslab.AdminProgs;

import pl.coderslab.Model.Exercise;
import pl.coderslab.Model.Group;
import pl.coderslab.Model.Solution;
import pl.coderslab.Model.User;
import pl.coderslab.Service.DBService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ManageSolutions {
    public static void main(String[] args) {
        String option = "";
        Scanner scan = new Scanner(System.in);
        Map<Integer, String> groups = new HashMap<>();
        Map<Integer, Exercise> exercises = new HashMap<>();

        try {
            Connection conn = DBService.getConnection();
            User[] users = User.loadAllUsers(conn);

            for (Exercise exercise : Exercise.loadAllExercises(conn))
                exercises.put(exercise.getId(), exercise);

            for (Group group : Group.loadAllGroups(conn))
                groups.put(group.getId(), group.getName());

            while (!option.equals("3")) {
                Solution[] solutions = Solution.loadAllSolutions(conn, null);
                showSolutionsAndMenu(users, groups);
                option = scan.nextLine();

                switch (option) {
                    case "1":
                        createNewSolution(conn, users, exercises);
                        break;
                    case "2":
                        viewSolution(solutions, users, exercises);
                        break;
                    case "3":
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


    static void createNewSolution(Connection conn, User[] users, Map<Integer, Exercise> exercises) {
        Scanner scan = new Scanner(System.in);
        int user_number = 0;
        int max_user_number = users.length;
        int exercise_id = 0;
        Solution solution = new Solution();

        System.out.print("\nWhich user you want to add the solution to? Enter the number: ");
        while (true) {
            try {
                user_number = Integer.parseInt(scan.nextLine());
                if (user_number <= max_user_number && user_number > 0) {
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter the correct user number: ");
        }
        solution.setUser_id(users[user_number - 1].getId());

        System.out.print("Enter description: ");    //dlaczego w zadaniu jest napisane, że pole description ma pozostać puste?
        solution.setDescription(scan.nextLine());

        System.out.println("Enter exercise id: ");
        Set<Integer> keyset = exercises.keySet();
        for (Integer key : keyset) {
            System.out.println("\tID: " + exercises.get(key).getId() + ". " + exercises.get(key).getTitle());
        }
        while (true) {
            try {
                exercise_id = Integer.parseInt(scan.nextLine());
                if (exercises.containsKey(exercise_id)) {
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter the correct exercise number: ");
        }
        solution.setExercise_id(exercise_id);

        try {
            solution.saveToDB(conn);
            if (solution.getId() != 0)
                System.out.println("The solution has been successfully added to the database!\n");
            else
                System.out.println("There is a problem here\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here\n");
        }
    }

    static void viewSolution(Solution[] solutions, User[] users, Map<Integer, Exercise> exercises) {
        Scanner scan = new Scanner(System.in);
        int user_number = 0;
        int max_user_number = users.length;

        System.out.print("Which user solutions you want to view? Enter the number: ");
        while (true) {
            try {
                user_number = Integer.parseInt(scan.nextLine());
                if (user_number <= max_user_number && user_number > 0) {
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter the correct user number: ");
        }

        System.out.println("\nUSER: " + users[user_number - 1].getUsername() + ":");
        int i = 0;
        for (Solution solution : solutions) {
            if (solution.getUser_id() == users[user_number - 1].getId()) {
                i++;
                System.out.println(i + ". BASE EXERCISE: " + exercises.get(solution.getExercise_id()).getTitle() +
                        ", SOLUTION CREATED: " + solution.getCreated() + ", UPDATED: " + solution.getUpdated());
                System.out.println("\tDESCRIPTION: " + solution.getDescription());
            }
        }
        System.out.println();
    }

    static void showSolutionsAndMenu(User[] users, Map<Integer, String> groups) {
        System.out.println("TABLE OF USERS:");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". NAME: " + users[i].getUsername() +
                    ", EMAIL: " + users[i].getEmail() + ", GROUP: " + groups.get(users[i].getUserGroupId()));
        }

        System.out.println("\nChoose one of the options:");
        System.out.print("1) add solution to user   2) view user solution   3) quit   |   ");
    }
}
