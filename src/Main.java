import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Connection connection;
    //static int currentlyloggedin;
    public static void main(String[] args) {
        //variables to connect to database
        String url = "jdbc:postgresql://localhost:5432/Project2";
        String user = "postgres";
        String password = "admin";
        try {
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
            //initiate login - ask which type of user is logging in
            Scanner scannerObj = new Scanner(System.in);
            System.out.println("Welcome to Chloe's Health and Fitness Club\n If you are a new member and would like to register," +
                    " please enter 1.\n If you already have an account and would like to login, please enter 2.");
            int choice1 = Integer.parseInt(scannerObj.nextLine());
            if(choice1 == 1){
                Members.registerUser();
            }
            if(choice1 == 2) {
                System.out.print("Please select your account type: \n 1. Member \n 2. Trainer \n 3. Administrative Staff \n Enter choice:");
                int choice2 = Integer.parseInt(scannerObj.nextLine());
                if (choice2 == 1) {
                    Members.memberLogin();
                }
                if (choice2 == 2) {
                    Trainers.trainerLogin();
                }
                if (choice2 == 3) {
                    AdminStaff.adminLogin();
                }
            }

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
