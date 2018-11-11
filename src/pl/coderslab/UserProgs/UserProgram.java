package pl.coderslab.UserProgs;

import pl.coderslab.Model.Exercise;
import pl.coderslab.Model.Solution;
import pl.coderslab.Model.User;
import pl.coderslab.Service.DBService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class UserProgram {
    public static void main(String[] args) {
        String option = "";
        Scanner scan = new Scanner(System.in);
        Integer user_id = 0;

        try {
            Connection conn = DBService.getConnection();
            if (args.length != 0)
                user_id = Integer.parseInt(args[0]);
            else {
                for (User user : User.loadAllUsers(conn)) {
                    System.out.println("ID: " + user.getId() + ", NAME: " + user.getUsername());
                }
                System.out.print("Enter user id: ");
                user_id = scan.nextInt();
                System.out.println();
            }

            User user;
            user = User.loadUserById(conn, user_id);
            if (user == null)
                throw new Exception();

            Map<Integer, Exercise> exercises = new HashMap<>();
            for (Exercise exercise : Exercise.loadAllExercises(conn)) {
                exercises.put(exercise.getId(), exercise);
            }

            while (!option.equals("3")) {
                Exercise[] missingExercises = Exercise.loadMissingUserExercises(conn, user_id);
                Solution[] userSolutions = Solution.loadAllSolutions(conn, user_id);

                showMenu(missingExercises);
                option = scan.nextLine();
                System.out.println();

                switch (option) {
                    case "1":
                        addNewSolution(conn, missingExercises, user);
                        break;
                    case "2":
                        viewSolution(userSolutions, exercises);
                        break;
                    case "3":
                        System.out.println("Good bye ;)");
                        break;
                    default:
                        System.out.println("There is no such option");
                }
            }

        } catch (InputMismatchException e) {
            System.out.println("Enter the correct number next time!");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("There is no such user here");
        }
    }

    static void addNewSolution(Connection conn, Exercise[] missingExercises, User user) {
        Scanner scan = new Scanner(System.in);
        Integer exerciseId = 0;
        Solution solution = new Solution();
        Set<Integer> missingExercisesId = new HashSet<>();
        for (Exercise exercise : missingExercises) {
            missingExercisesId.add(exercise.getId());
        }

        System.out.print("Which exercise solution you want to add the solution to? Enter the number: ");
        while (true) {
            try {
                exerciseId = Integer.parseInt(scan.nextLine());
                if (missingExercisesId.contains(exerciseId)) {
                    break;
                }
            } catch (Exception e) {
            }
            System.out.print("Enter the correct exercise number: ");
        }

        solution.setUser_id(user.getId());
        solution.setExercise_id(exerciseId);

        System.out.print("Enter description: ");
        solution.setDescription(scan.nextLine());

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

    static void viewSolution(Solution[] solutions, Map<Integer, Exercise> exercises) {
        int i = 0;
        for (Solution solution : solutions) {
            i++;
            System.out.println(i + ". BASE EXERCISE: " + exercises.get(solution.getExercise_id()).getTitle() +
                    ", SOLUTION CREATED: " + solution.getCreated() + ", UPDATED: " + solution.getUpdated());
            System.out.println("\tDESCRIPTION: " + solution.getDescription());
        }
        System.out.println();
    }

    static void showMenu(Exercise[] missingExercises) {
        System.out.println("TABLE OF MISSING EXERCISES:");
        for (Exercise exercise : missingExercises) {
            System.out.println("ID: " + exercise.getId() + ", TITLE: " + exercise.getTitle() +
            ", DESCRIPTION: " + exercise.getDescription());
        }

        System.out.println("\nChoose one of the options:");
        System.out.print("1) add new solution   2) view your solutions   3) quit   |   ");
    }
}
