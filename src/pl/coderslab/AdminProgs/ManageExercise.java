package pl.coderslab.AdminProgs;

import pl.coderslab.Model.Exercise;
import pl.coderslab.Service.DBService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageExercise {
    public static void main(String[] args) {
        String option = "";
        Scanner scan = new Scanner(System.in);

        try {
            Connection conn = DBService.getConnection();

            while (!option.equals("4")) {
                Exercise[] exercises = Exercise.loadAllExercises(conn);
                showExercisesAndMenu(exercises);
                option = scan.nextLine();

                switch (option) {
                    case "1":
                        createNewExercise(conn);
                        break;
                    case "2":
                        editExistingExercise(conn, exercises);
                        break;
                    case "3":
                        deleteExercise(conn, exercises);
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


    static Exercise enterExerciseParams(Exercise exercise) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter title: ");
        exercise.setTitle(scan.nextLine());

        System.out.print("Enter description: ");
        exercise.setDescription(scan.nextLine());

        return exercise;
    }

    static void editExistingExercise(Connection conn, Exercise[] exercises) {
        Scanner scan = new Scanner(System.in);
        int exercise_number = 0;
        int max_exercise_number = exercises.length;

        System.out.print("Which exercise you want to edit? Enter the number: ");
        while (true) {
            try {
                exercise_number = Integer.parseInt(scan.nextLine());
                if (exercise_number <= max_exercise_number && exercise_number > 0) {
                    exercises[exercise_number - 1] = enterExerciseParams(exercises[exercise_number - 1]);
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter the correct exercise number: ");
        }

        try {
            exercises[exercise_number - 1].saveToDB(conn);
            System.out.println("The exercise has been successfully edited!\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here\n");
        }
    }

    static void createNewExercise(Connection conn) {
        Exercise exercise = new Exercise();
        exercise = enterExerciseParams(exercise);
        try {
            exercise.saveToDB(conn);
            if (exercise.getId() != 0)
                System.out.println("The exercise has been successfully added to the database!\n");
            else
                System.out.println("There is a problem here\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here\n");
        }
    }

    static void deleteExercise(Connection conn, Exercise[] exercises) {
        Scanner scan = new Scanner(System.in);
        int exercise_number = 0;
        int max_exercise_number = exercises.length;

        System.out.print("Which exercise you want to delete? Enter the number: ");
        while (true) {
            try {
                exercise_number = Integer.parseInt(scan.nextLine());
                if (exercise_number <= max_exercise_number)
                    break;
            } catch (Exception e) {
            }
            System.out.println("Enter the correct exercise number: ");
        }

        try {
            exercises[exercise_number - 1].delete(conn);
            System.out.println("The exercise has been successfully deleted!\n");
        } catch (SQLException e) {
            System.out.println("There is a problem here, the exercise can be associated\n");
        }
    }

    static void showExercisesAndMenu(Exercise[] exercises) {
        System.out.println("TABLE OF EXERCISES:");
        for (int i = 0; i < exercises.length; i++) {
            System.out.println((i + 1) + ". NAME: " + exercises[i].getTitle()
            + ", DESCRIPTION: " + exercises[i].getDescription());
        }

        System.out.println("\nChoose one of the options:");
        System.out.print("1) add   2) edit   3) delete   4) quit   |   ");
    }
}
